package com.jmgr.usersapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.core.Ordered;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.authentication.AuthenticationManager;

import com.jmgr.usersapp.auth.filter.JwtAuthenticationFilter;
import com.jmgr.usersapp.auth.filter.JwtValidationFilter;

import lombok.extern.slf4j.Slf4j;


@Configuration 
@Slf4j 
public class SpringSecurityConfig {

    private static final String BASE_PATH = "/api/users/";
    @Autowired 
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean 
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Entra SpringSecurityConfig.filterChain");

        return http.authorizeHttpRequests(authz -> 
            authz
                .requestMatchers(HttpMethod.GET, BASE_PATH + "findAll", BASE_PATH +"findAllPaged/{page}").permitAll()
                .requestMatchers(HttpMethod.GET, BASE_PATH + "findAll", BASE_PATH +"findById/{id}").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, BASE_PATH + "save").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, BASE_PATH + "update/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, BASE_PATH + "delete/{id}").hasRole("ADMIN")
                .anyRequest().authenticated())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();


    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(java.util.List.of("*"));
        config.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
         
        return source;
    }

    @Bean 
    FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
