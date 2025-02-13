package com.ehr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class PatientRecordServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientRecordServiceApplication.class, args);
	}

}
