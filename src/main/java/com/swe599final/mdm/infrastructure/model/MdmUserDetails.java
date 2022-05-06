package com.swe599final.mdm.infrastructure.model;

import org.springframework.security.core.userdetails.UserDetails;

public interface MdmUserDetails extends UserDetails {
    public long getId();
    public User getUser();
}
