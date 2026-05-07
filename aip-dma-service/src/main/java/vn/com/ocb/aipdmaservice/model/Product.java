package vn.com.ocb.aipdmaservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String slug;
    private String price;
    private String originalPrice;
    private String description;
    private String shortDescription;
    private String category;
    private String categorySlug;
    private String material;
    private String size;
    private String weight;
    private String imageUrl;
    private List<String> images;
    private boolean bestSeller;
    private boolean newProduct;
    private boolean premium;
    private String fengShuiMeaning;
    private String detailDescription;
    private String specifications;
}
