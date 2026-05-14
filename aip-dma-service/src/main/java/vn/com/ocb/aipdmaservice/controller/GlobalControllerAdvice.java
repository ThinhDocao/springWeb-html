package vn.com.ocb.aipdmaservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.com.ocb.aipdmaservice.model.Category;
import vn.com.ocb.aipdmaservice.service.AppService;
import vn.com.ocb.aipdmaservice.service.SiteSettingService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final AppService appService;
    private final SiteSettingService siteSettingService;

    @ModelAttribute("categories")
    public List<Category> globalCategories(HttpServletRequest request) {
        if (isAdminRequest(request)) {
            return Collections.emptyList();
        }
        return appService.getCategories();
    }

    @ModelAttribute("popularProducts")
    public List<vn.com.ocb.aipdmaservice.model.Product> globalPopularProducts(HttpServletRequest request) {
        if (isAdminRequest(request)) {
            return Collections.emptyList();
        }
        return appService.getPopularProducts();
    }

    @ModelAttribute("siteSettings")
    public Map<String, String> globalSiteSettings(HttpServletRequest request) {
        if (isAdminRequest(request)) {
            return Collections.emptyMap();
        }
        return siteSettingService.getSettingsMap();
    }

    private boolean isAdminRequest(HttpServletRequest request) {
        return request != null && request.getRequestURI() != null && request.getRequestURI().startsWith("/admin");
    }
}
