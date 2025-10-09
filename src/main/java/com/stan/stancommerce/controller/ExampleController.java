package com.stan.stancommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("example")
public class ExampleController {
    @GetMapping
    public String getExample(@RequestHeader(name = "x-api-key") String token,
                             @RequestHeader(name = "x-secret-key") String key) {
        return "My example " + token + " and key " + key;
    }
}
