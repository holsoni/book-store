package com.example.bookstore.dto.order;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private String bookId;
    private int quantity;
}
