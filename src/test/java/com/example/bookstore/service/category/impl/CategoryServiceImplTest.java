package com.example.bookstore.service.category.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.category.CategoryCreateRequestDto;
import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 200L;

    private static final Pageable VALID_PAGEABLE = PageRequest.of(0, 10);

    private static final Category VALID_CATEGORY = new Category()
            .setId(VALID_ID)
            .setName("Fantasy")
            .setDescription("Super cool category.");
    private static final CategoryCreateRequestDto VALID_CREATE_CATEGORY_DTO =
            new CategoryCreateRequestDto()
                  .setName("Fantasy")
                  .setDescription("Super cool category.");
    private static final CategoryDto VALID_CATEGORY_DTO = new CategoryDto()
            .setName("Fantasy")
            .setDescription("Super cool category.");

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Test retrieving all categories from DB")
    public void getAll_ShouldReturnAllCategoriesDto() {
        when(categoryRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(VALID_CATEGORY)));

        List<CategoryDto> actual = categoryService.findAll(VALID_PAGEABLE);

        assertEquals(1, actual.size());
        verify(categoryRepository, times(1)).findAll(VALID_PAGEABLE);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Test retrieving the category with existing id from DB")
    public void findById_ExistingId_ShouldReturnCategoryDto() {
        when(categoryRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_CATEGORY));
        when(categoryMapper.toDto(VALID_CATEGORY)).thenReturn(VALID_CATEGORY_DTO);

        CategoryDto actual = categoryService.getById(VALID_ID);

        assertEquals(VALID_CATEGORY_DTO, actual);
        verify(categoryRepository, times(1)).findById(VALID_ID);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Test the creation of Category from CreateCategoryRequestDto")
    public void create_ValidBook_ShouldSaveNewBookToDB() {
        when(categoryMapper.toEntity(VALID_CREATE_CATEGORY_DTO))
                .thenReturn(VALID_CATEGORY);
        when(categoryRepository.save(VALID_CATEGORY)).thenReturn(VALID_CATEGORY);
        when(categoryMapper.toDto(VALID_CATEGORY)).thenReturn(VALID_CATEGORY_DTO);

        CategoryDto actual = categoryService.save(VALID_CREATE_CATEGORY_DTO);
        assertEquals(VALID_CATEGORY_DTO, actual);
        verify(categoryRepository, times(1)).save(VALID_CATEGORY);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Test deleting a category by ID")
    public void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(categoryRepository).deleteById(VALID_ID);
        categoryService.deleteById(VALID_ID);

        verify(categoryRepository, times(1)).deleteById(VALID_ID);
        verifyNoMoreInteractions(categoryRepository);
    }
}
