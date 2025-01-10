package com.zodo.kart.controller.operator;

import com.zodo.kart.dto.users.ResponseDto;
import com.zodo.kart.entity.users.DeliveryPerson;
import com.zodo.kart.entity.users.Seller;
import com.zodo.kart.entity.users.SubAdmin;
import com.zodo.kart.entity.users.User;
import com.zodo.kart.service.users.CustomerService;
import com.zodo.kart.service.users.OperatorApprovalService;
import com.zodo.kart.service.users.OperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/operator/api/approval")
@RequiredArgsConstructor
public class OperatorApprovalController {



    private final OperatorApprovalService operatorApprovalService;
    private final CustomerService customerService;
    private final OperatorService operatorService;

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @PutMapping("/subadmin/status")
    public ResponseEntity<ResponseDto<SubAdmin>> updateSubAdminStatus(
            @RequestParam Long subAdminId,
            @RequestParam Long adminId,
            @RequestParam String status) {

        SubAdmin updatedSubAdmin = operatorApprovalService.updateSubAdminStatus(subAdminId, adminId, status);
        ResponseDto<SubAdmin> responseDto = new ResponseDto<>("SubAdmin status updated successfully", updatedSubAdmin);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_SUB_ADMIN')")
    @PutMapping("/seller/status")
    public ResponseEntity<ResponseDto<Seller>> updateSellerStatus(
            @RequestParam Long sellerId,
            @RequestParam Long adminId,
            @RequestParam String status) {

        Seller updatedSeller = operatorApprovalService.updateSellerStatus(sellerId, adminId, status);
        ResponseDto<Seller> responseDto = new ResponseDto<>("Seller status updated successfully", updatedSeller);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_SELLER','SCOPE_ADMIN','SCOPE_SUB_ADMIN')")
    @PutMapping("/deliveryperson/status")
    public ResponseEntity<ResponseDto<DeliveryPerson>> updateDeliveryPersonStatus(
            @RequestParam Long deliveryPersonId,
            @RequestParam Long sellerId,
            @RequestParam String status) {


        ResponseDto<DeliveryPerson> responseDto = new ResponseDto<DeliveryPerson>("Delivery Person status updated successfully",  operatorApprovalService.updateDeliveryPersonStatus(deliveryPersonId, sellerId, status));
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/group/{adminId}/sub-admins/status")
    public ResponseEntity<Map<String, List<SubAdmin>>> getSubAdminsByStatus(@PathVariable Long adminId) {
        Map<String, List<SubAdmin>> subAdminsByStatus = operatorService.groupSubAdminsByStatus(adminId);
        return ResponseEntity.ok(subAdminsByStatus);
    }

    // Endpoint to get Sellers grouped by their status
    @GetMapping("/group/{adminId}/sellers/status")
    public ResponseEntity<Map<String, List<Seller>>> getSellersByStatus(@PathVariable Long adminId) {
        Map<String, List<Seller>> sellersByStatus = operatorService.groupSellersByStatus(adminId);
        return ResponseEntity.ok(sellersByStatus);
    }

    // Endpoint to get Delivery Persons grouped by their status
    @GetMapping("/group/sellers/{sellerId}/delivery-persons/status")
    public ResponseEntity<Map<String, List<DeliveryPerson>>> getDeliveryPersonsByStatus(@PathVariable Long sellerId) {
        Map<String, List<DeliveryPerson>> deliveryPersonsByStatus = operatorService.groupDeliveryPersonsByStatus(sellerId);
        return ResponseEntity.ok(deliveryPersonsByStatus);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_SUB_ADMIN')")
    @GetMapping("/user/status")
    public ResponseEntity<ResponseDto<User>> updateUserStatus(@RequestParam Long userId, @RequestParam String status) {
        User user = operatorApprovalService.updateCustomerStatus(userId, status);
        ResponseDto<User> responseDto = new ResponseDto<>("User status updated successfully", user);
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/customer/all")
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }


    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getSellerById(@PathVariable Long sellerId) {
        return ResponseEntity.ok(operatorService.getSellerById(sellerId));
    }

    // get delivery person by id

    @GetMapping("/deliveryperson/{deliveryPersonId}")
    public ResponseEntity<?> getDeliveryPersonById (@PathVariable Long deliveryPersonId) {
        return ResponseEntity.ok(operatorService.getDeliveryPersonById(deliveryPersonId));
    }

    // get sub admin by id
    @GetMapping("/subadmin/{subAdminId}")
    public ResponseEntity<?> getSubAdminById(@PathVariable Long subAdminId) {
        return ResponseEntity.ok(operatorService.getSubAdminById(subAdminId));
    }

    // get seller by region
    @GetMapping("/seller/region/{region}")
    public ResponseEntity<?> getSellerByRegion(@PathVariable String region) {
        return ResponseEntity.ok(operatorService.getSellerByRegion(region));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/msg")
    public String getMessage(){
        return "Bhanu";
    }


}