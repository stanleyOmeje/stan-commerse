package com.stan.stancommerce.exception;

import com.stan.stancommerce.enums.ResponseStatus;

public class NotFoundException extends RuntimeException {
    private String code;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
        this.code = ResponseStatus.NOT_FOUND.getCode();
    }

    public NotFoundException(String code, String message) {
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
