package com.example.bookstore.dto;

import com.example.bookstore.validation.Author;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class UpdateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    @Author
    private String author;
    @ISBN
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
