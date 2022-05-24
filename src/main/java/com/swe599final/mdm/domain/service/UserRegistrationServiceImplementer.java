package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.domain.repository.UserRepository;
import com.swe599final.mdm.infrastructure.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public final class UserRegistrationServiceImplementer implements UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImplementer() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public void createUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
