package vn.com.ocb.aipdmaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ocb.aipdmaservice.entity.BlogPostEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
    Optional<BlogPostEntity> findBySlug(String slug);
    List<BlogPostEntity> findByIsPublishedTrueOrderByPublishDateDesc();
    List<BlogPostEntity> findByCategory_SlugAndIsPublishedTrueOrderByPublishDateDesc(String categorySlug);
}
