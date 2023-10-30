package com.lavanya.springjwt;

import com.lavanya.springjwt.models.ERole;
import com.lavanya.springjwt.models.Role;
import com.lavanya.springjwt.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.*;
@SpringBootApplication
@ComponentScan(basePackages = "com.lavanya.springjwt")
public class SpringBootSecurityJwtApplication {

	@Autowired
	private RoleRepository roleRepository;

	@PostConstruct
	public void insertIntoRoles(){
		List<Role> roles= new ArrayList<>();
		roles.add(new Role(ERole.ROLE_USER));
		roles.add(new Role(ERole.ROLE_ADMIN));
		roles.add(new Role(ERole.ROLE_MODERATOR));
		roleRepository.saveAll(roles);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}

}
