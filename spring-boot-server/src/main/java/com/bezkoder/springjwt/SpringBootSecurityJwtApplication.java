package com.bezkoder.springjwt;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.*;
@SpringBootApplication
@ComponentScan(basePackages = "com.bezkoder.springjwt")
public class SpringBootSecurityJwtApplication {

	@PostConstruct
	public void insertIntoRoles(){
		List<Role> roles= new ArrayList<>();
		roles.add(new Role(ERole.ROLE_USER));
		roles.add(new Role(ERole.ROLE_ADMIN));
		roles.add(new Role(ERole.ROLE_MODERATOR));
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}

}
