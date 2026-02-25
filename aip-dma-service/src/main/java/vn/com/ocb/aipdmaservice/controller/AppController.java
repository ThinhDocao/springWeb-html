package vn.com.ocb.aipdmaservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import vn.com.ocb.aipdmaservice.enumDefine.AuthenticationInCorrectReason;
import vn.com.ocb.aipdmaservice.helper.jwtToken;
import vn.com.ocb.aipdmaservice.repository.AccessProfileDetailsDto;
import vn.com.ocb.aipdmaservice.repository.AccessProfileDetailsResponse;
import vn.com.ocb.aipdmaservice.repository.AuthenticationResultDto;
import vn.com.ocb.aipdmaservice.repository.TokenLoginResponse;
import vn.com.ocb.aipdmaservice.request.LoginRequest;
import vn.com.ocb.aipdmaservice.service.AuthenService;
import vn.com.ocb.omni.soap.ws.bian.*;
import vn.com.ocb.omni.soap.ws.bian.partyauthentication.PartyAuthentication;
import vn.com.ocb.omni.soap.ws.bian.partyauthentication.RequestAuthenticationInType;
import vn.com.ocb.omni.soap.ws.bian.partyauthentication.RequestAuthenticationOutType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Controller
@Slf4j
@Data
@Builder
public class AppController {

    @Autowired
    private jwtToken jwtTokenUtil;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PartyAuthentication partyAuthenticationWebservice;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenService authenService;

    @GetMapping("/customer-journey")
    public String auth(@RequestParam Map<String, Object> params, Model model) {

        if ((String) params.get("aip") != null && params.get("aip").toString().equals("yes")) {
            model.addAttribute("aip","yes");
            return "aip_main";
        } else if ((String) params.get("dma") != null && params.get("dma").toString().equals("yes"))
        {
            model.addAttribute("aip","no");
            return "dma_main";
        }

        return "error";
    }

    @RequestMapping("/")
    public RedirectView Hello(RedirectAttributes attributes) {
//            attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        return new RedirectView("customer-journey?aip=yes");
    }

    @PostMapping("login")
    @ResponseBody
    public TokenLoginResponse login(@RequestBody LoginRequest params) {

        AuthenticationResultDto result = authenService.authen(params.getUsername(),params.getPassword());

        if (result == null) {
            return TokenLoginResponse.builder()
                    .credentialsCorrect(false)
                    .changePasswordRequired(false)
                    .accessBlocked(false)
                    .accessProfileDetails(null)
                    .message("")
                    .authenticationInCorrectReason(AuthenticationInCorrectReason.CREDENTIAL_INCORRECT.name())
                    .firstLogon(false)
                    .authMethodChoosingRequired(false)
                    .jwtToken(null)
                    .firstLogonCustReferral(false)
                    .build();
        }


        TokenLoginResponse response = TokenLoginResponse.builder()
                .credentialsCorrect(result.getCredentialsCorrect())
                .changePasswordRequired(result.getChangePasswordRequired())
                .accessBlocked(result.getAccessBlocked())
                .accessProfileDetails(convertToAccessProfileDetails(result.getAccessProfileDetails()))
                .message(result.getMessage())
                .authenticationInCorrectReason(result.getAuthenticationInCorrectReason())
                .firstLogon(result.getFirstLogon())
                .firstLogonCustReferral(result.getFirstLogonCustReferral())
                .jwtToken(Boolean.TRUE.equals(result.getCredentialsCorrect()) ? jwtTokenUtil.generateJWTToken("OMNI-APP", result.getAccessProfileDetails()) : null)
                .build();

        return response;
    }

    private AccessProfileDetailsResponse convertToAccessProfileDetails(AccessProfileDetailsDto profileDTO) {
        if (profileDTO == null) {
            return null;
        }

        AccessProfileDetailsResponse profile = new AccessProfileDetailsResponse();
        profile.setLogin(profileDTO.getLogin());
        profile.setCustomerId(profileDTO.getCustomerId());
        profile.setRole(profileDTO.getRole());
        profile.setOrgUnit(profileDTO.getOrgUnit());
        profile.setAccessProfileId(profileDTO.getAccessProfileId());
        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName(profileDTO.getLastName());
        profile.setLastCorrectLogonDate(profileDTO.getLastCorrectLogonDate());
        profile.setLastIncorrectLogonDate(profileDTO.getLastIncorrectLogonDate());
        profile.setCif(profileDTO.getCif());
        profile.setPackageType(profileDTO.getPackageType());
        profile.setCustType(profileDTO.getCustType());

        return profile;
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


    @GetMapping("/login")
    public String success(@RequestParam Map<String, Object> params) {
        return "login";
    }

}
