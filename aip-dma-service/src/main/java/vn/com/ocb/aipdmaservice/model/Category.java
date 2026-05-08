package vn.com.ocb.aipdmaservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private Long parentId;
    private int level;
    private String name;
    private String slug;
    private String imageUrl;
    private String description;
    private int productCount;
    @Builder.Default
    private java.util.List<Category> subCategories = new java.util.ArrayList<>();
}
