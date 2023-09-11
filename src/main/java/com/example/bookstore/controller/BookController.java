package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.dto.book.UpdateBookRequestDto;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Online book store", description = "API for managing books")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get all books from the store")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create new books", description = "Add new book to the store")
    public BookDto create(@RequestBody CreateBookRequestDto requestDto) {
        return bookService.create(requestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find book by id", description = "Find book by id")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete book by id", description = "Delete book by id")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update book by id", description = "Update book by id")
    public BookDto update(@PathVariable Long id,
                              @RequestBody UpdateBookRequestDto updateBookRequestDto) {
        return bookService.update(id, updateBookRequestDto);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search book using dynamic parameters")
    public List<BookDto> searchBooks(BookSearchParameters searchParameters) {
        return bookService.search(searchParameters);
    }
}
