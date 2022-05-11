package com.swe599final.mdm.domain.exception;

final public class DeviceUserNotFoundByEnterpriseIdAndAccountIdentifierException extends Exception {
    public DeviceUserNotFoundByEnterpriseIdAndAccountIdentifierException(String msg) {
        super(msg);
    }

    public DeviceUserNotFoundByEnterpriseIdAndAccountIdentifierException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
