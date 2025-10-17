package com.stan.stancommerce.exception;

import com.stan.stancommerce.enums.ResponseStatus;

public class SystemError extends RuntimeException {
    private String code;
    public SystemError(String message) {
        super(message);
        this.code = ResponseStatus.BAD_REQUEST.getCode();
    }
    public SystemError(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
