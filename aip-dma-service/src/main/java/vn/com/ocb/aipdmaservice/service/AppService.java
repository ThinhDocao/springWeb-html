package vn.com.ocb.aipdmaservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.ocb.aipdmaservice.entity.BlogPostEntity;
import vn.com.ocb.aipdmaservice.entity.CategoryEntity;
import vn.com.ocb.aipdmaservice.entity.ProductEntity;
import vn.com.ocb.aipdmaservice.model.BlogPost;
import vn.com.ocb.aipdmaservice.model.Category;
import vn.com.ocb.aipdmaservice.model.Product;
import vn.com.ocb.aipdmaservice.repository.BlogPostRepository;
import vn.com.ocb.aipdmaservice.repository.CategoryRepository;
import vn.com.ocb.aipdmaservice.repository.ProductRepository;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BlogPostRepository blogPostRepository;

    private static final DecimalFormat df = new DecimalFormat("#,###₫");

    public List<Category> getCategories() {
        List<CategoryEntity> rootEntities = categoryRepository.findByParentIsNullAndIsActiveTrueOrderBySortOrderAsc();
        return rootEntities.stream().map(this::mapToCategory).collect(Collectors.toList());
    }

    public CategoryEntity getCategoryEntityBySlug(String slug) {
        return categoryRepository.findBySlug(slug).orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> getFilteredProducts(String categorySlug, String priceRange, String material) {
        java.math.BigDecimal min = null;
        java.math.BigDecimal max = null;
        
        if (priceRange != null && !priceRange.isEmpty() && !priceRange.equals("all")) {
            switch (priceRange) {
                case "under-5m":
                    max = new java.math.BigDecimal("5000000");
                    break;
                case "5-10m":
                    min = new java.math.BigDecimal("5000000");
                    max = new java.math.BigDecimal("10000000");
                    break;
                case "10-20m":
                    min = new java.math.BigDecimal("10000000");
                    max = new java.math.BigDecimal("20000000");
                    break;
                case "over-20m":
                    min = new java.math.BigDecimal("20000000");
                    break;
            }
        }
        
        // Treat empty or "all" as null for the query
        String materialFilter = (material != null && !material.isEmpty() && !material.equals("all")) ? material : null;
        
        return productRepository.findFiltered(categorySlug, min, max, materialFilter)
                .stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> getProductsByCategory(String categorySlug) {
        // Fetch products by exactly this category
        List<ProductEntity> directProducts = productRepository.findByCategory_SlugAndIsActiveTrueOrderBySortOrderAsc(categorySlug);
        // Also fetch products of its subcategories
        List<ProductEntity> subProducts = productRepository.findByCategory_Parent_SlugAndIsActiveTrueOrderBySortOrderAsc(categorySlug);
        
        directProducts.addAll(subProducts);
        return directProducts.stream().distinct().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> getBestSellers() {
        return productRepository.findByIsBestSellerTrueAndIsActiveTrueOrderBySortOrderAsc()
                .stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> getNewProducts() {
        return productRepository.findByIsNewTrueAndIsActiveTrueOrderBySortOrderAsc()
                .stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> getPremiumProducts() {
        return productRepository.findByIsPremiumTrueAndIsActiveTrueOrderBySortOrderAsc()
                .stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword, org.springframework.data.domain.PageRequest.of(0, 8))
                .stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> getPopularProducts() {
        return productRepository.findByIsBestSellerTrueAndIsActiveTrueOrderBySortOrderAsc(org.springframework.data.domain.PageRequest.of(0, 4))
                .stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public Product getProductBySlug(String slug) {
        return productRepository.findBySlug(slug).map(this::mapToProduct).orElse(null);
    }

    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findByIsPublishedTrueOrderByPublishDateDesc()
                .stream().map(this::mapToBlogPost).collect(Collectors.toList());
    }

    public BlogPost getBlogPostBySlug(String slug) {
        return blogPostRepository.findBySlug(slug).map(this::mapToBlogPost).orElse(null);
    }

    // --- MAPPERS ---

    private Category mapToCategory(CategoryEntity entity) {
        Category dto = new Category();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        dto.setImageUrl(entity.getImageUrl());
        dto.setDescription(entity.getDescription());
        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
        }
        dto.setLevel(entity.getLevel());
        
        // Calculate product count (own + subcategories)
        long count = productRepository.countByCategory_SlugAndIsActiveTrue(entity.getSlug());
        count += productRepository.countByCategory_Parent_SlugAndIsActiveTrue(entity.getSlug());
        dto.setProductCount((int) count);
        
        if (entity.getSubCategories() != null) {
            dto.setSubCategories(entity.getSubCategories().stream().map(this::mapToCategory).collect(Collectors.toList()));
        }
        return dto;
    }

    private Product mapToProduct(ProductEntity entity) {
        Product dto = new Product();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        
        if (entity.getCategory() != null) {
            dto.setCategory(entity.getCategory().getName());
            dto.setCategorySlug(entity.getCategory().getSlug());
        }
        if (entity.getMaterial() != null) {
            dto.setMaterial(entity.getMaterial().getName());
        } else {
            dto.setMaterial("Đồng cao cấp");
        }
        
        if (entity.getPrice() == null) {
            dto.setPrice("0₫");
        } else if (entity.getPrice().compareTo(java.math.BigDecimal.ZERO) == 0) {
            dto.setPrice("Liên hệ");
        } else {
            dto.setPrice(df.format(entity.getPrice()));
        }
        
        if (entity.getOriginalPrice() != null && entity.getOriginalPrice().compareTo(java.math.BigDecimal.ZERO) > 0) {
            dto.setOriginalPrice(df.format(entity.getOriginalPrice()));
        } else {
            dto.setOriginalPrice("0₫");
        }

        dto.setShortDescription(entity.getShortDescription());
        dto.setDescription(entity.getDescription());
        dto.setDetailDescription(entity.getDetailDescription());
        dto.setFengShuiMeaning(entity.getFengShuiMeaning());
        dto.setSpecifications(entity.getSpecifications());
        dto.setSize(entity.getSize());
        dto.setWeight(entity.getWeight());
        
        dto.setBestSeller(entity.isBestSeller());
        dto.setNewProduct(entity.isNew());
        dto.setPremium(entity.isPremium());
        
        // Map images from database
        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            // Find primary image or use the first one
            String primaryImageUrl = entity.getImages().stream()
                    .filter(vn.com.ocb.aipdmaservice.entity.ProductImageEntity::isPrimary)
                    .map(vn.com.ocb.aipdmaservice.entity.ProductImageEntity::getImageUrl)
                    .findFirst()
                    .orElse(entity.getImages().get(0).getImageUrl());
            
            dto.setImageUrl(primaryImageUrl);
            
            // Map all image URLs
            dto.setImages(entity.getImages().stream()
                    .map(vn.com.ocb.aipdmaservice.entity.ProductImageEntity::getImageUrl)
                    .collect(java.util.stream.Collectors.toList()));
        } else {
            // Fallback if no images in DB
            dto.setImageUrl("/images/product-tuong-dong.png");
            dto.setImages(java.util.Arrays.asList("/images/product-tuong-dong.png"));
        }
        
        return dto;
    }

    private BlogPost mapToBlogPost(BlogPostEntity entity) {
        BlogPost dto = new BlogPost();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setSlug(entity.getSlug());
        if (entity.getCategory() != null) {
            dto.setCategory(entity.getCategory().getName());
        }
        dto.setExcerpt(entity.getExcerpt());
        dto.setImageUrl(entity.getImageUrl());
        dto.setAuthor(entity.getAuthor());
        dto.setPublishDate(entity.getPublishDate() != null ? entity.getPublishDate().toString() : "N/A");
        return dto;
    }
}
