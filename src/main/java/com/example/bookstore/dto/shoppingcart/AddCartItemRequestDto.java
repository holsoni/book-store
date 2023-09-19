package com.example.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AddCartItemRequestDto {
    private Long bookId;
    @Min(1)
    private int quantity;
}
