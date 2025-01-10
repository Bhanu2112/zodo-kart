package com.zodo.kart.controller.operator;

import com.zodo.kart.dto.users.ResponseDto;
import com.zodo.kart.entity.users.Address;
import com.zodo.kart.entity.users.Admin;
import com.zodo.kart.service.users.OperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Bhanu prasad
 */
@RestController
@RequestMapping("/operator/api")
@RequiredArgsConstructor
public class OperatorProfiles {

    private final OperatorService operatorService;

    // operator profiles

    // admin

    @GetMapping("/profiles/admin/profile")
    public ResponseEntity<Admin> getAdminProfile(@RequestParam String email){
        return ResponseEntity.ok(operatorService.getAdminByEmail(email));
    }

    // sub admin

    @GetMapping("/profiles/subadmin/profile")
    public ResponseEntity<?> getSubAdminProfile(@RequestParam String email){
        return ResponseEntity.ok(operatorService.getSubAdminByEmail(email));
    }

    // seller

    @GetMapping("/profiles/seller/profile")
    public ResponseEntity<?> getSellerProfile(@RequestParam String email){
        return ResponseEntity.ok(operatorService.getSellerByEmail(email));
    }

    // delivery person

    @GetMapping("/profiles/deliveryperson/profile")
    public ResponseEntity<?> getDeliveryPersonProfile(@RequestParam String email){
        return ResponseEntity.ok(operatorService.getDeliveryPersonByEmail(email));
    }

    // add address

    @PostMapping("/add/address")
    public ResponseEntity<ResponseDto<Address>> addAddress(@RequestBody Address address, @RequestParam Long operatorId){
        ResponseDto<Address> responseDto = new ResponseDto<>("Address added successfully",operatorService.addAddress(address,operatorId));
        return ResponseEntity.ok(responseDto);
    }

    // update address

    @PutMapping("/update/address")
    public ResponseEntity<ResponseDto<Address>> updateAddress(@RequestBody Address address){
        ResponseDto<Address> responseDto = new ResponseDto<>("Address updated successfully",operatorService.updateAddress(address));
        return ResponseEntity.ok(responseDto);
    }

    // make default address

    @PutMapping("/make/default/address")
    public ResponseEntity<ResponseDto<Address>> makeDefaultAddress(@RequestParam Long addressId, @RequestParam Long operatorId){
        ResponseDto<Address> responseDto = new ResponseDto<>("Address updated successfully",operatorService.makeDefaultAddress(addressId,operatorId));
        return ResponseEntity.ok(responseDto);
    }

    // delete address

    @DeleteMapping("/delete/address")
    public ResponseEntity<String> deleteAddress(@RequestParam Long addressId){
        return ResponseEntity.ok(operatorService.deleteAddress(addressId));
    }


}
