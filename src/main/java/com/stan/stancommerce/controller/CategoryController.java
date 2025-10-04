package com.stan.stancommerce.controller;

import com.stan.stancommerce.dto.CategoryDto;
import com.stan.stancommerce.dto.CreateCategoryRequest;
import com.stan.stancommerce.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryRequest request,
                                                      UriComponentsBuilder uriBuilder) {
        try{
            CategoryDto category = categoryService.createCategory(request);
            URI uri = uriBuilder.path("/categories/{id}").buildAndExpand(category.getId()).toUri();
            return ResponseEntity.created(uri).body(category);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    return ResponseEntity.badRequest().build();
    }
}
