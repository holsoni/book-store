package com.example.bookstore.service.shoppingcart;

import com.example.bookstore.dto.shoppingcart.AddCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart();

    CartItemDto addToCart(AddCartItemRequestDto requestDto);

    void updateCartItem(Long id, UpdateCartItemRequestDto requestDto);

    void deleteCartItem(Long id);
}
