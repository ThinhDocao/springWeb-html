package vn.com.ocb.aipdmaservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.com.ocb.aipdmaservice.esbWapper.AuthenESB;
import vn.com.ocb.aipdmaservice.repository.AccessProfileDetailsDto;
import vn.com.ocb.aipdmaservice.repository.AuthenticationResultDto;
import vn.com.ocb.aipdmaservice.service.AuthenService;
import vn.com.ocb.omni.soap.ws.bian.AccessProfileDetailsType;
import vn.com.ocb.omni.soap.ws.bian.AuthenticationResultType;
import vn.com.ocb.omni.soap.ws.bian.partyauthentication.PartyAuthentication;
import vn.com.ocb.omni.soap.ws.bian.partyauthentication.RequestAuthenticationOutType;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@Service
public class AuthenServiceImpl implements AuthenService {
    @Autowired
    private AuthenESB authenESB;

    @Override
    public AuthenticationResultDto authen(String username, String password) {
        RequestAuthenticationOutType soapResult = authenESB.authenticationOutType(username, password);

        if (soapResult == null) {
            return null;
        }
        if (!soapResult.getTransactionInfo().getTransactionReturnMsg().equals("EXECUTED")) {
            return null;
        }

        AuthenticationResultType authSoapResult = soapResult.getResourceAuthenticationResult().getAuthenticationResult();

        AccessProfileDetailsDto accessProfileDetailsDto = null;
        try {
            if (authSoapResult.getAccessProfileDetails() != null) {
                AccessProfileDetailsType profileSoap = authSoapResult.getAccessProfileDetails();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                accessProfileDetailsDto = AccessProfileDetailsDto.builder().login(profileSoap.getLogin()).customerId(profileSoap.getCustomerId()).role(profileSoap.getRole()).orgUnit(profileSoap.getOrgUnit()).accessProfileId(profileSoap.getAccessProfileId()).firstName(profileSoap.getFirstName()).lastName(profileSoap.getLastName()).cif(profileSoap.getCif()).packageType(profileSoap.getPackageType()).custType(profileSoap.getCustType()).build();
                if ((profileSoap.getLastCorrectLogonDate() != null) && (!profileSoap.getLastCorrectLogonDate().equals(""))) {
                    accessProfileDetailsDto.setLastCorrectLogonDate(dateFormat.parse(profileSoap.getLastCorrectLogonDate()));
                }
                if ((profileSoap.getLastIncorrectLogonDate() != null) && (!profileSoap.getLastIncorrectLogonDate().equals(""))) {
                    accessProfileDetailsDto.setLastIncorrectLogonDate(dateFormat.parse(profileSoap.getLastIncorrectLogonDate()));
                }
            }
        } catch (ParseException ex) {
            log.error("Parse exception", ex);
        }

        AuthenticationResultDto result = AuthenticationResultDto.builder()
                .credentialsCorrect(authSoapResult.isCredentialsCorrect())
                .changePasswordRequired(authSoapResult.isChangePasswordRequired())
                .tokenSynchronizationRequired(authSoapResult.isTokenSynchronizationRequired())
                .accessBlocked(authSoapResult.isAccessBlocked())
                .message(authSoapResult.getMessage())
                .changePasswordInformationRequired(authSoapResult.isChangePasswordInformationRequired())
                .firstLogon(authSoapResult.isFirstLogonStatus())
                .systemMigration(authSoapResult.isSystemMigration())
                .language(authSoapResult.getLanguage())
                .authMethodChoosingRequired(authSoapResult.isAuthMethodChoosingRequired())
                .secondCredentialsRequired(authSoapResult.isSecondCredentialsRequired())
                .firstLogonCustReferral(!StringUtils.isEmpty(authSoapResult.getFirstLogonCustReferral()) ? Boolean.valueOf(authSoapResult.getFirstLogonCustReferral()) : null)
                .build();
        if (!result.getCredentialsCorrect().booleanValue()) {
            result.setAuthenticationInCorrectReason(authSoapResult.getAuthenticationInCorrectReason());
        }
        if (!authSoapResult.getRedirectUrl().equals("")) {
            result.setRedirectUrl(authSoapResult.getRedirectUrl());
        }

        result.setAccessProfileDetails(accessProfileDetailsDto);

        return result;
    }
}
