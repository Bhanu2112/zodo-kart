package com.zodo.kart.service.users;

import com.zodo.kart.entity.users.Otp;
import com.zodo.kart.repository.users.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * Author : Bhanu prasad
 */

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;


    public String sendOtp(String mobileNumber){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        Otp otp1 = new Otp();
        otp1.setMobileNumber(mobileNumber);
        otp1.setOtp(otp);
        otp1.setCreatedAt(LocalDateTime.now());
        otp1.setExpirationTime(LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
        otpRepository.save(otp1);
        return "Sending OTP: " + otp + " to mobile number: " + mobileNumber;
    }

    public boolean verifyOtp(String mobileNumber, int code){
        Otp otp = otpRepository.findById(mobileNumber).orElseThrow(() -> new UsernameNotFoundException("User with "+mobileNumber+" does not exist"));

        System.out.println(otp);

        if(otp.getOtp()==code && LocalDateTime.now().isBefore(otp.getExpirationTime())){
            return true;
        }
        return false;
    }

}
