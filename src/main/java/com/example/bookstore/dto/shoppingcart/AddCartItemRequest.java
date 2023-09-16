package com.example.bookstore.dto.shoppingcart;

import lombok.Data;

@Data
public class AddCartItemRequest {
    private Long bookId;
    private int quantity;
}
