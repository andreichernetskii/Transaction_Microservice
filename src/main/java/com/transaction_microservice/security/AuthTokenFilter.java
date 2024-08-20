package com.transaction_microservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private static final Logger LOGGER = LoggerFactory.getLogger( AuthTokenFilter.class );

    @Override
    protected void doFilterInternal( HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain ) throws ServletException, IOException {

        try {
            String jwtToken = jwtUtils.parseJwt( request );

            if ( jwtToken != null && jwtUtils.validateJwtToken( jwtToken ) ) {
                UsernamePasswordAuthenticationToken authentication = createAuthenticationFromUserToken( jwtToken );

                authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

                SecurityContextHolder.getContext().setAuthentication( authentication );
            }
        } catch ( Exception e ) {
            LOGGER.error( "Cannot set user authentication: {}", e.getMessage() );
        }

        filterChain.doFilter( request, response );
    }

    private UsernamePasswordAuthenticationToken createAuthenticationFromUserToken( String jwtToken ) {
        String userId = jwtUtils.getUserIdFromJwtToken( jwtToken );

        UserDetails userDetails = new User( userId, "", jwtUtils.getAuthoritiesFromJwtToken( jwtToken ) );

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}

