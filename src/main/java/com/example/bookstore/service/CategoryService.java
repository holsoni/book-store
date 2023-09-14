package com.example.bookstore.service;

import com.example.bookstore.dto.category.CategoryCreateRequestDto;
import com.example.bookstore.dto.category.CategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryCreateRequestDto categoryDto);

    CategoryDto update(Long id, CategoryCreateRequestDto categoryDto);

    void deleteById(Long id);
}
