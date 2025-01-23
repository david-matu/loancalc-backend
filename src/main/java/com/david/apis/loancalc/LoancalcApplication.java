package com.david.apis.loancalc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ComponentScan("com.david.apis.loancalc")
@EnableWebFlux
public class LoancalcApplication {
	private static Logger LOG = LoggerFactory.getLogger(LoancalcApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(LoancalcApplication.class, args);
	}
}
