package org.telemedicine.server.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.telemedicine.server.enums.Role;

import javax.crypto.spec.SecretKeySpec;

//config trong https://docs.spring.io/spring-security/reference/servlet/architecture.html
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    //ngoài những endpoint public bất cứ ai cũng có thể truy cập còn lại phải cung cấp token
    private final String[] PUBLIC_ENDPOINTS = {
            "auth/signup",
            "auth/signinUser",
            "auth/signinStaff",
            "auth/introspect",
            "auth/logout",
            "auth/refreshToken",
    } ;

    CustomJwtDecoder customJwtDecoder;

    //cấu hình spring security những endpoint nào cần bảo vệ và k cần
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //cấu hình những đường dẫn guest có thể đi vào
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
//                            .requestMatchers(HttpMethod.GET, "/users")
//                            .hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 ->
                //kiểm tra jwt của hệ thống
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        //chống tấn công nhưng trường hợp này có thể tắt csrf
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return converter;
    }

//
//
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
