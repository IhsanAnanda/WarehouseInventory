package com.indocyber.SoalOBS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SoalObsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoalObsApplication.class, args);
	}

}
