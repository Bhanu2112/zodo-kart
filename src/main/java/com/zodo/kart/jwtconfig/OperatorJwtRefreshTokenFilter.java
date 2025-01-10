package com.zodo.kart.jwtconfig;

import com.zodo.kart.config.RSAKeyRecord;
import com.zodo.kart.repository.users.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * Author : Bhanu prasad
 */

@Slf4j
@RequiredArgsConstructor
public class OperatorJwtRefreshTokenFilter extends OncePerRequestFilter {

    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            log.info("[OperatorJwtRefreshTokenFilter:doFilterInternal] Refresh Token Filter Started");
            log.info("[OperatorJwtRefreshTokenFilter:doFilterInternal] Request URL:{}", request.getRequestURL());

            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }

            final String token = authHeader.substring(7);
            final Jwt jwtRefreshToken = jwtDecoder.decode(token);
             final String userName = jwtTokenUtils.getUserName(jwtRefreshToken);

             if(!userName.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null){
                 var isRefreshTokenPresent = refreshTokenRepository.findByRefreshToken(jwtRefreshToken.getTokenValue())
                         .map(refreshToken -> !refreshToken.isRevoked())
                         .orElse(false);

                 UserDetails userDetails = jwtTokenUtils.userDetailsByEmail(userName);
                 if (jwtTokenUtils.isTokenValid(jwtRefreshToken, userDetails) && isRefreshTokenPresent) {
                     SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                     UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                             userDetails,
                             null,
                             userDetails.getAuthorities()
                     );

                     createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                     securityContext.setAuthentication(createdToken);
                     SecurityContextHolder.setContext(securityContext);
                 }


             }
            log.info("[OperatorJwtRefreshTokenFilter:doFilterInternal] Completed");
            filterChain.doFilter(request, response);



        }catch (JwtValidationException jwtValidationException){
            log.error("[OperatorJwtRefreshTokenFilter:doFilterInternal] Exception due to :{}", jwtValidationException.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,jwtValidationException.getMessage());
        }
    }
}
