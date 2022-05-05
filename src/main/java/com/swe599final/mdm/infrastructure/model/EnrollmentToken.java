package com.swe599final.mdm.infrastructure.model;

import javax.persistence.*;

@Entity
@Table(name = "enrollment_tokens")
public class EnrollmentToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne
    private DeviceUser deviceUser;

    @OneToOne
    private Policy policy;

    @ManyToOne
    private Enterprise enterprise;

    @Column(nullable = false)
    private EnrollmentTokenStatus status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DeviceUser getDeviceUser() {
        return deviceUser;
    }

    public void setDeviceUser(DeviceUser deviceUser) {
        this.deviceUser = deviceUser;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public EnrollmentTokenStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentTokenStatus status) {
        this.status = status;
    }
}
