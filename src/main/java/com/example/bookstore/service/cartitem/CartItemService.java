package com.example.bookstore.service.cartitem;

import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.bookstore.model.CartItem;

public interface CartItemService {
    CartItemDto create(CartItem cartItem);

    void update(Long id, UpdateCartItemRequestDto requestDto);

    void delete(Long id);
}
