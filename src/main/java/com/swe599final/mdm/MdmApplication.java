package com.swe599final.mdm;

import com.google.api.services.androidmanagement.v1.model.EnrollmentToken;
import com.swe599final.mdm.domain.repository.UserRepository;
import com.swe599final.mdm.domain.service.AndroidManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Controller
public class MdmApplication {
	public static void main(String[] args) {
		/**
		try {
			AndroidManager androidManager = new AndroidManager();

			EnrollmentToken enrollmentToken = androidManager.createEnrollmentToken(
				"enterprises/LC01yv4t3b/policies/96e795cd-a137-4cc8-a301-b111a3f4fc69",
				"enterprises/LC01yv4t3b"
			);

			System.out.println(enrollmentToken.getQrCode());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		*/
		SpringApplication.run(MdmApplication.class, args);
	}
}
