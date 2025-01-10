package com.zodo.kart.controller.operator;

import com.zodo.kart.dto.users.AuthResponseDto;
import com.zodo.kart.dto.users.OperatorLoginDto;
import com.zodo.kart.dto.users.ResponseDto;
import com.zodo.kart.entity.users.Address;
import com.zodo.kart.entity.users.Admin;
import com.zodo.kart.entity.users.Operator;
import com.zodo.kart.service.users.OperatorService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/operator/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class OperatorController {

    private final OperatorService operatorService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateOperator(
          @RequestBody OperatorLoginDto loginDto,
            HttpServletResponse response){

        Operator operator = new Operator();
        operator.setEmail(loginDto.getEmail());
        operator.setPassword(loginDto.getPassword());

        Authentication authentication = operatorService.getAuthentication(operator);


        return ResponseEntity.ok( operatorService.getJwtTokensForOperatorAfterAuthentication(authentication,response));
    }

   @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(operatorService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }








}
