package com.swe599final.mdm.infrastructure.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "device_users")
final public class DeviceUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String accountIdentifier;

    @OneToMany
    private List<EnrollmentToken> enrollmentTokens;

    @ManyToOne
    private Enterprise enterprise;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    public List<EnrollmentToken> getEnrollmentTokens() {
        return enrollmentTokens;
    }

    public void setEnrollmentTokens(List<EnrollmentToken> enrollmentTokens) {
        this.enrollmentTokens = enrollmentTokens;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
}
