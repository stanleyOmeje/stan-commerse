package com.stan.stancommerce.exception;


import com.stan.stancommerce.enums.ResponseStatus;

public class UnAuthorizedException extends RuntimeException {
    private String code;
    public UnAuthorizedException(String message) {
        super(message);
        this.code= ResponseStatus.ALREADY_EXIST.getCode();
    }
    public UnAuthorizedException(String code, String message) {
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
