package com.stan.stancommerce.exception;

import com.stan.stancommerce.enums.ResponseStatus;

public class BadRequestException extends RuntimeException {
    private String code;
    public BadRequestException() {

    }
    public BadRequestException(String message) {
        super(message);
        this.code= ResponseStatus.BAD_REQUEST.getCode();
    }
    public BadRequestException(String code, String message) {
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
