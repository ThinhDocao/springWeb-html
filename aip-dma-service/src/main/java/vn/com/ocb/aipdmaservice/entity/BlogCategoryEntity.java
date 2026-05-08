package vn.com.ocb.aipdmaservice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "blog_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String slug;

    @Column(length = 500)
    private String description;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;
}
