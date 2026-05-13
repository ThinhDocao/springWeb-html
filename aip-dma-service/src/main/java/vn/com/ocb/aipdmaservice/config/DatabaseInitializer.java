package vn.com.ocb.aipdmaservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.ocb.aipdmaservice.entity.BlogCategoryEntity;
import vn.com.ocb.aipdmaservice.entity.BlogPostEntity;
import vn.com.ocb.aipdmaservice.entity.CategoryEntity;
import vn.com.ocb.aipdmaservice.entity.ProductEntity;
import vn.com.ocb.aipdmaservice.entity.MaterialEntity;
import vn.com.ocb.aipdmaservice.repository.BlogCategoryRepository;
import vn.com.ocb.aipdmaservice.repository.BlogPostRepository;
import vn.com.ocb.aipdmaservice.repository.CategoryRepository;
import vn.com.ocb.aipdmaservice.repository.MaterialRepository;
import vn.com.ocb.aipdmaservice.repository.ProductImageRepository;
import vn.com.ocb.aipdmaservice.repository.ProductRepository;
import vn.com.ocb.aipdmaservice.model.ProductDataProvider;
import vn.com.ocb.aipdmaservice.model.Category;
import vn.com.ocb.aipdmaservice.model.Product;
import vn.com.ocb.aipdmaservice.model.BlogPost;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BlogPostRepository blogPostRepository;
    private final BlogCategoryRepository blogCategoryRepository;
    private final MaterialRepository materialRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            log.info("Database is empty. Initializing seed data...");
            seedData();
            log.info("Seed data initialized successfully!");
        } else {
            log.info("Database already contains data.");
            // Update materials and sync product details/images to fulfill user request
            ensureMaterialsAndAssignToProducts();
            syncProductDetailsAndImages();
            syncBlogPostsAndCategories();
        }
    }

    @Transactional
    private void syncBlogPostsAndCategories() {
        log.info("Syncing blog posts and categories...");
        // Ensure the old "Tin Tức Chung" only setup is overwritten or updated
        List<BlogPost> modelPosts = ProductDataProvider.getAllBlogPosts();
        
        for (BlogPost bp : modelPosts) {
            // Find or create category based on the mock data category string
            String catName = bp.getCategory() != null ? bp.getCategory() : "Tin Tức Khác";
            String catSlug = catName.toLowerCase().replace(" ", "-").replace("đ", "d")
                    .replace("á", "a").replace("à", "a").replace("ả", "a").replace("ã", "a").replace("ạ", "a")
                    .replace("ấ", "a").replace("ầ", "a").replace("ẩ", "a").replace("ẫ", "a").replace("ậ", "a")
                    .replace("é", "e").replace("è", "e").replace("ẻ", "e").replace("ẽ", "e").replace("ẹ", "e")
                    .replace("ế", "e").replace("ề", "e").replace("ể", "e").replace("ễ", "e").replace("ệ", "e")
                    .replace("í", "i").replace("ì", "i").replace("ỉ", "i").replace("ĩ", "i").replace("ị", "i")
                    .replace("ó", "o").replace("ò", "o").replace("ỏ", "o").replace("õ", "o").replace("ọ", "o")
                    .replace("ố", "o").replace("ồ", "o").replace("ổ", "o").replace("ỗ", "o").replace("ộ", "o")
                    .replace("ớ", "o").replace("ờ", "o").replace("ở", "o").replace("ỡ", "o").replace("ợ", "o")
                    .replace("ú", "u").replace("ù", "u").replace("ủ", "u").replace("ũ", "u").replace("ụ", "u")
                    .replace("ứ", "u").replace("ừ", "u").replace("ử", "u").replace("ữ", "u").replace("ự", "u")
                    .replace("ý", "y").replace("ỳ", "y").replace("ỷ", "y").replace("ỹ", "y").replace("ỵ", "y");
            
            BlogCategoryEntity catEntity = blogCategoryRepository.findAll().stream()
                    .filter(c -> c.getSlug().equals(catSlug))
                    .findFirst()
                    .orElseGet(() -> {
                        BlogCategoryEntity newCat = BlogCategoryEntity.builder()
                                .name(catName)
                                .slug(catSlug)
                                .build();
                        return blogCategoryRepository.save(newCat);
                    });

            java.util.Optional<BlogPostEntity> existingPostOpt = blogPostRepository.findBySlug(bp.getSlug());
            if (existingPostOpt.isPresent()) {
                BlogPostEntity pe = existingPostOpt.get();
                // Update existing
                pe.setCategory(catEntity);
                pe.setTitle(bp.getTitle());
                pe.setExcerpt(bp.getExcerpt());
                pe.setContent(bp.getContent());
                pe.setImageUrl(bp.getImageUrl());
                blogPostRepository.save(pe);
            } else {
                // Create new
                BlogPostEntity bpe = BlogPostEntity.builder()
                        .title(bp.getTitle())
                        .slug(bp.getSlug())
                        .category(catEntity)
                        .excerpt(bp.getExcerpt())
                        .content(bp.getContent())
                        .imageUrl(bp.getImageUrl())
                        .author(bp.getAuthor())
                        .publishDate(LocalDate.now())
                        .isPublished(true)
                        .build();
                blogPostRepository.save(bpe);
            }
        }
        log.info("Finished syncing blog posts and categories.");
    }

    @Transactional
    private void syncProductDetailsAndImages() {
        log.info("Syncing product details and images from data provider...");
        List<Product> modelProducts = ProductDataProvider.getAllProducts();
        for (Product p : modelProducts) {
            productRepository.findBySlug(p.getSlug()).ifPresent(pe -> {
                // Update missing details
                pe.setSize(p.getSize());
                pe.setWeight(p.getWeight());
                pe.setSpecifications(p.getSpecifications());
                pe.setFengShuiMeaning(p.getFengShuiMeaning());
                pe.setDetailDescription(p.getDetailDescription());
                
                // Sync images
                if (pe.getImages().isEmpty()) {
                    if (p.getImageUrl() != null) {
                        pe.getImages().add(vn.com.ocb.aipdmaservice.entity.ProductImageEntity.builder()
                                .imageUrl(p.getImageUrl())
                                .isPrimary(true)
                                .product(pe)
                                .build());
                    }
                    if (p.getImages() != null) {
                        for (String imgUrl : p.getImages()) {
                            if (!imgUrl.equals(p.getImageUrl())) {
                                pe.getImages().add(vn.com.ocb.aipdmaservice.entity.ProductImageEntity.builder()
                                        .imageUrl(imgUrl)
                                        .product(pe)
                                        .build());
                            }
                        }
                    }
                }
                productRepository.save(pe);
            });
        }
        log.info("Finished syncing product data.");
    }

    @Transactional
    private void ensureMaterialsAndAssignToProducts() {
        if (materialRepository.count() == 0) {
            seedMaterials();
        }
        
        List<MaterialEntity> materials = materialRepository.findAll();
        List<ProductEntity> products = productRepository.findAll();
        
        log.info("Found {} materials and {} products.", materials.size(), products.size());
        
        log.info("Updating existing products with random materials if needed...");
        int i = 0;
        for (ProductEntity p : products) {
            if (p.getMaterial() == null) {
                p.setMaterial(materials.get(i % materials.size()));
                productRepository.save(p);
                i++;
            }
        }
        log.info("Finished updating {} products with materials.", i);
    }

    @Transactional
    private void seedMaterials() {
        String[] names = {"Đồng đỏ", "Đồng vàng", "Đồng hun", "Đồng Cát tút"};
        for (String name : names) {
            if (!materialRepository.findByName(name).isPresent()) {
                materialRepository.save(MaterialEntity.builder().name(name).isActive(true).build());
            }
        }
    }

    private void seedData() {
        // 1. Seed Categories
        Map<String, CategoryEntity> categoryMap = new HashMap<>();
        List<Category> modelCategories = ProductDataProvider.getCategories();
        
        for (Category rootCat : modelCategories) {
            CategoryEntity rootEntity = CategoryEntity.builder()
                    .name(rootCat.getName())
                    .slug(rootCat.getSlug())
                    .imageUrl(rootCat.getImageUrl())
                    .description(rootCat.getDescription())
                    .level(0)
                    .isActive(true)
                    .build();
            categoryRepository.save(rootEntity);
            categoryMap.put(rootEntity.getSlug(), rootEntity);
            
            if (rootCat.getSubCategories() != null) {
                for (Category subCat : rootCat.getSubCategories()) {
                    CategoryEntity subEntity = CategoryEntity.builder()
                            .name(subCat.getName())
                            .slug(subCat.getSlug())
                            .parent(rootEntity)
                            .level(1)
                            .isActive(true)
                            .build();
                    categoryRepository.save(subEntity);
                    categoryMap.put(subEntity.getSlug(), subEntity);
                }
            }
        }

        // 2. Seed Materials
        seedMaterials();
        List<MaterialEntity> materials = materialRepository.findAll();

        // 3. Seed Products
        List<Product> modelProducts = ProductDataProvider.getAllProducts();
        int productIndex = 0;
        for (Product p : modelProducts) {
            CategoryEntity catEntity = categoryMap.get(p.getCategorySlug());
            
            BigDecimal price = null;
            BigDecimal originalPrice = null;
            try {
                if (p.getPrice() != null && !p.getPrice().isEmpty()) {
                    price = new BigDecimal(p.getPrice().replaceAll("[^0-9]", ""));
                }
                if (p.getOriginalPrice() != null && !p.getOriginalPrice().isEmpty()) {
                    originalPrice = new BigDecimal(p.getOriginalPrice().replaceAll("[^0-9]", ""));
                }
            } catch (Exception e) {
                price = BigDecimal.ZERO;
            }

            ProductEntity pe = ProductEntity.builder()
                    .name(p.getName())
                    .slug(p.getSlug())
                    .category(catEntity)
                    .material(materials.get(productIndex % materials.size())) // Randomly assign from our list
                    .price(price)
                    .originalPrice(originalPrice)
                    .shortDescription(p.getShortDescription())
                    .description(p.getShortDescription())
                    .detailDescription(p.getDetailDescription())
                    .fengShuiMeaning(p.getFengShuiMeaning())
                    .specifications(p.getSpecifications())
                    .size(p.getSize())
                    .weight(p.getWeight())
                    .isBestSeller(p.isBestSeller())
                    .isNew(p.isNewProduct())
                    .isPremium(p.isPremium())
                    .build();
            
            // Add images
            if (p.getImageUrl() != null) {
                pe.getImages().add(vn.com.ocb.aipdmaservice.entity.ProductImageEntity.builder()
                        .imageUrl(p.getImageUrl())
                        .isPrimary(true)
                        .product(pe)
                        .build());
            }
            if (p.getImages() != null) {
                for (String imgUrl : p.getImages()) {
                    if (!imgUrl.equals(p.getImageUrl())) {
                        pe.getImages().add(vn.com.ocb.aipdmaservice.entity.ProductImageEntity.builder()
                                .imageUrl(imgUrl)
                                .product(pe)
                                .build());
                    }
                }
            }
            
            productRepository.save(pe);
            productIndex++;
        }

        // 4. Seed Blog Posts
        syncBlogPostsAndCategories();
    }
}
