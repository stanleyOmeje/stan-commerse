package com.stan.stancommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("/my-header")
public class MessageController {
    public String getMessage(){
        return "Hello World";
    }
}
