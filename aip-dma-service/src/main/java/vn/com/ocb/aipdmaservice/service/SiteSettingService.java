package vn.com.ocb.aipdmaservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.ocb.aipdmaservice.entity.SiteSettingEntity;
import vn.com.ocb.aipdmaservice.repository.SiteSettingRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SiteSettingService {

    public static final String[] EDITABLE_KEYS = {
            "siteName", "logoUrl", "faviconUrl", "hotline", "email", "address", "workingHours",
            "facebookUrl", "zaloUrl", "youtubeUrl", "tiktokUrl", "footerDescription",
            "googleAnalyticsScript", "facebookPixelScript", "chatWidgetScript"
    };

    private final SiteSettingRepository siteSettingRepository;

    public Map<String, String> getSettingsMap() {
        Map<String, String> settings = new LinkedHashMap<>();
        for (String key : EDITABLE_KEYS) {
            settings.put(key, "");
        }
        List<SiteSettingEntity> rows = siteSettingRepository.findAll();
        for (SiteSettingEntity row : rows) {
            settings.put(row.getSettingKey(), row.getSettingValue());
        }
        return settings;
    }

    public String get(String key, String fallback) {
        return siteSettingRepository.findBySettingKey(key)
                .map(SiteSettingEntity::getSettingValue)
                .filter(value -> value != null && !value.trim().isEmpty())
                .orElse(fallback);
    }

    @Transactional
    public void saveSettings(Map<String, String> values) {
        for (String key : EDITABLE_KEYS) {
            upsert(key, values.get(key));
        }
    }

    @Transactional
    public void upsert(String key, String value) {
        SiteSettingEntity setting = siteSettingRepository.findBySettingKey(key)
                .orElseGet(() -> SiteSettingEntity.builder().settingKey(key).build());
        setting.setSettingValue(value == null ? "" : value.trim());
        siteSettingRepository.save(setting);
    }
}
