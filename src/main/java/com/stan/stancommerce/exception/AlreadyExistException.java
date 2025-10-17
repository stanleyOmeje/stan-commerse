package com.stan.stancommerce.exception;


import com.stan.stancommerce.enums.ResponseStatus;

public class AlreadyExistException extends RuntimeException {
    private String code;
    public AlreadyExistException(String message) {
        super(message);
        this.code= ResponseStatus.ALREADY_EXIXT.getCode();
    }
    public AlreadyExistException(String code, String message) {
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
