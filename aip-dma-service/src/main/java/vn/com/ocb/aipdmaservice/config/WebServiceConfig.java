//package vn.com.ocb.aipdmaservice.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import vn.com.ocb.aipdmaservice.esb.ws.SoapLoggingAndMaskingHandler;
//import vn.com.ocb.aipdmaservice.esb.ws.WsFactoryBean;
//
//import javax.annotation.PostConstruct;
//
//@Configuration
//public class WebServiceConfig {
//
//    @Value("${xslt.mask.file}")
//    private String maskFile;
//
//    @Value("${xslt.mask.params.key}")
//    private String maskKey;
//
//    @Value("${xslt.mask.params.value}")
//    private String maskValue;
//
//    @Bean
//    public WsFactoryBean party() {
//        WsFactoryBean wsFactoryBean = new WsFactoryBean();
//        wsFactoryBean.setServiceClass(vn.com.bian.partyauthentication.PartyAuthentication_Service.class);
//        wsFactoryBean.setPort(vn.com.bian.partyauthentication.PartyAuthentication.class);
//        wsFactoryBean.setEndpointUrl("http://10.96.24.101:80/partyauthentication/v1/ws");
//        return wsFactoryBean;
//    }
//
//    @PostConstruct
//    public SoapLoggingAndMaskingHandler soapLoggingAndMaskingHandler(){
//        SoapLoggingAndMaskingHandler soapLoggingAndMaskingHandler = new SoapLoggingAndMaskingHandler();
//        soapLoggingAndMaskingHandler.setMaskFile(maskFile);
//        soapLoggingAndMaskingHandler.setMaskKey(maskKey);
//        soapLoggingAndMaskingHandler.setMaskValue(maskValue);
//        return soapLoggingAndMaskingHandler;
//    }
//
//}
