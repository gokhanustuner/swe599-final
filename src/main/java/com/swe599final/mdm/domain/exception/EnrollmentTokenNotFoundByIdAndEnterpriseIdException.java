package com.swe599final.mdm.domain.exception;

final public class EnrollmentTokenNotFoundByIdAndEnterpriseIdException extends Exception {
    public EnrollmentTokenNotFoundByIdAndEnterpriseIdException(String msg) {
        super(msg);
    }

    public EnrollmentTokenNotFoundByIdAndEnterpriseIdException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
