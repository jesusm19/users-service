package com.jmgr.usersapp.auth.filter;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.jmgr.usersapp.auth.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import static com.jmgr.usersapp.auth.TokenJwtConfig.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j 
public class JwtValidationFilter extends BasicAuthenticationFilter{

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("Entra JwtValidationFilter.doFilterInternal");
        String header = request.getHeader(AUTHORIZATION);
        if(header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request, response);
            logger.info("Sale JwtValidationFilter.doFilterInternal response = token inválido");
            return;
        }

        String token = header.replace(TOKEN_PREFIX, "");

        try {
            Claims claims = Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            
            String username = (String) claims.get("username");
            Object authotiriesClaims = claims.get("authorities");
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                new ObjectMapper()
                //.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                .readValue(authotiriesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null,authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.info("Sale JwtValidationFilter.doFilterInternal = Token válido");
            chain.doFilter(request, response);
            
        } catch (JwtException e) {
            logger.error("Error {}", e);
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "Token inválido");
            
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType(CONTENT_TYPE);
            logger.info("Sale JwtValidationFilter.doFilterInternal response = Token inválido");
        }

        
        
    }   


    
}
