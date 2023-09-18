package com.example.bookstore.service.shoppingcart;

import com.example.bookstore.dto.shoppingcart.AddCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(Authentication authentication);

    CartItemDto addToCart(AddCartItemRequestDto requestDto, Authentication authentication);

    void updateCartItem(Long id, UpdateCartItemRequestDto requestDto);

    void deleteCartItem(Long id);
}
