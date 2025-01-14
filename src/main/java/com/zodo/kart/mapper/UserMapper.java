package com.zodo.kart.mapper;

import com.zodo.kart.dto.users.*;
import com.zodo.kart.entity.users.*;
import com.zodo.kart.enums.DeliveryPersonStatus;
import com.zodo.kart.enums.SellerStatus;
import com.zodo.kart.enums.SubAdminStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Author : Bhanu prasad
 */


@Component
@RequiredArgsConstructor
public class UserMapper {




    public User convertCustomerDtoToUser(CustomerRegistrationDto customerRegistrationDto){
        User user = new User();
        user.setName(customerRegistrationDto.getName());
        user.setNickName(customerRegistrationDto.getNickName());
        user.setEmail(customerRegistrationDto.getEmail());
        user.setMobileNumber(customerRegistrationDto.getMobileNumber());
        user.setPhotoUrl(customerRegistrationDto.getPhotoUrl());
        user.setRoles("ROLE_CUSTOMER");
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }


    public User convertGoogleUserToUser(Map<String, String> googleClaims){
        User user = new User();
        user.setName(googleClaims.get("name"));
        user.setNickName(googleClaims.get("given_name"));
        user.setEmail(googleClaims.get("email"));
        user.setMobileNumber("0000000000");
        user.setRoles("ROLE_CUSTOMER");
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }


    public Admin convertAdminRegistrationDtoToAdmin(AdminRegistrationDto adminRegistrationDto){
        Admin admin = new Admin();
        Operator personalInfo = convertToOperatorInfo(adminRegistrationDto.getFirstName(),
                adminRegistrationDto.getLastName(),
                adminRegistrationDto.getNickName(),
                adminRegistrationDto.getEmail(),
                adminRegistrationDto.getMobileNumber(),
                "ADMIN",
                adminRegistrationDto.getPassword());

        admin.setAdminInventoryId(generateInventoryUniqueId("ADMIN", adminRegistrationDto.getEmail()));


        System.out.println("--- admin mapper at 67");
        System.out.println(admin);
        admin.setPersonalInfo(personalInfo);

        System.out.println("------ admin mapper");
        System.out.println(admin);
        return admin;
    }


    public SubAdmin convertSubAdminRegistrationDtoToSubAdmin(SubAdminRegistrationDto subAdminRegistrationDto){
        SubAdmin subAdmin = new SubAdmin();
        Operator personalInfo = convertToOperatorInfo(subAdminRegistrationDto.getFirstName(),
                subAdminRegistrationDto.getLastName(),
                subAdminRegistrationDto.getNickName(),
                subAdminRegistrationDto.getEmail(),
                subAdminRegistrationDto.getMobileNumber(),
                "SUB_ADMIN",
                subAdminRegistrationDto.getPassword());
        subAdmin.setPersonalInfo(personalInfo);

        Admin admin = new Admin();
        admin.setId(subAdminRegistrationDto.getAdminId());
        subAdmin.setAdmin(admin);

        return subAdmin;
    }
    public Seller convertSellerRegistrationDtoToSeller(SellerRegistrationDto sellerRegistrationDto){
        Seller seller = new Seller();
        Operator personalInfo = convertToOperatorInfo(sellerRegistrationDto.getFirstName(),
                sellerRegistrationDto.getLastName(),
                sellerRegistrationDto.getNickName(),
                sellerRegistrationDto.getEmail(),
                sellerRegistrationDto.getMobileNumber(),
                "SELLER",
                sellerRegistrationDto.getPassword());
        seller.setPersonalInfo(personalInfo);
        seller.setVerified(false);
        seller.setRegion(sellerRegistrationDto.getRegion());
        seller.setRegionCode(sellerRegistrationDto.getRegionCode());

        seller.setSellerInventoryId(generateInventoryUniqueId("SELLER", sellerRegistrationDto.getEmail()));


        Admin admin = new Admin();
        admin.setId(sellerRegistrationDto.getAdminId());
        seller.setAdmin(admin);

        return seller;

    }


    public DeliveryPerson deliveryPersonRegistrationDtoToDeliveryPerson(DeliveryPersonRegistrationDto deliveryPersonRegistrationDto){

        DeliveryPerson deliveryPerson = new DeliveryPerson();
        Operator personalInfo = convertToOperatorInfo(deliveryPersonRegistrationDto.getFirstName(),
                deliveryPersonRegistrationDto.getLastName(),
                deliveryPersonRegistrationDto.getNickName(),
                deliveryPersonRegistrationDto.getEmail(),
                deliveryPersonRegistrationDto.getMobileNumber(),
                "DELIVERY_PERSON",
                deliveryPersonRegistrationDto.getPassword());
        deliveryPerson.setPersonalDetails(personalInfo);


        deliveryPerson.setLicenceNumber(deliveryPersonRegistrationDto.getLicenceNumber());
        deliveryPerson.setVehicleNumber(deliveryPersonRegistrationDto.getVehicleNumber());
        deliveryPerson.setVehicleType(deliveryPersonRegistrationDto.getVehicleType());
        deliveryPerson.setVehicleModel(deliveryPersonRegistrationDto.getVehicleModel());
        deliveryPerson.setAlternateMobileNumber(deliveryPersonRegistrationDto.getAlternateMobileNumber());
        deliveryPerson.setVehicleRegistrationNumber(deliveryPersonRegistrationDto.getVehicleRegistrationNumber());

        deliveryPerson.setVerified(false);
        Seller seller = new Seller();
        seller.setId(deliveryPersonRegistrationDto.getSellerId());
        deliveryPerson.setSeller(seller);
        return deliveryPerson;


    }


    public Operator convertToOperatorInfo(String firstName,
                                          String lastName, String nickName, String email,
                                          String mobileNumber, String role, String password
    ) {
        Operator operator = new Operator();
        operator.setFirstName(firstName);
        operator.setLastName(lastName);
        operator.setFullName(firstName+" "+lastName);
        operator.setNickName(nickName);
        operator.setEmail(email);
        operator.setPassword(password);
        operator.setMobileNumber(mobileNumber);

        operator.setCreatedAt(LocalDateTime.now());
        operator.setUpdatedAt(LocalDateTime.now());


        switch (role.toUpperCase()){
            case "ADMIN":
                operator.setRoles("ROLE_ADMIN");
                operator.setStatus("ACTIVE");
                break;
            case "SUB_ADMIN":
                operator.setRoles("ROLE_SUB_ADMIN");
                operator.setStatus(SubAdminStatus.PENDING_APPROVAL.name());
                break;
            case "SELLER":
                operator.setRoles("ROLE_SELLER");
                operator.setStatus(SellerStatus.UNDER_REVIEW.name());
                break;
            case "DELIVERY_PERSON":
                operator.setRoles("ROLE_DELIVERY_PERSON");
                operator.setStatus(DeliveryPersonStatus.UNDER_REVIEW.name());
                break;
            default:
                operator.setRoles("ROLE_CUSTOMER");
                break;
        }

        System.out.println(operator);

        return operator;
    }


    public String generateInventoryUniqueId(String operatorType, String email) {
        String prefix;
        switch (operatorType) {
            case "ADMIN":
                prefix = "ADM";
                break;
            case "SELLER":
                prefix = "SEL";
                break;
            case "SUB_ADMIN":
                prefix = "SUB";
                break;
            default:
                throw new IllegalArgumentException("Invalid operator type");
        }
        return prefix + "-" + UUID.randomUUID().toString().split("-")[0]+email.substring(0,5); // Keeping it short
    }

}
