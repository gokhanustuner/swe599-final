package com.swe599final.mdm.domain.exception;

final public class PolicyNotFoundByIdAndEnterpriseIdException extends Exception {
    public PolicyNotFoundByIdAndEnterpriseIdException(String msg) {
        super(msg);
    }

    public PolicyNotFoundByIdAndEnterpriseIdException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
