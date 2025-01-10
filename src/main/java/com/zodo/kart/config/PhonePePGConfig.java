package com.zodo.kart.config;

import com.phonepe.sdk.pg.Env;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author : Bhanu prasad
 */

@Configuration
public class PhonePePGConfig {

    @Value("${phonepe.pg.merchantId}")
    private String merchantId;

    @Value("${phonepe.pg.saltKey}")
    private String saltKey;

    @Value("${phonepe.pg.saltIndex}")
    private Integer saltIndex;

    private Env env = Env.UAT;

    @Value("${phonepe.pg.publishEvents}")
    private boolean shouldPublishEvents;




    @Bean
    public PhonePePaymentClient phonePeClient(){
        return new PhonePePaymentClient(merchantId, saltKey, saltIndex, env, shouldPublishEvents);
    }






}
