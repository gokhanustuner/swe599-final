package com.swe599final.mdm.application;

import com.swe599final.mdm.domain.repository.UserRepository;
import com.swe599final.mdm.domain.service.MdmUserDetailsService;
import com.swe599final.mdm.infrastructure.model.MdmUserDetails;
import com.swe599final.mdm.infrastructure.model.MdmUserDetailsImplementer;
import com.swe599final.mdm.infrastructure.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MdmUserDetailsServiceImplementer implements MdmUserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public MdmUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return user.map(MdmUserDetailsImplementer::new).get();
    }
}
