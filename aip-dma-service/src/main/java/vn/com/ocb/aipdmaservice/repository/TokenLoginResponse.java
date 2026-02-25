package vn.com.ocb.aipdmaservice.repository;

import lombok.Builder;
import lombok.Data;
import vn.com.ocb.aipdmaservice.request.AccessProfileDetails;

@Data
@Builder
public class TokenLoginResponse {
    private boolean credentialsCorrect;

    private boolean changePasswordRequired;

    private boolean accessBlocked;

    private AccessProfileDetailsResponse accessProfileDetails;

    private String message;

    private String authenticationInCorrectReason;

    private Boolean firstLogon;

    private boolean authMethodChoosingRequired;

    private String jwtToken;

    private Boolean firstLogonCustReferral;
}
