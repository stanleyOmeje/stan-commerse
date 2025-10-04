package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.CreateProductRequest;
import com.stan.stancommerce.dto.ProductDto;
import com.stan.stancommerce.dto.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    public ProductDto createProduct(CreateProductRequest request);

    ProductDto updateProduct(UpdateProductRequest request, long id);

    List<ProductDto> findProduct(Long id);

    ProductDto findProductById(Long id);
}
