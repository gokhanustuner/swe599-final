package com.swe599final.mdm.domain.exception;

final public class EnterpriseNotFoundByUserIdException extends Exception {
    public EnterpriseNotFoundByUserIdException(String msg) {
        super(msg);
    }

    public EnterpriseNotFoundByUserIdException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
