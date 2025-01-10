package com.zodo.kart.service.users;

import com.zodo.kart.config.OperatorManagerConfig;
import com.zodo.kart.dto.users.AuthResponseDto;
import com.zodo.kart.entity.users.*;
import com.zodo.kart.enums.TokenType;
import com.zodo.kart.jwtconfig.JwtTokenGenerator;
import com.zodo.kart.repository.users.AddressRepository;
import com.zodo.kart.repository.users.RefreshTokenRepository;
import com.zodo.kart.repository.users.operator.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author : Bhanu prasad
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class OperatorService {


    private final OperatorRepository operatorRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OperatorManagerConfig operatorManagerConfig;

    private final AddressRepository addressRepository;

    private final AdminRepository adminRepository;
    private final SubAdminRepository subAdminRepository;
    private final SellerRepository sellerRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;

    public AuthResponseDto getJwtTokensForOperatorAfterAuthentication(Authentication authentication, HttpServletResponse response){

        try{
            var operator = operatorRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> {
                        log.error("[OperatorService:operatorSignInAuth] User :{} not found", authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
                    });
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
            saveOperatorRefreshToken(operator,refreshToken);
            createRefreshTokenCookie(response,refreshToken);

            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15*60)
                    .userName(operator.getEmail())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[OperatorService:operatorSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }


    public void saveOperatorRefreshToken(Operator operator, String refreshToken){

        var refreshTokenBuilder = RefreshToken.builder()
                .refreshToken(refreshToken)
                .operator(operator)
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenBuilder);

    }

    private Cookie createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }



    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
        if (!authorizationHeader.startsWith(TokenType.Bearer.name())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);
        System.out.println(refreshToken);
        //Find refreshToken from database and should not be revoked

        var refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken).
                filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        var operator = refreshTokenEntity.getOperator();

        System.out.println("------------ into autho");
        Authentication authentication = getAuthentication(operator);
        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
        log.info("[AuthService:userRefreshTokenAuth] Access token for user:{}, has been generated by refresh token", operator.getEmail());
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(operator.getEmail())
                .tokenType(TokenType.Bearer)
                .build();
    }

    public Authentication getAuthentication(Operator operator) {

        String username = operator.getEmail();
        String password = operator.getPassword();
        UserDetails userDetails = operatorManagerConfig.loadUserByUsername(username);

        // Create an authentication token with the provided credentials
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(operatorManagerConfig);
        // Authenticate the token using the AuthenticationManager
        return daoAuthenticationProvider.authenticate(authenticationToken);

    }


    public AuthResponseDto getJwtTokensForOperatorAfterRegistration(Operator operator, HttpServletResponse response) {
        Authentication authentication = getAuthentication(operator);
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
        String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
        saveOperatorRefreshToken(operator,refreshToken);
        createRefreshTokenCookie(response,refreshToken);
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(15*60)
                .userName(operator.getEmail())
                .tokenType(TokenType.Bearer)
                .build();
    }




    // get all sub admins of admin

    public List<SubAdmin> getAllSubAdminsOfAdmin(Long adminId){
        return adminRepository.findById(adminId).get().getSubAdmins();
    }

    // get all seller of admin

    public List<Seller> getAllSellersOfAdmin(Long adminId){
        return adminRepository.findById(adminId).get().getSellers();
    }

    // get all delivery person of seller

    public List<DeliveryPerson> getAllDeliveryPersonsOfSeller(Long sellerId){
        return sellerRepository.findById(sellerId).get().getDeliveryPersons();
    }


    // attention required

    // group types of sub admins

    public Map<String,List<SubAdmin>> groupSubAdminsByStatus(Long adminId){
        Map<String,List<SubAdmin>> subAdminsByStatus =  adminRepository.findById(adminId)
                .map(admin -> admin.getSubAdmins().stream()
                        .collect(Collectors.groupingByConcurrent(subAdmin -> subAdmin.getPersonalInfo().getStatus())))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin not found"));

        return subAdminsByStatus;
    }


    // group types of sellers by status

    public Map<String,List<Seller>> groupSellersByStatus(Long adminId){
        Map<String, List<Seller>> sellersByStatus = adminRepository.findById(adminId)
                .map(a -> a.getSellers().stream()
                        .collect(Collectors.groupingByConcurrent(seller -> seller.getPersonalInfo().getStatus()))).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin not found"));
        return sellersByStatus;
    }


    // group types of delivery persons by status

    public Map<String,List<DeliveryPerson>> groupDeliveryPersonsByStatus(Long sellerId){
        Map<String, List<DeliveryPerson>> deliveryPersonsByStatus = sellerRepository.findById(sellerId)
                .map(seller -> seller.getDeliveryPersons().stream()
                        .collect(Collectors.groupingByConcurrent(deliveryPerson -> deliveryPerson.getPersonalDetails().getStatus())))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Seller not found"));
        return deliveryPersonsByStatus;
    }


    // get seller by id
    public Seller getSellerById(Long sellerId){
        return sellerRepository.findById(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Seller not found"));
    }

    // get seller by region
    public Seller getSellerByRegion(String region){
        return sellerRepository.findByRegion(region).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Seller not found"));
    }

    // get delivery person by id
    public DeliveryPerson getDeliveryPersonById(Long deliveryPersonId){
        return deliveryPersonRepository.findById(deliveryPersonId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Delivery Person not found"));
    }

    // get sub admin by id
    public SubAdmin getSubAdminById(Long subAdminId){
        return subAdminRepository.findById(subAdminId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Sub Admin not found"));
    }



    // add address

    public Address addAddress(Address address, Long operatorId) {
        Address address1 = new Address();
        Operator operator = operatorRepository.findById(operatorId).orElseThrow(()-> new UsernameNotFoundException("Operator with "+operatorId+" does not exist."));
        address1.setName(address.getName());
        address1.setMobileNumber(address.getMobileNumber());
        address1.setCity(address.getCity());
        address1.setPinCode(address.getPinCode());
        address1.setCountry(address.getCountry());
        address1.setLocality(address.getLocality());
        address1.setState(address.getState());
        address1.setStreet(address.getStreet());
        address1.setOperator(operator);

        if(operator.getAddresses().size()>1){
            address1.setDefault(false);
        }else {
            address1.setDefault(true);
        }

        addressRepository.save(address1);

        return address1;

    }


    // update address

    public Address updateAddress(Address address) {

        Address address1 = addressRepository.findById(address.getId()).orElseThrow(()-> new UsernameNotFoundException("Address with "+address.getId()+" does not exist."));
        address1.setName(address.getName());
        address1.setMobileNumber(address.getMobileNumber());
        address1.setCity(address.getCity());
        address1.setPinCode(address.getPinCode());
        address1.setCountry(address.getCountry());
        address1.setLocality(address.getLocality());
        address1.setState(address.getState());
        address1.setStreet(address.getStreet());
        return addressRepository.save(address1);
    }


    // delete address

    public String deleteAddress(Long addressId) {
        try{
            addressRepository.deleteById(addressId);
            return "Address deleted successfully";
        }catch (Exception e){
            return "Address not found";
        }

    }


    // make default address
    public Address makeDefaultAddress(Long addressId, Long operatorId) {
        Address address = addressRepository.findById(addressId).orElseThrow(()-> new UsernameNotFoundException("Address with Id "+addressId+" does not exist."));
        address.setDefault(true);

        Address previousDefaultAddress = operatorRepository.findById(operatorId).get().getAddresses().stream().filter(Address::isDefault).findFirst().orElse(null);

        if(previousDefaultAddress!=null){
            previousDefaultAddress.setDefault(false);
            addressRepository.save(previousDefaultAddress);
        }

        return addressRepository.save(address);
    }



    // get admin profile by email

    public Admin getAdminByEmail(String email){
        return adminRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin not found"));
    }

    // get sub admin profile by email

    public SubAdmin getSubAdminByEmail(String email){
        return subAdminRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Sub Admin not found"));
    }

    // get seller profile by email

    public Seller getSellerByEmail(String email){
        return sellerRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Seller not found"));
    }

    // get delivery person profile by email

    public DeliveryPerson getDeliveryPersonByEmail(String email){
        return deliveryPersonRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Delivery Person not found"));
    }



}
