package com.zodo.kart.service.users;

import com.zodo.kart.config.UserManagerConfig;
import com.zodo.kart.dto.users.AuthResponseDto;
import com.zodo.kart.entity.users.RefreshToken;
import com.zodo.kart.entity.users.User;
import com.zodo.kart.enums.TokenType;
import com.zodo.kart.jwtconfig.JwtTokenGenerator;
import com.zodo.kart.repository.users.RefreshTokenRepository;
import com.zodo.kart.repository.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Author : Bhanu prasad
 */


@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserManagerConfig userManagerConfig;

    @Autowired
    private OtpService otpService;
    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    public Authentication getAuthentication(String mobileNumber) {

        UserDetails userDetails = userManagerConfig.loadUserByUsername(mobileNumber);
        System.out.println(userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }


    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {
            var user = userRepository.findByMobileNumber(authentication.getName())
                    .orElseThrow(() -> {
                        log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                    });


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
            saveUserRefreshToken(user, refreshToken);

            createRefreshTokenCookie(response, refreshToken);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated", user.getMobileNumber());
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(user.getMobileNumber())
                    .tokenType(TokenType.Bearer)
                    .build();


        } catch (Exception e) {
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    private void saveUserRefreshToken(User user, String refreshToken) {
        var refreshTokenBuilder = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(user)
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenBuilder);

    }


    private Cookie createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }


    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
        if (!authorizationHeader.startsWith(TokenType.Bearer.name())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);
        //Find refreshToken from database and should not be revoked

        var refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken).
                filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        User user = refreshTokenEntity.getUser();
        Authentication authentication = getAuthentication(user.getMobileNumber());
        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
        log.info("[AuthService:userRefreshTokenAuth] Access token for user:{}, has been generated by refresh token", user.getMobileNumber());
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(user.getMobileNumber())
                .tokenType(TokenType.Bearer)
                .build();
    }


    public AuthResponseDto authenticateUser(String mobileNumber, int otp, HttpServletResponse response){
        boolean verified = otpService.verifyOtp(mobileNumber,otp);
        if(verified){
            Authentication authentication = getAuthentication(mobileNumber);
            return getJwtTokensAfterAuthentication(authentication,response);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"OTP not verified");
        }
    }


}


