package vn.com.ocb.aipdmaservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPost {
    private Long id;
    private String title;
    private String slug;
    private String metaTitle;
    private String metaDescription;
    private String excerpt;
    private String content;
    private String category;
    private String imageUrl;
    private String publishDate;
    private String author;
}
