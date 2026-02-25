package vn.com.ocb.aipdmaservice.repository;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccessProfileDetailsDto {
    private String login;
    private String customerId;
    private String role;
    private String orgUnit;
    private String accessProfileId;
    private String firstName;
    private String lastName;
    private Date lastCorrectLogonDate;
    private Date lastIncorrectLogonDate;
    private Long cif;
    private String packageType;
    private String custType;
}
