package vn.com.ocb.aipdmaservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.com.ocb.aipdmaservice.model.OrderRequest;
import vn.com.ocb.aipdmaservice.model.Product;
import vn.com.ocb.aipdmaservice.service.AppService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final AppService appService;

    @GetMapping("/cart/products")
    public List<Product> getCartProducts(@RequestParam("ids") String ids) {
        List<Long> productIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return appService.getProductsByIds(productIds);
    }

    @PostMapping("/order")
    public Map<String, Object> createOrder(@RequestBody OrderRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String orderCode = appService.createOrder(request);
            response.put("success", true);
            response.put("orderCode", orderCode);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}
