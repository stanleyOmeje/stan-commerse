package com.stan.stancommerce.exception;

import com.stan.stancommerce.dto.response.DefaultResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionResolver extends ResponseEntityExceptionHandler  {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler
    public ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request) {
       return new ResponseEntity<>(new DefaultResponse(ex.getCode(), ex.getMessage()),new HttpHeaders(), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler
    public ResponseEntity<Object> handleAlreadyExist(AlreadyExistException ex, WebRequest request) {
        return new ResponseEntity<>(new DefaultResponse(ex.getCode(), ex.getMessage()),new HttpHeaders(), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler
    public ResponseEntity<Object> handleBadRequest(BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(new DefaultResponse(ex.getCode(), ex.getMessage()),new HttpHeaders(), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler
    public ResponseEntity<Object> handleSystemError(SystemError ex, WebRequest request) {
        return new ResponseEntity<>(new DefaultResponse(ex.getCode(), ex.getMessage()),new HttpHeaders(), HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler
    public ResponseEntity<Object> handleUnAuthorizedException(UnAuthorizedException ex, WebRequest request) {
        return new ResponseEntity<>(new DefaultResponse(ex.getCode(), ex.getMessage()),new HttpHeaders(), HttpStatus.OK);
    }
}
