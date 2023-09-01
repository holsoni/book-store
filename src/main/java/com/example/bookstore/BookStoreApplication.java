package com.example.bookstore;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {

    private final BookService bookService;

    @Autowired
    public BookStoreApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book bestBook = new Book();
            bestBook.setTitle("Sapiens");
            bestBook.setAuthor("Yuval Harari");
            bestBook.setIsbn("9780062316097");
            bestBook.setPrice(BigDecimal.valueOf(435));

            bookService.save(bestBook);
            bookService.findAll().forEach(System.out::println);
        };
    }
}
