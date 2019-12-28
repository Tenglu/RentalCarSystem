package com.example.rentalCar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class RentalCarSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalCarSystemApplication.class, args);
	}

}
