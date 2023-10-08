package com.example.bookstore.service.book.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
class BookServiceImplTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 200L;
    private static final Pageable VALID_PAGEABLE = PageRequest.of(0, 10);
    private static final Category VALID_CATEGORY = new Category()
            .setId(VALID_ID)
            .setName("Fantasy");
    private static final Book VALID_BOOK = new Book()
            .setId(VALID_ID)
            .setTitle("Harry Potter")
            .setAuthor("J. K. Rowling")
            .setIsbn("921-3-ewfwefwef-148410-")
            .setPrice(BigDecimal.valueOf(50.00))
            .setCategories(Set.of(VALID_CATEGORY));

    private static final CreateBookRequestDto VALID_CREATE_BOOK_REQUEST_DTO =
            new CreateBookRequestDto()
                .setTitle("Harry Potter")
                .setAuthor("J. K. Rowling")
                .setIsbn("921-3-ewfwefwef-148410-")
                .setPrice(BigDecimal.valueOf(50.00))
                .setCategoryIds(Set.of(VALID_ID));
    private static final BookDto VALID_BOOK_DTO = new BookDto()
            .setId(VALID_ID)
            .setTitle("Harry Potter")
            .setAuthor("J. K. Rowling")
            .setIsbn("921-3-ewfwefwef-148410-")
            .setPrice(BigDecimal.valueOf(50.00))
            .setCategoryIds(Set.of(VALID_ID));
    private static final BookDtoWithoutCategoryIds VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS =
            new BookDtoWithoutCategoryIds()
                .setId(VALID_ID)
                .setTitle("Harry Potter")
                .setAuthor("J. K. Rowling")
                .setIsbn("921-3-ewfwefwef-148410-")
                .setPrice(BigDecimal.valueOf(50.00));

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Test the creation of Book from BookRequestDto")
    public void create_ValidBook_ShouldSaveNewBookToDB() {
        when(bookMapper.toModel(VALID_CREATE_BOOK_REQUEST_DTO)).thenReturn(VALID_BOOK);
        when(bookRepository.save(VALID_BOOK)).thenReturn(VALID_BOOK);
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_BOOK_DTO);

        BookDto actual = bookService.create(VALID_CREATE_BOOK_REQUEST_DTO);
        assertEquals(VALID_BOOK_DTO, actual);
        verify(bookRepository).save(VALID_BOOK);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Test retrieving all books from DB")
    public void getAll_ShouldReturnAllBooksDto() {
        when(bookRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(VALID_BOOK)));

        List<BookDtoWithoutCategoryIds> actual = bookService.getAll(VALID_PAGEABLE);

        assertEquals(1, actual.size());
        verify(bookRepository).findAll(VALID_PAGEABLE);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Test retrieving the book with existing id from DB")
    public void findById_ExistingId_ShouldReturnBookDto() {
        when(bookRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_BOOK));
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_BOOK_DTO);

        BookDto actual = bookService.findById(VALID_ID);

        assertEquals(VALID_BOOK_DTO, actual);
        verify(bookRepository).findById(VALID_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Test retrieving the book with not existing id from DB")
    public void findById_NonExistingId_ShouldThrowException() {
        when(bookRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(INVALID_ID));

        String expected = "Sorry! There is no book with id {" + INVALID_ID + "} in the DB!";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(bookRepository).findById(INVALID_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Test deleting a book by ID")
    public void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(bookRepository).deleteById(VALID_ID);
        bookService.deleteById(VALID_ID);

        verify(bookRepository).deleteById(VALID_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Test retrieving books by category ID")
    public void getBooksByCategoryId_ShouldReturnBooksDtoList() {
        when(bookRepository.findAllByCategoriesId(VALID_ID, VALID_PAGEABLE))
                .thenReturn(List.of(VALID_BOOK));

        when(bookMapper.toDtoWithoutCategories(VALID_BOOK))
                .thenReturn(VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS);

        List<BookDtoWithoutCategoryIds> actual = bookService
                .getBooksByCategoryId(VALID_ID, VALID_PAGEABLE);

        assertEquals(1, actual.size());
        verify(bookRepository).findAllByCategoriesId(VALID_ID, VALID_PAGEABLE);
        verify(bookMapper).toDtoWithoutCategories(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
    }
}
