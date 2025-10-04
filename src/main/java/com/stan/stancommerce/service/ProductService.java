package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.CreateProductRequest;
import com.stan.stancommerce.dto.ProductDto;

public interface ProductService {
    public ProductDto createProduct(CreateProductRequest request);
}
