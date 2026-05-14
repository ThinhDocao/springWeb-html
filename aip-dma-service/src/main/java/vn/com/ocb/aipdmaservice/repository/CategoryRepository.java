package vn.com.ocb.aipdmaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ocb.aipdmaservice.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findBySlug(String slug);
    List<CategoryEntity> findAllByOrderBySortOrderAsc();
    List<CategoryEntity> findByIsActiveTrueOrderBySortOrderAsc();
    List<CategoryEntity> findByParentIsNullOrderBySortOrderAsc();
    List<CategoryEntity> findByParentIsNullAndIsActiveTrueOrderBySortOrderAsc();
    List<CategoryEntity> findByParent_IdAndIsActiveTrueOrderBySortOrderAsc(Long parentId);
}
