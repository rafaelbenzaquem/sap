package br.jus.trf1.sap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SapApplication.class, args);
	}

}
