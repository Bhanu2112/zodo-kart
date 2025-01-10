package com.zodo.kart.jwtconfig;

import com.zodo.kart.config.OperatorConfig;
import com.zodo.kart.config.UserConfig;
import com.zodo.kart.repository.users.UserRepository;
import com.zodo.kart.repository.users.operator.OperatorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

/**
 * Author : Bhanu prasad
 */

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private final UserRepository userRepo;
    private final OperatorRepository operatorRepository;

    public String getUserName(Jwt jwtToken){
        return jwtToken.getSubject();
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails){
        final String userName = getUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired  && isTokenUserSameAsDatabase;

    }

    private boolean getIfTokenIsExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }


    public UserDetails userDetails(String mobileNumber){
        return userRepo
                .findByMobileNumber(mobileNumber)
                .map(UserConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+mobileNumber+" does not exist"));
    }


    public UserDetails userDetailsByEmail(String email){
        return operatorRepository
                .findByEmail(email)
                .map(OperatorConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+email+" does not exist"));
    }
}
