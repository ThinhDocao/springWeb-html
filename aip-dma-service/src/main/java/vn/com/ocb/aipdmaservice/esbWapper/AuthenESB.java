package vn.com.ocb.aipdmaservice.esbWapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.ocb.omni.soap.ws.bian.AuthenticateCommandType;
import vn.com.ocb.omni.soap.ws.bian.AuthenticateHeaderType;
import vn.com.ocb.omni.soap.ws.bian.BranchInfoType;
import vn.com.ocb.omni.soap.ws.bian.TransactionInfoType;
import vn.com.ocb.omni.soap.ws.bian.partyauthentication.PartyAuthentication;
import vn.com.ocb.omni.soap.ws.bian.partyauthentication.RequestAuthenticationInType;
import vn.com.ocb.omni.soap.ws.bian.partyauthentication.RequestAuthenticationOutType;

@Service
public class AuthenESB {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PartyAuthentication partyAuthenticationWebservice;

    public RequestAuthenticationOutType authenticationOutType(String username, String password){
        AuthenticateCommandType authCmd = new AuthenticateCommandType();
        authCmd.setLogin(username);
        authCmd.setCredentials(password);
        authCmd.setLogonTryContext(null);
        authCmd.setAuthenticateType(null);
        authCmd.setResourceId(null);

        AuthenticateHeaderType authenticationHeader = new AuthenticateHeaderType();
        authenticationHeader.setAuthorization("Bearer");
        authenticationHeader.setAcceptLanguage("en");

        RequestAuthenticationInType soapInParam = new RequestAuthenticationInType();
        soapInParam.setTransactionInfo(getTransactionInfoSOAP());
        soapInParam.setAuthenticateCommand(authCmd);
        soapInParam.setAuthenticateHeader(authenticationHeader);
        RequestAuthenticationOutType soapResult = partyAuthenticationWebservice.requestAuthentication(soapInParam);
        return soapResult;
    }

    public TransactionInfoType getTransactionInfoSOAP() {
        String userLogin = null;
        TransactionInfoType result = new TransactionInfoType();
        result.setClientCode("OMNI");
        result.setCRefNum("112233");
        result.setUserId("test1");

        BranchInfoType branchInfo = new BranchInfoType();
        branchInfo.setBranchCode("internetbanking");
        result.setBranchInfo(branchInfo);

        return result;
    }
}
