package vn.com.ocb.aipdmaservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vn.com.ocb.aipdmaservice.entity.BlogCategoryEntity;
import vn.com.ocb.aipdmaservice.entity.BlogPostEntity;
import vn.com.ocb.aipdmaservice.entity.CategoryEntity;
import vn.com.ocb.aipdmaservice.entity.ProductEntity;
import vn.com.ocb.aipdmaservice.repository.BlogCategoryRepository;
import vn.com.ocb.aipdmaservice.repository.BlogPostRepository;
import vn.com.ocb.aipdmaservice.repository.CategoryRepository;
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

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            log.info("Database is empty. Initializing seed data...");
            seedData();
            log.info("Seed data initialized successfully!");
        } else {
            log.info("Database already contains data. Skipping initialization.");
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

        // 2. Seed Products
        List<Product> modelProducts = ProductDataProvider.getAllProducts();
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
                    .price(price)
                    .originalPrice(originalPrice)
                    .shortDescription(p.getShortDescription())
                    .description(p.getShortDescription())
                    .isBestSeller(p.isBestSeller())
                    .isNew(p.isNewProduct())
                    .isPremium(p.isPremium())
                    .build();
            productRepository.save(pe);
        }

        // 3. Seed Blog Posts
        BlogCategoryEntity defaultBlogCat = BlogCategoryEntity.builder()
                .name("Tin Tức Chung")
                .slug("tin-tuc-chung")
                .build();
        blogCategoryRepository.save(defaultBlogCat);

        List<BlogPost> modelPosts = ProductDataProvider.getAllBlogPosts();
        for (BlogPost bp : modelPosts) {
            BlogPostEntity bpe = BlogPostEntity.builder()
                    .title(bp.getTitle())
                    .slug(bp.getSlug())
                    .category(defaultBlogCat)
                    .excerpt(bp.getExcerpt())
                    .imageUrl(bp.getImageUrl())
                    .author(bp.getAuthor())
                    .publishDate(LocalDate.now())
                    .isPublished(true)
                    .build();
            blogPostRepository.save(bpe);
        }
    }
}
