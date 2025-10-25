package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.CreateProductRequest;
import com.stan.stancommerce.dto.ProductDto;
import com.stan.stancommerce.dto.UpdateProductRequest;
import com.stan.stancommerce.entities.Category;
import com.stan.stancommerce.entities.Product;
import com.stan.stancommerce.mapper.ProductMapper;
import com.stan.stancommerce.repositories.CategoryRepository;
import com.stan.stancommerce.repositories.ProductRepository;
import com.stan.stancommerce.service.ProductService;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Data
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(CreateProductRequest request) {
        log.info("Creating product with name " + request.getName());
        ProductDto productDto = null;
        String categoryId = request.getCategoryId();
        if (StringUtils.isEmpty(categoryId)) {
            throw new RuntimeException();
        }
        Optional<Category> category = categoryRepository.findById(Long.parseLong(categoryId));
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        Optional<Product> productCheck = productRepository.findByName(request.getName());
        if (productCheck.isPresent()) {
            throw new IllegalArgumentException("Product with name " + request.getName() + " already exists");
        }
        Product product = productMapper.mapCreateProductRequestToProduct(request, category.get());
        try {
            productRepository.save(product);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        productDto = productMapper.mapProductToProductDto(product);
        log.info("productDto...{}", productDto);
        return productDto;
    }

    @Override
    public ProductDto updateProduct(UpdateProductRequest request, long id) {
        ProductDto productDto = null;
        log.info("Updating product with id " + id);
        String categoryId = request.getCategoryId();
        if (StringUtils.isEmpty(categoryId)) {
            throw new RuntimeException();
        }
        Optional<Category> category = categoryRepository.findById(Long.parseLong(categoryId));
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        Optional<Product> productCheck = productRepository.findById(id);
        if (!productCheck.isPresent()) {
            throw new IllegalArgumentException("Product with id " + id + " does not exists");
        }
        Product product = productCheck.get();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category.get());
        try {
            product = productRepository.save(product);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        productDto = productMapper.mapProductToProductDto(product);
        log.info("productDto...{}", productDto);
        return productDto;

    }

    @Override
    public List<ProductDto> findProduct(Long id) {
        log.info("Finding product with id " + id);
        ProductDto productDto = null;
        List<Product> product = null;
        if (id != null) {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent()) {
                product = productRepository.findByCategory(category.get());
            }
        } else {
            product = productRepository.findAll();
        }
        return product.stream().map(productMapper::mapProductToProductDto).toList();
    }

    @Override
    public ProductDto findProductById(Long id) {
        log.info("Finding product with id " + id);
        ProductDto productDto = null;
        Product product = null;
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isPresent()) {
                product = optionalProduct.get();
                productDto = productMapper.mapProductToProductDto(product);
                log.info("productDto...{}", productDto);
                return productDto;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return productDto;
    }
}
