package com.example.bookstore.dto.shoppingcart;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long bookId;
    private String title;
    private int quantity;
}
