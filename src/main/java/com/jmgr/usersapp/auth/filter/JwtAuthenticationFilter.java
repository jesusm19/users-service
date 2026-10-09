package com.jmgr.usersapp.auth.filter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jmgr.usersapp.models.UserDTO;
import com.jmgr.usersapp.models.entity.User;
import com.jmgr.usersapp.models.mapper.IUserMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import static com.jmgr.usersapp.auth.TokenJwtConfig.*;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;
    
    @Autowired 
    private IUserMapper userMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        logger.info("Entra JwtAuthenticationFilter.attemptAuthentication");
        User user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            //user = userMapper.toUser(userDto);
        } catch (JacksonException e) {
            logger.error("Error al convertir objeto {}", e);
        } catch (IOException e) {
            logger.error("Error al convertir objeto {}", e);
        }

        if (user == null) {
            logger.error("El usuario recibido es nulo");
            throw new AuthenticationServiceException("No se pudo procesar la autenticación");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        logger.info("Sale JwtAuthenticationFilter.attemptAuthentication");
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        logger.info("Entra JwtAuthenticationFilter.successfulAuthentication");
        
        org.springframework.security.core.userdetails.User userPrincipal = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String username = userPrincipal.getUsername();
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        Claims claims = Jwts
            .claims()
            .add("authorities", new ObjectMapper().writeValueAsString(authorities))
            .build();

        String jwt = Jwts.builder()
            .subject(username)
            .claims(claims)
            .signWith(SECRET_KEY)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .compact();
        

        logger.info("Sale JwtAuthenticationFilter.successfulAuthentication");
        response.addHeader(AUTHORIZATION, TOKEN_PREFIX + jwt);

        Map<String, String> body = new HashMap<>();
        body.put("token", jwt);
        body.put("username", username);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        // TODO Auto-generated method stub
        super.unsuccessfulAuthentication(request, response, failed);
    }

    

}
