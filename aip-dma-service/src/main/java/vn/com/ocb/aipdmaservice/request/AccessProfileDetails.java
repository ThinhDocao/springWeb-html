package vn.com.ocb.aipdmaservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessProfileDetails {

    @JsonProperty(value="login")
    private String login;

    @JsonProperty(value="customerId")
    private String customerId;

    @JsonProperty(value="orgUnit")
    private String orgUnit;

    @JsonProperty(value="accessProfileId")
    private String accessProfileId;

    @JsonProperty(value="firstName")
    private String firstName;

    @JsonProperty(value="lastCorrectLogonDate")
    private double lastCorrectLogonDate;

    @JsonProperty(value="cif")
    private long cif;

    @JsonProperty(value="packageType")
    private String packageType;

    @JsonProperty(value="custType")
    private String custType;

    private String userRole;

}
