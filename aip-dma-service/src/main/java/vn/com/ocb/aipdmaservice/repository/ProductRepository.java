package vn.com.ocb.aipdmaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ocb.aipdmaservice.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findBySlug(String slug);
    List<ProductEntity> findAllByOrderBySortOrderAsc();
    List<ProductEntity> findByNameContainingIgnoreCaseOrderBySortOrderAsc(String keyword);
    List<ProductEntity> findByCategory_SlugAndIsActiveTrueOrderBySortOrderAsc(String categorySlug);
    List<ProductEntity> findByCategory_Parent_SlugAndIsActiveTrueOrderBySortOrderAsc(String parentCategorySlug);
    List<ProductEntity> findByIsBestSellerTrueAndIsActiveTrueOrderBySortOrderAsc();
    List<ProductEntity> findByIsNewTrueAndIsActiveTrueOrderBySortOrderAsc();
    List<ProductEntity> findByIsPremiumTrueAndIsActiveTrueOrderBySortOrderAsc();
    long countByCategory_SlugAndIsActiveTrue(String categorySlug);
    long countByCategory_Parent_SlugAndIsActiveTrue(String parentCategorySlug);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM ProductEntity p LEFT JOIN p.category c LEFT JOIN c.parent cp LEFT JOIN p.material m WHERE " +
           "(:catSlug = 'san-pham' OR c.slug = :catSlug OR cp.slug = :catSlug) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:material IS NULL OR m.name = :material) AND " +
           "p.isActive = true ORDER BY p.sortOrder ASC")
    List<ProductEntity> findFiltered(
            @org.springframework.data.repository.query.Param("catSlug") String catSlug,
            @org.springframework.data.repository.query.Param("minPrice") java.math.BigDecimal minPrice,
            @org.springframework.data.repository.query.Param("maxPrice") java.math.BigDecimal maxPrice,
            @org.springframework.data.repository.query.Param("material") String material);
    @org.springframework.data.jpa.repository.Query("SELECT p FROM ProductEntity p WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY p.sortOrder ASC")
    List<ProductEntity> searchByKeyword(@org.springframework.data.repository.query.Param("keyword") String keyword, org.springframework.data.domain.Pageable pageable);

    List<ProductEntity> findByIsBestSellerTrueAndIsActiveTrueOrderBySortOrderAsc(org.springframework.data.domain.Pageable pageable);
}
