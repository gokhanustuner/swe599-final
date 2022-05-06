package com.swe599final.mdm.infrastructure.model;

import com.google.api.services.androidmanagement.v1.model.ApplicationPolicy;

import java.util.ArrayList;
import java.util.List;

public final class PolicyDto {
    private String displayName;

    private List<ApplicationPolicy> applications = new ArrayList<>();

    private boolean initial;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<ApplicationPolicy> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationPolicy> applications) {
        this.applications = applications;
    }

    public void addApplication(ApplicationPolicy applicationPolicy) {
        applications.add(applicationPolicy);
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }
}
