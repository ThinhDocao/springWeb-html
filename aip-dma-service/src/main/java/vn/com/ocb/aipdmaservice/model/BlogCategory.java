package vn.com.ocb.aipdmaservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogCategory {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private boolean isActive;
}
