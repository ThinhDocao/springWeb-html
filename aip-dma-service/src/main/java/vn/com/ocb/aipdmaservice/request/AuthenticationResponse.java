package vn.com.ocb.aipdmaservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

	@JsonProperty(value="credentialsCorrect")
	private Boolean credentialsCorrect;

	@JsonProperty(value="changePasswordRequired")
	private Boolean changePasswordRequired;

	@JsonProperty(value="accessBlocked")
	private Boolean accessBlocked;

	@JsonProperty(value="accessProfileDetails")
	private AccessProfileDetails accessProfileDetails;

	@JsonProperty(value="message")
	private String message;

	@JsonProperty(value="firstLogon")
	private Boolean firstLogon;

	@JsonProperty(value="authMethodChoosingRequired")
	private Boolean authMethodChoosingRequired;

	@JsonProperty(value="jwtToken")
	private String jwtToken;

	@JsonProperty(value="jwtRefreshToken")
	private String jwtRefreshToken;

	@JsonProperty(value="firstLogonCustReferral")
	private Boolean firstLogonCustReferral;

	@JsonProperty(value="_links")
	private Object links;

	private Long jwtTokenExpiredIn;

}
