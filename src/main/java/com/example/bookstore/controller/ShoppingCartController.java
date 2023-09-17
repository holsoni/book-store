package com.example.bookstore.controller;

import com.example.bookstore.dto.shoppingcart.AddCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart Controller",
        description = "API for managing logged user's shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Retrieve Shopping Cart",
            description = "Get the user's shopping cart.")
    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add Item to Shopping Cart",
            description = "Add a new item to the user's shopping cart.")
    @PostMapping
    public CartItemDto addItemToCart(@RequestBody AddCartItemRequestDto requestDto) {
        return shoppingCartService.addToCart(requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update Cart Item",
            description = "Update the quantity of an item in the user's shopping cart.")
    @PutMapping("/cart-items/{id}")
    public void updateCartItem(@PathVariable Long id,
                               @RequestBody UpdateCartItemRequestDto requestDto) {
        shoppingCartService.updateCartItem(id, requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete Cart Item",
            description = "Remove an item from the user's shopping cart.")
    @DeleteMapping("/cart-items/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        shoppingCartService.deleteCartItem(id);
    }
}
