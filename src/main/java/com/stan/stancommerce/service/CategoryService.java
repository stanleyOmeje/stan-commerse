package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.CategoryDto;
import com.stan.stancommerce.dto.CreateCategoryRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;

public interface CategoryService {
    public DefaultResponse createCategory(CreateCategoryRequest request);
}
