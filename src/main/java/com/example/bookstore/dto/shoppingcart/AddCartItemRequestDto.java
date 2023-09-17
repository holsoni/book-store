package com.example.bookstore.dto.shoppingcart;

import lombok.Data;

@Data
public class AddCartItemRequestDto {
    private Long bookId;
    private int quantity;
}
