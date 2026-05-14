package vn.com.ocb.aipdmaservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.com.ocb.aipdmaservice.service.AppService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final AppService appService;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("siteSettings", appService.getAllSiteSettings());
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("popularProducts", appService.getPopularProducts());
    }
}
