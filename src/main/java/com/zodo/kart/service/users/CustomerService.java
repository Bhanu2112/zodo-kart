package com.zodo.kart.service.users;

import com.zodo.kart.dto.users.CustomerDto;
import com.zodo.kart.entity.users.Address;
import com.zodo.kart.entity.users.User;
import com.zodo.kart.repository.users.AddressRepository;
import com.zodo.kart.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author : Bhanu prasad
 */


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    // update customer profile
    public CustomerDto updateCustomerProfile(CustomerDto customerDto) {

        User user = userRepository.findById(customerDto.getId()).orElseThrow(()-> new UsernameNotFoundException("User with "+customerDto.getId()+" does not exist."));
        user.setName(customerDto.getName());
        user.setNickName(customerDto.getNickName());
        user.setEmail(customerDto.getEmail());
        user.setUpdatedAt(LocalDateTime.now());

        // profile pic need to add

        userRepository.save(user);
        return userToCustomerDto(user);
    }


    // get customer profile
    public CustomerDto getCustomerProfile(String mobileNumber) {
        return userToCustomerDto(userRepository.findByMobileNumber(mobileNumber).orElseThrow(()-> new UsernameNotFoundException("User with "+mobileNumber+" does not exist.")));
    }






    // get all customers for admin dashboard
    public List<User> getAllCustomers() {
        return userRepository.findAll();
    }


    // add address
    public Address addAddress(Address address, Long userId) {
        Address address1 = new Address();
        User user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User with "+userId+" does not exist."));
        address1.setName(address.getName());
        address1.setMobileNumber(address.getMobileNumber());
        address1.setCity(address.getCity());
        address1.setPinCode(address.getPinCode());
        address1.setCountry(address.getCountry());
        address1.setLocality(address.getLocality());
        address1.setState(address.getState());
        address1.setStreet(address.getStreet());
        address1.setUser(user);

        if(user.getAddresses().size()>1){
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


    // make default address
    public Address makeDefaultAddress(Long addressId, Long userId) {
        Address address = addressRepository.findById(addressId).orElseThrow(()-> new UsernameNotFoundException("Address with Id "+addressId+" does not exist."));
        address.setDefault(true);

        Address previousDefaultAddress = userRepository.findById(userId).get().getAddresses().stream().filter(Address::isDefault).findFirst().orElse(null);

        if(previousDefaultAddress!=null){
            previousDefaultAddress.setDefault(false);
            addressRepository.save(previousDefaultAddress);
        }

        return addressRepository.save(address);
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

    private CustomerDto userToCustomerDto(User user) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(user.getId());
        customerDto.setName(user.getName());
        customerDto.setNickName(user.getNickName());
        customerDto.setEmail(user.getEmail());
        customerDto.setPhotoUrl(user.getPhotoUrl());
        customerDto.setMobileNumber(user.getMobileNumber());
        customerDto.setCreatedAt(user.getCreatedAt());
        customerDto.setUpdatedAt(user.getUpdatedAt());
        customerDto.setAddresses(user.getAddresses());
        return customerDto;
    }
}

