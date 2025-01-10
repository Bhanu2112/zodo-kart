package com.zodo.kart.service.users;

import com.zodo.kart.dto.users.AuthResponseDto;
import com.zodo.kart.entity.users.RefreshToken;
import com.zodo.kart.entity.users.User;
import com.zodo.kart.enums.TokenType;
import com.zodo.kart.jwtconfig.JwtTokenGenerator;
import com.zodo.kart.mapper.UserMapper;
import com.zodo.kart.repository.users.RefreshTokenRepository;
import com.zodo.kart.repository.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2Service {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenGenerator jwtTokenGenerator;


    public AuthResponseDto customerOAuth2SignOn(Map<String, String> googleClaims , HttpServletResponse response){

        log.info("[OAuth2Service:customerOAuth2SignOn] started ");

        String email = googleClaims.get("email");
        Optional<User> googleUser = userRepository.findByEmail(email);
        User user = userMapper.convertGoogleUserToUser(googleClaims);
        if(googleUser.isPresent()){
            log.info("[OAuth2Service:customerOAuth2SignOn] for old user:: {} ",user);

            return getJwtTokensAfterGoogleSignInIfOldUser(googleUser.get(),response);
        }else{
            log.info("[OAuth2Service:customerOAuth2SignOn] for new user:: {} ",user);
            return getJwtTokensAfterGoogleSignInIfNewUser(user,response);

        }


    }

    private AuthResponseDto getJwtTokensAfterGoogleSignInIfNewUser(User user, HttpServletResponse response){
        Authentication authentication = createAuthenticationObject(user);

        System.out.println("--------");
        System.out.println(authentication.getAuthorities());
        System.out.println("-------------");
        // Generate a JWT token
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
        String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

        User savedUser = userRepository.save(user);

        saveUserRefreshToken(savedUser,refreshToken);
        createRefreshTokenCookie(response,refreshToken);

        log.info("[AuthService:registerUser] User:{} Successfully registered",savedUser.getName());
        return   AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(savedUser.getMobileNumber())
                .tokenType(TokenType.Bearer)
                .build();

    }
    private AuthResponseDto getJwtTokensAfterGoogleSignInIfOldUser(User user, HttpServletResponse response){
        Authentication authentication = createAuthenticationObject(user);
        System.out.println("-------- 87");
        System.out.println(authentication);
        System.out.println("------------- 89");

        // Generate a JWT token
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
        String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);



        saveUserRefreshToken(user,refreshToken);
        createRefreshTokenCookie(response,refreshToken);

        log.info("[AuthService:registerUser] User:{} Successfully logged in",user.getName());
        return   AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(user.getMobileNumber())
                .tokenType(TokenType.Bearer)
                .build();

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


    private static Authentication createAuthenticationObject(User user) {
        // Extract user details from UserDetailsEntity
        String username = user.getEmail();

        String roles = user.getRoles();

        System.out.println(roles);
        System.out.println("---------------------132");


        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        System.out.println(roleArray);
//        GrantedAuthority[] authorities = Arrays.stream(roleArray)
//                .map(role -> (GrantedAuthority) role::trim)
//                .toArray(GrantedAuthority[]::new);

        List<GrantedAuthority> authorities = Arrays.stream(roleArray)
                .map(role -> new SimpleGrantedAuthority(role.trim())) // Use SimpleGrantedAuthority for each role
                .collect(Collectors.toList());
        System.out.println("--------------------------- 140");


        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
