package vn.com.ocb.aipdmaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ocb.aipdmaservice.entity.SiteSettingEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiteSettingRepository extends JpaRepository<SiteSettingEntity, Long> {
    Optional<SiteSettingEntity> findBySettingKey(String settingKey);
    List<SiteSettingEntity> findBySettingKeyIn(Collection<String> settingKeys);
}
