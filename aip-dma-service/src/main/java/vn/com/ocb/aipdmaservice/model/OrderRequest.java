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
public class OrderRequest {
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String confirmationMethod;
    private String note;
    private List<OrderItemRequest> items;
}
