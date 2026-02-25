package vn.com.ocb.aipdmaservice.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vn.com.ocb.aipdmaservice.repository.AccessProfileDetailsDto;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Component
public class jwtToken implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${omni.secretKey}")
    private String secretKey;
    @Value("${omni.expireToken}")
    private int expireTimeInSecond;


    @Autowired
    private ObjectMapper objectMapper;

    public String generateJWTToken(String clientCode, AccessProfileDetailsDto accessProfileDetailsDto) {
        Map<String, Object> identities = new HashMap<>();
        identities.put("org_unit", accessProfileDetailsDto.getOrgUnit());
        identities.put("person_id", accessProfileDetailsDto.getCustomerId());
        identities.put("access_profile_id", accessProfileDetailsDto.getAccessProfileId());
        identities.put("first_name", accessProfileDetailsDto.getFirstName());
        identities.put("last_name", accessProfileDetailsDto.getLastName());
        identities.put("login", accessProfileDetailsDto.getLogin());
        identities.put("role", "CUSTOMER");
        identities.put("member_groups", null);
        identities.put("cif", accessProfileDetailsDto.getCif());

        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, expireTimeInSecond);

        Map<String, Object> map = new HashMap<>();
        map.put("identity", identities);
        map.put("exp", now.getTimeInMillis() / 1000);
        map.put("iss", null);
        map.put("user_name", accessProfileDetailsDto.getLogin());
        map.put("scope", new String[]{"read", "write"});
        map.put("authorities", new String[]{"ROLE_USER"});
        map.put("aud", new String[]{});
        map.put("jti", UUID.randomUUID().toString());
        map.put("client_id", "new-omni");
        map.put("client_session_id", "213123123123");
        map.put("source", StringUtils.isEmpty(clientCode) ? "OMNI-APP" : null);

        Signer signer = new MacSigner(secretKey);
        try {
            Jwt jwt = JwtHelper.encode(objectMapper.writeValueAsString(map), signer);
            return jwt.getEncoded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
