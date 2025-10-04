package com.stan.stancommerce.mapper;

import com.stan.stancommerce.dto.CreateProductRequest;
import com.stan.stancommerce.dto.ProductDto;
import com.stan.stancommerce.entities.Category;
import com.stan.stancommerce.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDto mapProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setCategoryName(product.getCategory().getName());
        productDto.setId(product.getId());
        return productDto;
    }

    public Product mapCreateProductRequestToProduct(CreateProductRequest request, Category category) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        return product;

    }
}
