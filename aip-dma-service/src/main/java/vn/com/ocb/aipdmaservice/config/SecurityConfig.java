package vn.com.ocb.aipdmaservice.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import vn.com.ocb.aipdmaservice.helper.jwtToken;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] antMatcher = {
            "/**",
            "/v3/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/*/login",
            "/*/login_with_token",
            "/decrypt",
            "/encrypt"
    };
    @Autowired
    jwtToken jwtTokenUtil;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(antMatcher).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
//        httpSecurity.addFilterBefore(new JwtRequestFilter(jwtTokenUtil), BasicAuthenticationFilter.class);
    }
}
