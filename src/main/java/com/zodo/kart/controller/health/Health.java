package com.zodo.kart.controller.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public")
public class Health {

    @GetMapping("/health")
    public String health() {
        return "application is up, zodokart";
    }
}
