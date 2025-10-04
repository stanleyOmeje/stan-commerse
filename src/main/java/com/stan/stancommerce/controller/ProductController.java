package com.stan.stancommerce.controller;


import com.stan.stancommerce.dto.CreateProductRequest;
import com.stan.stancommerce.dto.ProductDto;
import com.stan.stancommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest request,
                                                    UriComponentsBuilder uriBuilder) {
        log.info("Creating product with name " + request.getName());
        ProductDto productDto = null;
        try{
            productDto = productService.createProduct(request);
            URI uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
            return ResponseEntity.created(uri).body(productDto);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.noContent().build();

    }
}
