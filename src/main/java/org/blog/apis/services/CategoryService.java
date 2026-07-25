package org.blog.apis.services;

import org.blog.apis.payloads.CategoryRequestDto;
import org.blog.apis.payloads.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto request);

    List<CategoryResponseDto> getAllCategory();

    CategoryResponseDto getCategoryById(Long id);

    CategoryResponseDto updateCategory(CategoryRequestDto request, Long id);

    void deleteCategory(Long id);
}
