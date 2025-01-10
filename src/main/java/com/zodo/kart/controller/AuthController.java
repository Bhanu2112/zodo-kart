package com.zodo.kart.controller;

import com.zodo.kart.service.users.AuthService;
import com.zodo.kart.service.users.OtpService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {


    private final OtpService otpService;
    private final AuthService authService;

    @GetMapping("/send-otp/{mobileNUmber}")
    public ResponseEntity<?> requestOTP(@PathVariable String mobileNUmber){
        return ResponseEntity.ok(otpService.sendOtp(mobileNUmber));
    }


    @GetMapping("/verify-otp/{mobileNumber}/{code}")
    public ResponseEntity<?> verifyOTP(@PathVariable String mobileNumber, @PathVariable int code){
        return ResponseEntity.ok(otpService.verifyOtp(mobileNumber,code));
    }


    @GetMapping("/customer/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestParam String mobileNumber, @RequestParam int OTP, HttpServletResponse response){
        // Authentication authentication = authService.getAuthentication(mobileNumber);
        System.out.println("gooing to create token");
        return ResponseEntity.ok(authService.authenticateUser(mobileNumber,OTP,response));
        // return ResponseEntity.ok(authentication);

    }


    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }


}