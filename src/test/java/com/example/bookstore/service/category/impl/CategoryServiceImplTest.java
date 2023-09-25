package com.example.bookstore.service.category.impl;

import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
   private static final Long VALID_ID = 1l;
   private static final Long INVALID_ID = 200l;

   private static final Pageable VALID_PAGEABLE = PageRequest.of(0, 10);

   private static final Category VALID_CATEGORY = new Category()
           .setId(VALID_ID)
           .setName("Fantasy")
           .setDescription("Super cool category.");


   private static final Category VALID_CREATE_CATEGORY_DTO = new Category()
         .setName("Horror")
         .setDescription("Not such a cool category.");


   @Mock
   CategoryRepository categoryRepository;

   @Mock
   CategoryMapper categoryMapper;

   @InjectMocks
   CategoryServiceImpl categoryService;



   @Test
    void findAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void save() {
    }

    @Test
    void deleteById() {
    }
}