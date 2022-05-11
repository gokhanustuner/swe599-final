package com.swe599final.mdm.infrastructure.model;

import javax.persistence.*;

@Entity
@Table(name = "enrollment_tokens")
final public class EnrollmentToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String token;

    @OneToOne
    private Policy policy;

    @ManyToOne
    private Enterprise enterprise;

    @Lob
    @Column(nullable = false, length = 10000)
    private String qrCode;

    @Column(nullable = false)
    private String duration;

    @Column
    private String additionalData;

    @Column(nullable = false)
    private String expirationTimestamp;

    @Column(nullable = false)
    private String policyName;

    @Column(nullable = false)
    private Boolean oneTimeOnly;

    @Column(nullable = false)
    private String allowPersonalUsage;

    @Column(nullable = false)
    private EnrollmentTokenStatus status;

    @ManyToOne
    private DeviceUser deviceUser;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public String getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(String expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Boolean getOneTimeOnly() {
        return oneTimeOnly;
    }

    public void setOneTimeOnly(Boolean oneTimeOnly) {
        this.oneTimeOnly = oneTimeOnly;
    }

    public String getAllowPersonalUsage() {
        return allowPersonalUsage;
    }

    public void setAllowPersonalUsage(String allowPersonalUsage) {
        this.allowPersonalUsage = allowPersonalUsage;
    }

    public DeviceUser getDeviceUser() {
        return deviceUser;
    }

    public void setDeviceUser(DeviceUser deviceUser) {
        this.deviceUser = deviceUser;
    }
}
