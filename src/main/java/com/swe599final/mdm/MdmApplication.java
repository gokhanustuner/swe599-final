package com.swe599final.mdm;

import com.swe599final.mdm.domain.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class MdmApplication {
	public static void main(String[] args) {
		SpringApplication.run(MdmApplication.class, args);
	}
}
