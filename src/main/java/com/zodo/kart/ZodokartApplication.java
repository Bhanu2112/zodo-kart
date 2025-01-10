package com.zodo.kart;

import com.zodo.kart.config.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.statemachine.config.EnableStateMachine;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
public class ZodokartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZodokartApplication.class, args);
	}

}
