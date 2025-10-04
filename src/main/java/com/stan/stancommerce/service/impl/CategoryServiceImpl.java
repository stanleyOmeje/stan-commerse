package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.CategoryDto;
import com.stan.stancommerce.dto.CreateCategoryRequest;
import com.stan.stancommerce.entities.Category;
import com.stan.stancommerce.mapper.CategoryMapper;
import com.stan.stancommerce.repositories.CategoryRepository;
import com.stan.stancommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public CategoryDto createCategory(CreateCategoryRequest request) {
        log.info("Inside Create Category with request: {}", request);
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        Category category = categoryMapper.mapCreateCategoryRequestToCategory(request);
        try{
            Optional<Category> categoryCheck = categoryRepository.findByName(request.getName());
            if (categoryCheck.isPresent()) {
                throw new IllegalArgumentException("Category with name " + request.getName() + " already exists");
            }
            category = categoryRepository.save(category);
        }catch (Exception e){
            e.printStackTrace();
        }
         CategoryDto categoryDto = categoryMapper.mapCategoryToCategoryDto(category);
        log.info("categoryDto...{}", categoryDto);
        return categoryDto;
    }
}
