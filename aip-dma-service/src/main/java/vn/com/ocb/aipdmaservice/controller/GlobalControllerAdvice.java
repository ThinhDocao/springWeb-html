package vn.com.ocb.aipdmaservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.com.ocb.aipdmaservice.model.Category;
import vn.com.ocb.aipdmaservice.service.AppService;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final AppService appService;

    @ModelAttribute("categories")
    public List<Category> globalCategories() {
        return appService.getCategories();
    }
}
