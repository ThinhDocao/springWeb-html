package vn.com.ocb.aipdmaservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.ocb.aipdmaservice.model.Product;
import vn.com.ocb.aipdmaservice.service.AppService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final AppService appService;

    @GetMapping
    public List<Product> search(@RequestParam(value = "q", required = false) String query) {
        if (query == null || query.trim().length() < 2) {
            return Collections.emptyList();
        }
        return appService.searchProducts(query.trim());
    }
}
