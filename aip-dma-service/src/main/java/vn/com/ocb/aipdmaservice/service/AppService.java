package vn.com.ocb.aipdmaservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.ocb.aipdmaservice.entity.BlogPostEntity;
import vn.com.ocb.aipdmaservice.entity.CategoryEntity;
import vn.com.ocb.aipdmaservice.entity.ProductEntity;
import vn.com.ocb.aipdmaservice.model.BlogPost;
import vn.com.ocb.aipdmaservice.model.Category;
import vn.com.ocb.aipdmaservice.model.Product;
import vn.com.ocb.aipdmaservice.entity.BlogCategoryEntity;
import vn.com.ocb.aipdmaservice.repository.BlogCategoryRepository;
import vn.com.ocb.aipdmaservice.model.BlogCategory;
import vn.com.ocb.aipdmaservice.repository.BlogPostRepository;
import vn.com.ocb.aipdmaservice.repository.CategoryRepository;
import vn.com.ocb.aipdmaservice.repository.ProductRepository;
import vn.com.ocb.aipdmaservice.repository.ContactInquiryRepository;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BlogPostRepository blogPostRepository;
    private final BlogCategoryRepository blogCategoryRepository;
    private final vn.com.ocb.aipdmaservice.repository.SiteSettingRepository siteSettingRepository;
    private final vn.com.ocb.aipdmaservice.repository.OrderRepository orderRepository;
    private final ContactInquiryRepository contactInquiryRepository;

    private static final DecimalFormat df = new DecimalFormat("#,###₫");

    private final CacheEntry<List<Product>> bestSellersCache = new CacheEntry<>(30000);
    private final CacheEntry<List<Product>> newProductsCache = new CacheEntry<>(30000);
    private final CacheEntry<List<Product>> premiumProductsCache = new CacheEntry<>(30000);
    private final CacheEntry<List<Product>> allProductsCache = new CacheEntry<>(30000);
    private final CacheEntry<List<BlogPost>> allBlogPostsCache = new CacheEntry<>(30000);
    private final CacheEntry<java.util.Map<String, String>> siteSettingsCache = new CacheEntry<>(30000);
    private final CacheEntry<List<Category>> categoriesCache = new CacheEntry<>(30000);
    private final CacheEntry<List<Product>> popularProductsCache = new CacheEntry<>(30000);

    private static class CacheEntry<T> {
        private volatile T value;
        private volatile long expiry;
        private final long ttl;

        public CacheEntry(long ttlMillis) {
            this.ttl = ttlMillis;
        }

        public T get(java.util.function.Supplier<T> supplier) {
            long now = System.currentTimeMillis();
            if (value == null || now > expiry) {
                value = supplier.get();
                expiry = now + ttl;
            }
            return value;
        }
    }

    public java.util.Map<String, String> getAllSiteSettings() {
        return siteSettingsCache.get(() -> 
            siteSettingRepository.findAll().stream()
                    .collect(java.util.stream.Collectors.toMap(
                            vn.com.ocb.aipdmaservice.entity.SiteSettingEntity::getSettingKey,
                            vn.com.ocb.aipdmaservice.entity.SiteSettingEntity::getSettingValue,
                            (existing, replacement) -> existing
                    ))
        );
    }

    public List<Category> getCategories() {
        return categoriesCache.get(() -> {
            List<CategoryEntity> rootEntities = categoryRepository.findByParentIsNullAndIsActiveTrueOrderBySortOrderAsc();
            return rootEntities.stream().map(this::mapToCategory).collect(Collectors.toList());
        });
    }

    public CategoryEntity getCategoryEntityBySlug(String slug) {
        return categoryRepository.findBySlug(slug).orElse(null);
    }

    public List<Product> getAllProducts() {
        return allProductsCache.get(() -> 
            productRepository.findAll().stream().map(this::mapToProduct).collect(Collectors.toList())
        );
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
        
        String materialFilter = (material != null && !material.isEmpty() && !material.equals("all")) ? material : null;
        
        return productRepository.findFiltered(categorySlug, min, max, materialFilter)
                .stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> getProductsByCategory(String categorySlug) {
        List<ProductEntity> directProducts = productRepository.findByCategory_SlugAndIsActiveTrueOrderBySortOrderAsc(categorySlug);
        List<ProductEntity> subProducts = productRepository.findByCategory_Parent_SlugAndIsActiveTrueOrderBySortOrderAsc(categorySlug);
        
        java.util.Set<ProductEntity> allProducts = new java.util.LinkedHashSet<>(directProducts);
        allProducts.addAll(subProducts);
        
        return allProducts.stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> getRelatedProducts(String categorySlug, String currentSlug) {
        List<Product> related = getProductsByCategory(categorySlug);
        
        if (related.size() <= 1) {
            Optional<CategoryEntity> catOpt = categoryRepository.findBySlug(categorySlug);
            if (catOpt.isPresent() && catOpt.get().getParent() != null) {
                related = getProductsByCategory(catOpt.get().getParent().getSlug());
            }
        }
        
        return related.stream()
                .filter(p -> !p.getSlug().equals(currentSlug))
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Product> getBestSellers() {
        return bestSellersCache.get(() -> 
            productRepository.findByIsBestSellerTrueAndIsActiveTrueOrderBySortOrderAsc()
                    .stream().map(this::mapToProduct).collect(Collectors.toList())
        );
    }

    public List<Product> getNewProducts() {
        return newProductsCache.get(() -> 
            productRepository.findByIsNewTrueAndIsActiveTrueOrderBySortOrderAsc()
                    .stream().map(this::mapToProduct).collect(Collectors.toList())
        );
    }

    public List<Product> getPremiumProducts() {
        return premiumProductsCache.get(() -> 
            productRepository.findByIsPremiumTrueAndIsActiveTrueOrderBySortOrderAsc()
                    .stream().map(this::mapToProduct).collect(Collectors.toList())
        );
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword, org.springframework.data.domain.PageRequest.of(0, 8))
                .stream().map(this::mapToProduct).collect(Collectors.toList());
    }

    public List<Product> getPopularProducts() {
        return popularProductsCache.get(() -> 
            productRepository.findByIsBestSellerTrueAndIsActiveTrueOrderBySortOrderAsc(org.springframework.data.domain.PageRequest.of(0, 4))
                    .stream().map(this::mapToProduct).collect(Collectors.toList())
        );
    }

    public Product getProductBySlug(String slug) {
        return productRepository.findBySlug(slug).map(this::mapToProduct).orElse(null);
    }

    public List<BlogPost> getAllBlogPosts() {
        return allBlogPostsCache.get(() -> 
            blogPostRepository.findByIsPublishedTrueOrderByPublishDateDesc()
                    .stream().map(this::mapToBlogPost).collect(Collectors.toList())
        );
    }

    public List<BlogPost> getBlogPostsByCategorySlug(String slug) {
        return blogPostRepository.findByCategory_SlugAndIsPublishedTrueOrderByPublishDateDesc(slug)
                .stream().map(this::mapToBlogPost).collect(Collectors.toList());
    }

    public List<BlogCategory> getAllBlogCategories() {
        return blogCategoryRepository.findAll()
                .stream().map(this::mapToBlogCategory).collect(Collectors.toList());
    }

    public BlogPost getBlogPostBySlug(String slug) {
        return blogPostRepository.findBySlug(slug).map(this::mapToBlogPost).orElse(null);
    }

    public List<Product> getProductsByIds(List<Long> ids) {
        return productRepository.findAllById(ids).stream()
                .map(this::mapToProduct)
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional
    public void createContactInquiry(String fullName, String phone, String email, String subject, String message,
                                     String sourcePage, String productSlug, String productName) {
        vn.com.ocb.aipdmaservice.entity.ContactInquiryEntity inquiry =
                vn.com.ocb.aipdmaservice.entity.ContactInquiryEntity.builder()
                        .fullName(fullName)
                        .phone(phone)
                        .email(email)
                        .subject(subject)
                        .message(message)
                        .sourcePage(sourcePage)
                        .productSlug(productSlug)
                        .productName(productName)
                        .status("NEW")
                        .isRead(false)
                        .build();
        contactInquiryRepository.save(inquiry);
    }

    @org.springframework.transaction.annotation.Transactional
    public String createOrder(vn.com.ocb.aipdmaservice.model.OrderRequest request) {
        String orderCode = "DD-" + java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd").format(java.time.LocalDateTime.now()) + "-" + String.format("%04d", (orderRepository.count() + 1));
        
        java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;
        
        vn.com.ocb.aipdmaservice.entity.OrderEntity order = vn.com.ocb.aipdmaservice.entity.OrderEntity.builder()
                .orderCode(orderCode)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .confirmationMethod(request.getConfirmationMethod())
                .note(request.getNote())
                .status("PENDING")
                .build();
        
        List<vn.com.ocb.aipdmaservice.entity.OrderItemEntity> items = new java.util.ArrayList<>();
        for (vn.com.ocb.aipdmaservice.model.OrderItemRequest itemReq : request.getItems()) {
            ProductEntity product = productRepository.findById(itemReq.getProductId()).orElse(null);
            if (product != null) {
                java.math.BigDecimal itemPrice = product.getPrice() != null ? product.getPrice() : java.math.BigDecimal.ZERO;
                totalAmount = totalAmount.add(itemPrice.multiply(java.math.BigDecimal.valueOf(itemReq.getQuantity())));
                
                String primaryImg = product.getImages().stream()
                        .filter(vn.com.ocb.aipdmaservice.entity.ProductImageEntity::isPrimary)
                        .map(vn.com.ocb.aipdmaservice.entity.ProductImageEntity::getImageUrl)
                        .findFirst()
                        .orElse(product.getImages().isEmpty() ? "" : product.getImages().get(0).getImageUrl());

                items.add(vn.com.ocb.aipdmaservice.entity.OrderItemEntity.builder()
                        .order(order)
                        .product(product)
                        .productName(product.getName())
                        .productSlug(product.getSlug())
                        .productImageUrl(primaryImg)
                        .price(itemPrice)
                        .quantity(itemReq.getQuantity())
                        .build());
            }
        }
        
        order.setTotalAmount(totalAmount);
        order.setItems(items);
        
        orderRepository.save(order);
        return orderCode;
    }

    private Category mapToCategory(CategoryEntity entity) {
        Category dto = new Category();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        dto.setImageUrl(entity.getImageUrl());
        dto.setDescription(entity.getDescription());
        dto.setMetaTitle(entity.getMetaTitle());
        dto.setMetaDescription(entity.getMetaDescription());
        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
        }
        dto.setLevel(entity.getLevel());
        
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
        dto.setMetaTitle(entity.getMetaTitle());
        dto.setMetaDescription(entity.getMetaDescription());
        
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
        
        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            String primaryImageUrl = entity.getImages().stream()
                    .filter(vn.com.ocb.aipdmaservice.entity.ProductImageEntity::isPrimary)
                    .map(vn.com.ocb.aipdmaservice.entity.ProductImageEntity::getImageUrl)
                    .findFirst()
                    .orElse(entity.getImages().get(0).getImageUrl());
            
            dto.setImageUrl(primaryImageUrl);
            
            dto.setImages(entity.getImages().stream()
                    .map(vn.com.ocb.aipdmaservice.entity.ProductImageEntity::getImageUrl)
                    .collect(java.util.stream.Collectors.toList()));
        } else {
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
        dto.setMetaTitle(entity.getMetaTitle());
        dto.setMetaDescription(entity.getMetaDescription());
        if (entity.getCategory() != null) {
            dto.setCategory(entity.getCategory().getName());
        }
        dto.setExcerpt(entity.getExcerpt());
        dto.setContent(entity.getContent());
        dto.setImageUrl(entity.getImageUrl());
        dto.setAuthor(entity.getAuthor());
        dto.setPublishDate(entity.getPublishDate() != null ? entity.getPublishDate().toString() : "N/A");
        return dto;
    }

    private BlogCategory mapToBlogCategory(BlogCategoryEntity entity) {
        return BlogCategory.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .isActive(entity.isActive())
                .build();
    }
}
