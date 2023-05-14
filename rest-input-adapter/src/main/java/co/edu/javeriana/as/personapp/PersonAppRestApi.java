package co.edu.javeriana.as.personapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class PersonAppRestApi {

	public static void main(String[] args) {
		log.info("Starting PersonAppRestApi ...");
		SpringApplication.run(PersonAppRestApi.class, args);
		log.info("Started PersonAppRestApi OK");
	}

}
