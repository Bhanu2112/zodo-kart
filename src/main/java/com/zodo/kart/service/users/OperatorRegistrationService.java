package com.zodo.kart.service.users;

import com.zodo.kart.dto.users.*;
import com.zodo.kart.entity.users.*;
import com.zodo.kart.mapper.UserMapper;
import com.zodo.kart.repository.users.operator.*;
import com.zodo.kart.service.inventory.InventoryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class OperatorRegistrationService {

    private final OperatorService operatorService;
    private final OperatorRepository operatorRepository;
    private final AdminRepository adminRepository;
    private final SubAdminRepository subAdminRepository;
    private final SellerRepository sellerRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final UserMapper userMapper;

    private final InventoryService inventoryService;



    // Admin registration

    public AuthResponseDto adminRegistration(AdminRegistrationDto adminRegistrationDto, HttpServletResponse response) {

        try{
            Optional<Admin> optionalAdmin = adminRepository.findByEmail(adminRegistrationDto.getEmail());
            if (optionalAdmin.isPresent()) {
                throw new Exception("Admin already exists");
            }
            Admin admin = userMapper.convertAdminRegistrationDtoToAdmin(adminRegistrationDto);
            log.info("[OperatorRegistrationService:registerAdmin] admin: {} ",admin);
            adminRepository.save(admin);
            // create inventory after registration of admin

            log.info("Inventory creation started for admin ");
            inventoryService.createInventory(admin.getAdminInventoryId());
            return operatorService.getJwtTokensForOperatorAfterRegistration(admin.getPersonalInfo(), response);
        }catch (Exception e){
            log.error("[OperatorRegistrationService:registerAdmin]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }


    public Admin adminDtoToAdmin(AdminRegistrationDto adminRegistrationDto) {
        return userMapper.convertAdminRegistrationDtoToAdmin(adminRegistrationDto);
    }






    // Sub Admin Registration

    public AuthResponseDto subAdminRegistration(SubAdminRegistrationDto subAdminRegistrationDto, HttpServletResponse response) {

        try{
            Optional<SubAdmin> optionalSubAdmin = subAdminRepository.findByEmail(subAdminRegistrationDto.getEmail());
            if (optionalSubAdmin.isPresent()) {
                throw new Exception("SubAdmin already exists");
            }
            SubAdmin subAdmin = userMapper.convertSubAdminRegistrationDtoToSubAdmin(subAdminRegistrationDto);
            subAdminRepository.save(subAdmin);

            return operatorService.getJwtTokensForOperatorAfterRegistration(subAdmin.getPersonalInfo(), response);
        }catch (Exception e){
            log.error("[OperatorRegistrationService:registerSubAdmin]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    // Seller Registration
    public AuthResponseDto sellerRegistration(SellerRegistrationDto sellerRegistrationDto, HttpServletResponse response) {

        try{
            Optional<Seller> optionalSeller = sellerRepository.findByEmail(sellerRegistrationDto.getEmail());
            if (optionalSeller.isPresent()) {
                throw new Exception("Seller already exists");
            }
            Seller seller = userMapper.convertSellerRegistrationDtoToSeller(sellerRegistrationDto);
            sellerRepository.save(seller);

            // create inventory after registration of seller

            inventoryService.createInventory(seller.getSellerInventoryId());


            return operatorService.getJwtTokensForOperatorAfterRegistration(seller.getPersonalInfo(), response);
        }catch (Exception e){
            log.error("[OperatorRegistrationService:registerSeller]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    // Delivery person registration

    public AuthResponseDto deliveryPersonRegistration(DeliveryPersonRegistrationDto deliveryPersonRegistrationDto, HttpServletResponse response) {

        try{
            Optional<DeliveryPerson> optionalDeliveryPerson = deliveryPersonRepository.findByEmail(deliveryPersonRegistrationDto.getEmail());
            if (optionalDeliveryPerson.isPresent()) {
                throw new Exception("Delivery Person already exists");
            }
            DeliveryPerson deliveryPerson = userMapper.deliveryPersonRegistrationDtoToDeliveryPerson(deliveryPersonRegistrationDto);
            deliveryPersonRepository.save(deliveryPerson);
            return operatorService.getJwtTokensForOperatorAfterRegistration(deliveryPerson.getPersonalDetails(), response);
        }catch (Exception e){
            log.error("[OperatorRegistrationService:registerDeliveryPerson]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }


    // get all operators

    public List<Operator> findAllOperators(){
        return operatorRepository.findAll();
    }

    // get all admins

    public List<Admin> getAllAdmins(){
        return adminRepository.findAdminsByRole("ROLE_ADMIN");
    }

    // get all sub admins

    public List<SubAdmin> getAllSubAdmins(){
        return subAdminRepository.findSubAdminsByRole("ROLE_SUB_ADMIN");
    }


    // get all sellers

    public List<Seller> getAllSellers(){
        return sellerRepository.findSellersByRole("ROLE_SELLER");
    }

    // get all delivery persons

    public List<DeliveryPerson> getAllDeliveryPersona(){
        return deliveryPersonRepository.findDeliveryPersonsByRole("ROLE_DELIVERY_PERSON");
    }
    


}
