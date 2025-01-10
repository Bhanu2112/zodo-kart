package com.zodo.kart.controller;

import com.zodo.kart.dto.users.CustomerRegistrationDto;
import com.zodo.kart.service.users.RegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/register/sign-up")
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final RegistrationService registrationService;

    @GetMapping("/send-otp/{mobileNumber}")
    public ResponseEntity<?> sendOTP(@PathVariable String mobileNumber){
        return ResponseEntity.ok(registrationService.generateOTP(mobileNumber)) ;
    }

    @PostMapping("/verify-otp/customer")
    public ResponseEntity<?> registerCustomer(@RequestParam String mobileNumber, @RequestParam int OTP, @Valid @RequestBody CustomerRegistrationDto customerRegistrationDto,
                                              BindingResult bindingResult, HttpServletResponse httpServletResponse){

        log.info("[AuthController:registerUser]Signup Process Started for user:{}",customerRegistrationDto.getMobileNumber());
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser]Errors in user:{}",errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.ok(registrationService.verifyOTPAndRegisterCustomer(mobileNumber,OTP,customerRegistrationDto,httpServletResponse));
    }
}
