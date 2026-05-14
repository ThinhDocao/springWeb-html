package vn.com.ocb.aipdmaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ocb.aipdmaservice.entity.MaterialEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {
    Optional<MaterialEntity> findByName(String name);
    List<MaterialEntity> findAllByOrderByNameAsc();
    List<MaterialEntity> findByIsActiveTrueOrderByNameAsc();
}
