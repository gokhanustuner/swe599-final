package com.swe599final.mdm.infrastructure.model;

final public class EnrollmentTokenResponse {
    private Long id;

    private String name;

    private String value;

    private String duration;

    private String expirationTimestamp;

    private Policy policy;

    private String policyName;

    private String additionalData;

    private String qrCode;

    private Boolean oneTimeOnly;

    private DeviceUser deviceUser;

    private String allowPersonalUsage;

    private EnrollmentTokenStatus status;

    private String qrCodeFilePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(String expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getOneTimeOnly() {
        return oneTimeOnly;
    }

    public void setOneTimeOnly(Boolean oneTimeOnly) {
        this.oneTimeOnly = oneTimeOnly;
    }

    public DeviceUser getDeviceUser() {
        return deviceUser;
    }

    public void setDeviceUser(DeviceUser deviceUser) {
        this.deviceUser = deviceUser;
    }

    public String getAllowPersonalUsage() {
        return allowPersonalUsage;
    }

    public void setAllowPersonalUsage(String allowPersonalUsage) {
        this.allowPersonalUsage = allowPersonalUsage;
    }

    public EnrollmentTokenStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentTokenStatus status) {
        this.status = status;
    }

    public String getQrCodeFilePath() {
        return qrCodeFilePath;
    }

    public void setQrCodeFilePath(String qrCodeFilePath) {
        this.qrCodeFilePath = qrCodeFilePath;
    }
}
