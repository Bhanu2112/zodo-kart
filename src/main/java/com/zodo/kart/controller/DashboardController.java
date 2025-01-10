package com.zodo.kart.controller;

import com.zodo.kart.entity.users.User;
import com.zodo.kart.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Author : Bhanu prasad
 */


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    //@PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_ADMIN','ROLE_CUSTOMER')")

    @PreAuthorize("hasAnyAuthority('SCOPE_SELLER','SCOPE_ADMIN','SCOPE_CUSTOMER')")
    @GetMapping("/welcome-message")
    public ResponseEntity<String> getFirstWelcomeMessage(Authentication authentication){
        return ResponseEntity.ok("Welcome to the Zodo APP:"+authentication.getName()+" with scope:"+authentication.getAuthorities());
    }

    // @PreAuthorize("hasRole('ROLE_SELLER')")
    @PreAuthorize("hasAuthority('SCOPE_SELLER')")
    @GetMapping("/manager-message")
    public ResponseEntity<String> getManagerData(Principal principal){
        return ResponseEntity.ok("Seller ::"+principal.getName());

    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/admin-message")
    public ResponseEntity<String> getAdminData(@RequestParam("message") String message, Principal principal){

        User user = userRepository.findByMobileNumber(principal.getName()).get();

        return ResponseEntity.ok("Admin::"+user.getName()+" has this message:"+message);

    }
}
