package vn.com.ocb.aipdmaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ocb.aipdmaservice.entity.ProductImageEntity;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
}
