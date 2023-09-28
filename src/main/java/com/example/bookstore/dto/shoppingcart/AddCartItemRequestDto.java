package com.example.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddCartItemRequestDto {
    @NotNull
    private Long bookId;
    @Min(1)
    private int quantity;
}
