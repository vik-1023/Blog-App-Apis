package org.blog.apis.services.impl;

import org.blog.apis.entities.Category;
import org.blog.apis.exceptions.ResourceNotFoundException;
import org.blog.apis.payloads.CategoryRequestDto;
import org.blog.apis.payloads.CategoryResponseDto;
import org.blog.apis.repositories.CategoryRepo;
import org.blog.apis.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto request) {

        Category category = modelMapper.map(request, Category.class);
        Category savedCategory = categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryResponseDto.class);
    }

    @Override
    public List<CategoryResponseDto> getAllCategory() {
        List<Category> findAllCategory = categoryRepo.findAll();
        findAllCategory.forEach(System.out::println);
        return findAllCategory.stream().map(category -> modelMapper.map(category, CategoryResponseDto.class)).toList();
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", id));
        return modelMapper.map(category, CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto updateCategory(CategoryRequestDto request, Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", id));
        System.out.println("Before Mapping: " + category.getCategoryId());

        modelMapper.map(request, category);

        System.out.println("After Mapping: " + category.getCategoryId());
        Category savedCategory = categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryResponseDto.class);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", id));
        categoryRepo.delete(category);

    }
}
