package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.infrastructure.model.User;

public interface UserRegistrationService {
    abstract public void createUser(User user);
}
