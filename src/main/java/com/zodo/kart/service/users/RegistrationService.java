package com.zodo.kart.service.users;

import com.zodo.kart.dto.users.AuthResponseDto;
import com.zodo.kart.dto.users.CustomerRegistrationDto;
import com.zodo.kart.entity.users.RefreshToken;
import com.zodo.kart.entity.users.User;
import com.zodo.kart.enums.TokenType;
import com.zodo.kart.exceptions.OtpValidationException;
import com.zodo.kart.jwtconfig.JwtTokenGenerator;
import com.zodo.kart.mapper.UserMapper;
import com.zodo.kart.repository.users.RefreshTokenRepository;
import com.zodo.kart.repository.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private OtpService otpService;


    public String generateOTP(String mobileNumber){

        // need to modify after setting up sms service


        return otpService.sendOtp(mobileNumber);

    }

    @SneakyThrows
    public AuthResponseDto verifyOTPAndRegisterCustomer(String mobileNumber, int OTP, CustomerRegistrationDto customerRegistrationDto, HttpServletResponse response){
        boolean verified = otpService.verifyOtp(mobileNumber,OTP);
        if(verified){
            return registerCustomer(customerRegistrationDto,response);
        }else{
            throw new OtpValidationException("Invalid OTP. Please enter the correct OTP.");
        }

    }

    public AuthResponseDto registerCustomer(CustomerRegistrationDto customerRegistrationDto, HttpServletResponse response) {

        try {
            log.info("[AuthService:registerUser]User Registration Started with :::{}", customerRegistrationDto);
            Optional<User> user = userRepository.findByMobileNumber(customerRegistrationDto.getMobileNumber());
            if (user.isPresent()) {
                throw new Exception("User Already Exits");
            }

            User newUser = userMapper.convertCustomerDtoToUser(customerRegistrationDto);
            return getJwtTokensAfterRegistration(newUser,response);

        }catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    private AuthResponseDto getJwtTokensAfterRegistration(User user, HttpServletResponse response){
        Authentication authentication = createAuthenticationObject(user);


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
        String username = user.getMobileNumber();

        String roles = user.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        System.out.println(roleArray);
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);
        System.out.println("---------------------------");
        System.out.println(authorities);

        return new UsernamePasswordAuthenticationToken(username, null, Arrays.asList(authorities));
    }


}
