package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.infrastructure.model.MdmUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MdmUserDetailsService extends UserDetailsService {
    MdmUserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
