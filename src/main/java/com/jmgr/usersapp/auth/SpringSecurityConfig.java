package com.jmgr.usersapp.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration 
public class SpringSecurityConfig {

    private static final String BASE_PATH = "/api/users/";

    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http){
        return http.authorizeHttpRequests(authz -> 
            authz
                .requestMatchers(HttpMethod.GET, BASE_PATH + "findAll", BASE_PATH +"findAllPaged/{page}").permitAll()
                .requestMatchers(HttpMethod.GET, BASE_PATH + "findAll", BASE_PATH +"findById/{id}").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, BASE_PATH + "save").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, BASE_PATH + "update/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, BASE_PATH + "delete/{id}").hasRole("ADMIN")
                .anyRequest().authenticated()
        )
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();


    }

}
