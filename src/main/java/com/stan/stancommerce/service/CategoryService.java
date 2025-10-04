package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.CategoryDto;
import com.stan.stancommerce.dto.CreateCategoryRequest;

public interface CategoryService {
    public CategoryDto createCategory(CreateCategoryRequest request);
}
