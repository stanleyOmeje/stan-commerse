package com.stan.stancommerce.dto.response;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DefaultResponse<T> {
    private String status;
    private String message;

    private T data;

    public DefaultResponse(){}

    public DefaultResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public DefaultResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
