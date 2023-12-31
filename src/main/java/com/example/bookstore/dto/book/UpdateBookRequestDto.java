package com.example.bookstore.dto.book;

import com.example.bookstore.validation.author.Author;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
public class UpdateBookRequestDto {
    @NotNull
    @Length(min = 5, max = 200)
    private String title;
    @Author
    private String author;
    @ISBN
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private Set<Long> categoryIds;
    private String description;
    private String coverImage;
}
