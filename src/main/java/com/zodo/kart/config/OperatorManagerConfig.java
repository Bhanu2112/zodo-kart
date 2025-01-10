package com.zodo.kart.config;

import com.zodo.kart.repository.users.operator.OperatorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
public class OperatorManagerConfig implements UserDetailsService {

    private final OperatorRepository operatorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return operatorRepository
                .findByEmail(email)
                .map(OperatorConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("Operator with "+email+" does not exist"));
    }
}
