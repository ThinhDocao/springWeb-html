package vn.com.ocb.aipdmaservice.repository;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResultDto {
    private Boolean credentialsCorrect;
    private Boolean changePasswordRequired;
    private Boolean tokenSynchronizationRequired;
    private Boolean accessBlocked;
    private String redirectUrl;
    private String message;
    private Boolean changePasswordInformationRequired;
    private String authenticationInCorrectReason;
    private Boolean firstLogon;
    private Boolean systemMigration;
    private String language;
    private Boolean authMethodChoosingRequired;
    private Boolean secondCredentialsRequired;
    private AccessProfileDetailsDto accessProfileDetails;
    private Boolean firstLogonCustReferral;
}
