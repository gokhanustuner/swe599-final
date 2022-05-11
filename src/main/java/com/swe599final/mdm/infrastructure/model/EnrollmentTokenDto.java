package com.swe599final.mdm.infrastructure.model;

import java.util.ArrayList;
import java.util.List;

final public class EnrollmentTokenDto {
    private List<Policy> policies = new ArrayList<>();

    private Long policyId;

    private String deviceUser;

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    public void addPolicy(Policy policy) {
        policies.add(policy);
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getDeviceUser() {
        return deviceUser;
    }

    public void setDeviceUser(String deviceUser) {
        this.deviceUser = deviceUser;
    }
}
