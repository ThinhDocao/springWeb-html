package vn.com.ocb.aipdmaservice.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProductEntity product;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_slug")
    private String productSlug;

    @Column(name = "product_image_url")
    private String productImageUrl;

    @Column(precision = 15, scale = 0)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;
}
