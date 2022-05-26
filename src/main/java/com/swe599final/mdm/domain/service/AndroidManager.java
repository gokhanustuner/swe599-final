package com.swe599final.mdm.domain.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidmanagement.v1.AndroidManagement;
import com.google.api.services.androidmanagement.v1.model.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public final class AndroidManager {
    /** The JSON credential file for the service account. */
    private static final String SERVICE_ACCOUNT_CREDENTIAL_FILE = "/Users/gokhanustuner/Downloads/swe578-c6b0b00ff06c.json";

    private static final String OAUTH_SCOPE = "https://www.googleapis.com/auth/androidmanagement";

    private static final String APP_NAME = "Android Management API sample app";

    private static final String PROJECT_ID = "swe578";

    private static final String PUB_SUB_TOPIC = "projects/swe578/topics/device_enrollment";

    private static final List<String> ENABLED_NOTIFICATION_TYPES = new ArrayList<>(
        Arrays.asList("ENROLLMENT", "STATUS_REPORT")
    );

    private final AndroidManagement androidManagementClient;

    public AndroidManager() throws IOException, GeneralSecurityException {
        androidManagementClient = getAndroidManagementClient();
    }

    public Enterprise createEnterprise(String displayName, String email) throws IOException {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setContactEmail(email);
        // contactInfo.setDataProtectionOfficerName("John Doe");
        // contactInfo.setDataProtectionOfficerEmail("dpo@example.com");
        // contactInfo.setDataProtectionOfficerPhone("+33 1 99 00 98 76 54");
        // contactInfo.setEuRepresentativeName("Jane Doe");
        // contactInfo.setEuRepresentativeEmail("eurep@example.com");
        // contactInfo.setEuRepresentativePhone("+33 1 99 00 12 34 56");

        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseDisplayName(displayName);
        enterprise.setContactInfo(contactInfo);

        return androidManagementClient
            .enterprises()
            .create(enterprise)
            .setProjectId(PROJECT_ID)
            .setAgreementAccepted(true)
            .execute()
            .setPubsubTopic(PUB_SUB_TOPIC)
            .setEnabledNotificationTypes(ENABLED_NOTIFICATION_TYPES);
    }

    public Empty deleteEnterprise(String enterpriseName) throws IOException {
        return androidManagementClient.enterprises().delete(enterpriseName).execute();
    }

    public Enterprise getEnterprise(String name) throws IOException {
        return androidManagementClient.enterprises().get(name).execute();
    }

    public Policy createPolicy(String name, Policy policy) throws IOException {
        String policyName = name + "/policies/" + UUID.randomUUID();

        return androidManagementClient.enterprises().policies().patch(policyName, policy).execute();
    }

    public Policy getPolicy(String name) throws IOException {
        return androidManagementClient.enterprises().policies().get(name).execute();
    }

    public EnrollmentToken createEnrollmentToken(String policyId, String enterpriseName, String accountIdentifier) throws IOException {
        EnrollmentToken token =
            new EnrollmentToken()
                .setAllowPersonalUsage("PERSONAL_USAGE_ALLOWED")
                .setOneTimeOnly(true)
                .setUser(new User().setAccountIdentifier(accountIdentifier))
                .setPolicyName(policyId)
                .setDuration("600s");

        return androidManagementClient.enterprises().enrollmentTokens().create(enterpriseName, token).execute();
    }

    public Empty deleteEnrollmentToken(String enrollmentTokenName) throws IOException {
        return androidManagementClient.enterprises().enrollmentTokens().delete(enrollmentTokenName).execute();
    }

    public List<Device> listDevices(String enterpriseName) throws IOException {
        return androidManagementClient.enterprises().devices().list(enterpriseName).execute().getDevices();
    }

    public Empty deleteDevice(String deviceName) throws IOException {
        return androidManagementClient.enterprises().devices().delete(deviceName).execute();
    }

    private static AndroidManagement getAndroidManagementClient() throws IOException, GeneralSecurityException {
        try (FileInputStream input =
                     new FileInputStream(SERVICE_ACCOUNT_CREDENTIAL_FILE)) {
            GoogleCredential credential =
                    GoogleCredential.fromStream(input)
                            .createScoped(Collections.singleton(OAUTH_SCOPE));
            return new AndroidManagement.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName(APP_NAME)
                    .build();
        }
    }
}
