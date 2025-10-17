package com.stan.stancommerce.enums;

import lombok.Data;

public enum ResponseStatus {
    SUCCESS("00", "Successful"),
    FAILED("01", "Failed"),
    NOT_FOUND("100", "Not Found"),
    ALREADY_EXIXT("200", "Already Exixt"),
    CREATED("300", "Created"),
    BAD_REQUEST("400", "Bad Request"),
    SYSTEM_ERROR("500", "System Error"),;

    private String code;
    private String message;
    ResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
