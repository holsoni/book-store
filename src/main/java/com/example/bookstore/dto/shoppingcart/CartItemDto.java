package com.example.bookstore.dto.shoppingcart;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemDto {
    private Long id;
    private Long bookId;
    private String title;
    private int quantity;
}
