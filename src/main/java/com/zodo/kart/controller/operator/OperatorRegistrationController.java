package com.zodo.kart.controller.operator;

import com.zodo.kart.dto.users.*;
import com.zodo.kart.entity.users.*;
import com.zodo.kart.service.users.CustomerService;
import com.zodo.kart.service.users.OperatorRegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author : Bhanu prasad
 */
@RestController
@RequestMapping("/operator/registration")
@RequiredArgsConstructor
public class OperatorRegistrationController {

    private final OperatorRegistrationService operatorRegistrationService;
    private final CustomerService customerService;


    /**
     * @param adminRegistrationDto
     * @param response
     * @return
     */
    @PostMapping("/admin")
    public ResponseEntity<ResponseDto<AuthResponseDto>> registerAdmin(@RequestBody AdminRegistrationDto adminRegistrationDto, HttpServletResponse response) {
        ResponseDto<AuthResponseDto> responseDto =new ResponseDto<>("Admin registered successfully", operatorRegistrationService.adminRegistration(adminRegistrationDto, response));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/con")
    public Admin convertToAdmin(@RequestBody AdminRegistrationDto adminRegistrationDto) {
        return operatorRegistrationService.adminDtoToAdmin(adminRegistrationDto);
    }


    // Sub admin registration

    /**
     * @param subAdminRegistrationDto
     * @param response
     * @return
     */
    @PostMapping("/subadmin")
    public ResponseEntity<ResponseDto<AuthResponseDto>> registerSubAdmin(@RequestBody SubAdminRegistrationDto subAdminRegistrationDto, HttpServletResponse response) {
        ResponseDto<AuthResponseDto> responseDto =new ResponseDto<>("SubAdmin registered successfully", operatorRegistrationService.subAdminRegistration(subAdminRegistrationDto, response));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    // seller registration

    /**
     * @param sellerRegistrationDto
     * @param response
     * @return
     */
    @PostMapping("/seller")
    public ResponseEntity<ResponseDto<AuthResponseDto>> registerSeller(@RequestBody SellerRegistrationDto sellerRegistrationDto, HttpServletResponse response) {
        ResponseDto<AuthResponseDto> responseDto =new ResponseDto<>("Seller registered successfully", operatorRegistrationService.sellerRegistration(sellerRegistrationDto, response));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delivery person registration

    /**
     * @param deliveryPersonRegistrationDto
     * @param response
     * @return
     */
    @PostMapping("/deliveryperson")
    public ResponseEntity<ResponseDto<AuthResponseDto>> registerDeliveryPerson(@RequestBody DeliveryPersonRegistrationDto deliveryPersonRegistrationDto, HttpServletResponse response) {
            ResponseDto<AuthResponseDto> responseDto =new ResponseDto<>("Delivery Person registered successfully", operatorRegistrationService.deliveryPersonRegistration(deliveryPersonRegistrationDto, response));
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/all-operators")
    public List<Operator> findAllOperators(){
        return operatorRegistrationService.findAllOperators();
    }

    // get all admins

    @GetMapping("/all-admins")
    public List<Admin> getAllAdmis(){
        return operatorRegistrationService.getAllAdmins();
    }

    // get all sub admins

    @GetMapping("/all-sub-admins")
    public List<SubAdmin> getAllSubAdmins(){
        return operatorRegistrationService.getAllSubAdmins();
    }

    // get all sellers

    @GetMapping("/all-sellers")
    public List<Seller> getAllSellers(){
        return operatorRegistrationService.getAllSellers();
    }

    // get all delivery persons

    @GetMapping("/all-delivery-persons")
    public List<DeliveryPerson> getAllDeliveryPersona(){
        return operatorRegistrationService.getAllDeliveryPersona();
    }

    @GetMapping("/customer/all")
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
}
