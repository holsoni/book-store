package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParameters;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.dto.UpdateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto create(CreateBookRequestDto createBookRequestDto);

    List<BookDto> getAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, UpdateBookRequestDto updateBookRequestDto);

    List<BookDto> search(BookSearchParameters searchParameters);
}
