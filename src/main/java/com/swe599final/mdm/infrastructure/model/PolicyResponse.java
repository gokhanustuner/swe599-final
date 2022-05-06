package com.swe599final.mdm.infrastructure.model;

import com.google.api.services.androidmanagement.v1.model.ApplicationPolicy;

import java.util.List;

public final class PolicyResponse {
    private Long id;

    private String displayName;

    private String name;

    private List<ApplicationPolicy> applications;

    private boolean initial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ApplicationPolicy> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationPolicy> applications) {
        this.applications = applications;
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }
}
