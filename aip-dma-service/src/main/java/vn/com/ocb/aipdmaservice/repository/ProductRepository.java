package vn.com.ocb.aipdmaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ocb.aipdmaservice.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findBySlug(String slug);
    List<ProductEntity> findByCategory_SlugAndIsActiveTrueOrderBySortOrderAsc(String categorySlug);
    List<ProductEntity> findByCategory_Parent_SlugAndIsActiveTrueOrderBySortOrderAsc(String parentCategorySlug);
    List<ProductEntity> findByIsBestSellerTrueAndIsActiveTrueOrderBySortOrderAsc();
    List<ProductEntity> findByIsNewTrueAndIsActiveTrueOrderBySortOrderAsc();
    List<ProductEntity> findByIsPremiumTrueAndIsActiveTrueOrderBySortOrderAsc();
    long countByCategory_SlugAndIsActiveTrue(String categorySlug);
    long countByCategory_Parent_SlugAndIsActiveTrue(String parentCategorySlug);
}
