package com.zodo.kart.config;

import com.zodo.kart.repository.users.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Author : Bhanu prasad
 */
@Service
@AllArgsConstructor
public class UserManagerConfig implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        return userRepository
                .findByMobileNumber(mobileNumber)
                .map(UserConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("User with "+mobileNumber+" does not exist"));
    }
}
