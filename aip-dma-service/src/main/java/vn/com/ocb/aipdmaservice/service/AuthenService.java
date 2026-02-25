package vn.com.ocb.aipdmaservice.service;

import vn.com.ocb.aipdmaservice.repository.AuthenticationResultDto;
import vn.com.ocb.omni.soap.ws.bian.partyauthentication.RequestAuthenticationOutType;

public interface AuthenService {
    AuthenticationResultDto authen(String username, String password);
}
