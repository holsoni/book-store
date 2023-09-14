package com.example.bookstore.dto.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryCreateRequestDto {
    @NotEmpty
    @Length(min = 3, max = 50)
    private String name;
    private String description;
}
