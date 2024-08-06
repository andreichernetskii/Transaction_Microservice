package com.transaction_microservice.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    public static final Logger logger = LoggerFactory.getLogger( JwtUtils.class );
    private final PublicKeyVault publicKeyVault;
    private final String jwtCookie = "token";

    public boolean validateJwtToken( String token ) {
        try {
            // build the object of JWTParser with parameter publicKey and compare with a token
            Jwts
                    .parser()
                    .verifyWith( publicKeyVault.getPublicKey() )
                    .build()
                    .parse( token );

            return true;
        } catch ( MalformedJwtException e ) {
            logger.error( "Invalid JWT token: {}", e.getMessage() );
        } catch ( ExpiredJwtException e ) {
            logger.error( "JWT token is expired: {}", e.getMessage() );
        } catch ( UnsupportedJwtException e ) {
            logger.error( "JWT token is unsupported: {}", e.getMessage() );
        } catch ( IllegalArgumentException e ) {
            logger.error( "JWT claims string is empty: {}", e.getMessage() );
        }

        return false;
    }

    public String parseJwt( HttpServletRequest request ) {
        return getJwtFromCookies( request );
    }

    public String getJwtFromCookies( HttpServletRequest request ) {
        Cookie cookie = WebUtils.getCookie( request, jwtCookie );
        return cookie != null ? cookie.getValue() : null;
    }

    public String getUserNameFromJwtToken( String token ) {
        return Jwts
                .parser()
                .verifyWith( publicKeyVault.getPublicKey() )
                .build()
                .parseSignedClaims( token )
                .getPayload()
                .getSubject();
    }

    public List<GrantedAuthority> getAuthoritiesFromJwtToken( String token ) {
        Claims claims = Jwts
                .parser()
                .verifyWith( publicKeyVault.getPublicKey() )
                .build()
                .parseSignedClaims( token )
                .getPayload();

        List<String> roles = claims.get( "role", List.class );
        return roles.stream()
                .map( SimpleGrantedAuthority::new )
                .collect( Collectors.toList() );
    }

}
