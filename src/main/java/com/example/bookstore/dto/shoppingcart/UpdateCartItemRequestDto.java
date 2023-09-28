package com.example.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateCartItemRequestDto {
    @Min(1)
    private int quantity;
}
