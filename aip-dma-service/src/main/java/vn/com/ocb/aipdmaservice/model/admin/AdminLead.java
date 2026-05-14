package vn.com.ocb.aipdmaservice.model.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLead {
    private Long id;
    private String type;
    private String code;
    private String fullName;
    private String phone;
    private String email;
    private String subject;
    private String message;
    private String source;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
