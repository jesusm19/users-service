package com.jmgr.usersapp.auth;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenJwtConfig {

    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public static final String AUTHORIZATION = "Authorization";
    
    public static final long EXPIRATION_TIME = 3600000; 

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String CONTENT_TYPE = "application/json";

}
