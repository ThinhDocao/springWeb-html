package vn.com.ocb.aipdmaservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.com.ocb.aipdmaservice.service.AppService;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final AppService appService;

    @ModelAttribute
    public void addGlobalAttributes(HttpServletRequest request, Model model) {
        String contextPath = request.getContextPath() == null ? "" : request.getContextPath();
        String requestUri = request.getRequestURI();
        String path = requestUri.startsWith(contextPath) ? requestUri.substring(contextPath.length()) : requestUri;
        if (path.startsWith("/admin")) {
            return;
        }

        model.addAttribute("siteSettings", appService.getAllSiteSettings());
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("popularProducts", appService.getPopularProducts());
    }
}
