package vn.com.ocb.aipdmaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ocb.aipdmaservice.entity.BlogCategoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategoryEntity, Long> {
    Optional<BlogCategoryEntity> findBySlug(String slug);
    List<BlogCategoryEntity> findAllByOrderByNameAsc();
    List<BlogCategoryEntity> findByIsActiveTrueOrderByNameAsc();
}
