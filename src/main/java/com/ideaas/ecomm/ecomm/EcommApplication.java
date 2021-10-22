package com.ideaas.ecomm.ecomm;

import com.ideaas.ecomm.ecomm.domain.Role;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.services.UserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@AllArgsConstructor
public class EcommApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(IUserService userService) {
		return args -> {
			Role role = new Role(1l, "ROLE_ADMIN");
			User user = new User("admin", "user administrator", "admin", role);
			userService.save(user);
		};
	}
}
