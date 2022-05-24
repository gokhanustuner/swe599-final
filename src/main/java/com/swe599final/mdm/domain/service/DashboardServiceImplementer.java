package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.domain.repository.UserRepository;
import com.swe599final.mdm.infrastructure.model.Enterprise;
import com.swe599final.mdm.infrastructure.model.MdmUserDetailsImplementer;
import com.swe599final.mdm.infrastructure.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class DashboardServiceImplementer implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Enterprise getApplicationUsersEnterprise(UserDetails userDetails) {
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userDetails.getUsername()));
        MdmUserDetailsImplementer mappedUser = user.map(MdmUserDetailsImplementer::new).get();

        return mappedUser.getUser().getEnterprise();
    }
}
