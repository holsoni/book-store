package com.example.bookstore.dto.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
public class CategoryCreateRequestDto {
    @NotEmpty
    @Length(min = 3, max = 50)
    private String name;
    private String description;
}
