package com.stan.stancommerce.mapper;

import com.stan.stancommerce.dto.CategoryDto;
import com.stan.stancommerce.dto.CreateCategoryRequest;
import com.stan.stancommerce.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category mapCreateCategoryRequestToCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public CategoryDto mapCategoryToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId((long)category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
