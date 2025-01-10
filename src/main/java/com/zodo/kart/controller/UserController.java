package com.zodo.kart.controller;

import com.zodo.kart.config.RSAKeyRecord;
import com.zodo.kart.dto.users.CustomerDto;
import com.zodo.kart.dto.users.ResponseDto;
import com.zodo.kart.entity.users.Address;
import com.zodo.kart.entity.users.User;
import com.zodo.kart.repository.users.UserRepository;
import com.zodo.kart.service.users.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public")
public class UserController {
    @Autowired
    private RSAKeyRecord rsaKeyRecord;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/h2-console")
    public List<User> getAll(){
        return userRepository.findAll();
    }


    @GetMapping("/user-info")
    public String getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION)  String token1) {
        final String token = token1.substring(7);
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

        final Jwt jwt = jwtDecoder.decode(token);

        // Extract claims from the JWT token
        String userId = jwt.getClaim("sub");    // User ID (subject)
        String email = jwt.getClaim("mobileNumber");   // Email if present as a claim
        String roles = jwt.getClaim("scope");   // Extract any custom claim like roles

        return getUser(jwt);
    }

    public String getUser(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaim("sub");
    }

    @GetMapping("/user-profile/{mobileNumber}")
    public ResponseEntity<CustomerDto> userProfile(@PathVariable String mobileNumber){
        return ResponseEntity.ok(customerService.getCustomerProfile(mobileNumber));
    }

    @PostMapping("/update/user-profile")
    public ResponseEntity<ResponseDto<CustomerDto>> updateCustomerProfile(@RequestBody CustomerDto customerDto){
        ResponseDto<CustomerDto> responseDto = new ResponseDto<>("Profile updated successfully",customerService.updateCustomerProfile(customerDto));
        return ResponseEntity.ok(responseDto);
    }


    // add address

    @PostMapping("/add/address")
    public ResponseEntity<ResponseDto<Address>> addAddress(@RequestBody Address address, @RequestParam Long userId){
        ResponseDto<Address> responseDto = new ResponseDto<>("Address added successfully",customerService.addAddress(address,userId));
        return ResponseEntity.ok(responseDto);
    }

    // update address

    @PutMapping("/update/address")
    public ResponseEntity<ResponseDto<Address>> updateAddress(@RequestBody Address address){
        ResponseDto<Address> responseDto = new ResponseDto<>("Address updated successfully",customerService.updateAddress(address));
        return ResponseEntity.ok(responseDto);
    }

    // make default address

    @PutMapping("/make/default/address")
    public ResponseEntity<ResponseDto<Address>> makeDefaultAddress(@RequestParam Long addressId, @RequestParam Long userId){
        ResponseDto<Address> responseDto = new ResponseDto<>("Address updated successfully",customerService.makeDefaultAddress(addressId,userId));
        return ResponseEntity.ok(responseDto);
    }

    // delete address

    @DeleteMapping("/delete/address")
    public ResponseEntity<String> deleteAddress(@RequestParam Long addressId){
        return ResponseEntity.ok(customerService.deleteAddress(addressId));
    }

}
