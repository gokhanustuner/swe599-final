package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MdmUserDetailsService extends UserDetailsService {
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
