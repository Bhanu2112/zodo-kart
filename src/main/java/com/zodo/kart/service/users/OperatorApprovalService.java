package com.zodo.kart.service.users;

import com.zodo.kart.entity.users.DeliveryPerson;
import com.zodo.kart.entity.users.Seller;
import com.zodo.kart.entity.users.SubAdmin;
import com.zodo.kart.entity.users.User;
import com.zodo.kart.enums.DeliveryPersonStatus;
import com.zodo.kart.enums.SellerStatus;
import com.zodo.kart.enums.SubAdminStatus;
import com.zodo.kart.enums.UserStatus;
import com.zodo.kart.repository.users.UserRepository;
import com.zodo.kart.repository.users.operator.AdminRepository;
import com.zodo.kart.repository.users.operator.DeliveryPersonRepository;
import com.zodo.kart.repository.users.operator.SellerRepository;
import com.zodo.kart.repository.users.operator.SubAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class OperatorApprovalService {
    private final AdminRepository adminRepository;
    private final SubAdminRepository subAdminRepository;
    private final SellerRepository sellerRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final UserRepository userRepository;

    public SubAdmin updateSubAdminStatus(Long subAdminId, Long adminId, String status) {

        // Checking sub admin exits under given admin. if present will proceed further

        log.info("[OperatorApprovalService:updateSubAdminStatus] subAdminId: {}, adminId: {}, status: {}",subAdminId, adminId, status);
        log.info("[OperatorApprovalService:updateSubAdminStatus] Checking sub admin exits under given admin. if present will proceed further");
        boolean subAdminExists = adminRepository.findById(adminId)
                .map(admin -> admin.getSubAdmins().stream()
                        .anyMatch(subAdmin -> (subAdmin.getId() == (subAdminId)))).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"SubAdmin not found"));


        SubAdmin subAdmin = subAdminRepository.findById(subAdminId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"SubAdmin not found"));
        subAdmin.getPersonalInfo().setStatus(SubAdminStatus.valueOf(status).name());

        return subAdminRepository.save(subAdmin);
    }


    // seller status update
    public Seller updateSellerStatus(Long sellerId, Long adminId, String status) {

        log.info("[OperatorApprovalService:updateSellerStatus] sellerId: {}, adminId: {}, status: {}",sellerId, adminId, status);
        log.info("[OperatorApprovalService:updateSellerStatus] Checking seller exits under given admin. if present will proceed further");
        boolean sellerExists = adminRepository.findById(adminId)
                .map(admin -> admin.getSellers().stream()
                        .anyMatch(seller -> (seller.getId() == (sellerId)))).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Seller not found"));

        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Seller not found"));
        seller.getPersonalInfo().setStatus(SellerStatus.valueOf(status).name());

        return sellerRepository.save(seller);
    }


    // update delivery person status
    public DeliveryPerson updateDeliveryPersonStatus(Long deliveryPersonId, Long sellerId , String status) {
        log.info("[OperatorApprovalService:updateDeliveryPersonStatus] deliveryPersonId: {}, status: {}",deliveryPersonId, status);
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(deliveryPersonId).orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,"Delivery Person not found"));

        log.info("[OperatorApprovalService:updateDeliveryPersonStatus] Checking delivery person exits under given seller. if present will proceed further");

        boolean deliveryPersonExists = sellerRepository.findById(sellerId)
                .map(seller -> seller.getDeliveryPersons().stream()
                        .anyMatch(d -> (d.getId() == (deliveryPersonId)))).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Delivery Person not found"));

        deliveryPerson.getPersonalDetails().setStatus(DeliveryPersonStatus.valueOf(status).name());
        return  deliveryPersonRepository.save(deliveryPerson);

    }


    // change status of customer if admin wants to change status

    public User updateCustomerStatus(Long customerId, String status) {

        User user = userRepository.findById(customerId).orElseThrow(()-> new UsernameNotFoundException("User with "+customerId+" does not exist."));
        user.setStatus(UserStatus.valueOf(status).name());
        userRepository.save(user);
        return user;
    }






}
