package com.stan.stancommerce.controller;


import com.stan.stancommerce.dto.CreateProductRequest;
import com.stan.stancommerce.dto.ProductDto;
import com.stan.stancommerce.dto.UpdateProductRequest;
import com.stan.stancommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable long id) {
        log.info("Updating product with id " + id);
        ProductDto productDto = null;
        try {
            productDto = productService.updateProduct(request, id);
            return ResponseEntity.ok(productDto);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findProduct(@RequestParam( name = "categoryId", required = false) Long id) {
        log.info("Finding product with id " + id);
        List<ProductDto> productDto = null;
        try {
            productDto = productService.findProduct(id);
                return ResponseEntity.ok(productDto);
            }catch (Exception e){
            log.error(e.getMessage());
        }
         return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
        log.info("getting product with id " + id);
            ProductDto productDto = null;
            try {
                productDto = productService.findProductById(id);
                return ResponseEntity.ok(productDto);
            }catch (Exception e){
                log.error(e.getMessage());
            }
            return ResponseEntity.noContent().build();
        }

}
