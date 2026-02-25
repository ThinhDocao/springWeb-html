package vn.com.ocb.aipdmaservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import vn.com.ocb.aipdmaservice.enumDefine.AuthenticationInCorrectReason;
import vn.com.ocb.aipdmaservice.helper.jwtToken;
import vn.com.ocb.aipdmaservice.repository.AccessProfileDetailsDto;
import vn.com.ocb.aipdmaservice.repository.AccessProfileDetailsResponse;
import vn.com.ocb.aipdmaservice.repository.AuthenticationResultDto;
import vn.com.ocb.aipdmaservice.repository.TokenLoginResponse;
import vn.com.ocb.aipdmaservice.request.LoginRequest;

import java.util.Map;

@Controller
@Slf4j
@Data
@Builder
public class AppController {

    @Autowired
    private jwtToken jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/customer-journey")
    public String auth(@RequestParam Map<String, Object> params, Model model) {

        return "index";


//        if ((String) params.get("aip") != null && params.get("aip").toString().equals("yes")) {
//            model.addAttribute("aip", "yes");
//            return "aip_main";
//        } else if ((String) params.get("dma") != null && params.get("dma").toString().equals("yes")) {
//            model.addAttribute("aip", "no");
//            return "dma_main";
//        }
//
//        return "error";
    }

    @RequestMapping("/")
    public RedirectView Hello(RedirectAttributes attributes) {
//            attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        return new RedirectView("customer-journey?aip=yes");
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


    @GetMapping("/login")
    public String success(@RequestParam Map<String, Object> params) {
        return "login";
    }

}
