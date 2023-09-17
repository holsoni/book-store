package com.example.bookstore.service.shoppingcart.impl;

import com.example.bookstore.dto.shoppingcart.AddCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.service.cartitem.CartItemService;
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getShoppingCart() {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(getCurrentUserId())
                .orElseGet(this::create);
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        shoppingCartDto.setCartItems(shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet()));
        return shoppingCartDto;
    }

    @Override
    public CartItemDto addToCart(AddCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(getCurrentUserId())
                .orElseGet(this::create);

        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        return cartItemService.create(cartItem);
    }

    @Override
    public void updateCartItem(Long id, UpdateCartItemRequestDto requestDto) {
        cartItemService.update(id, requestDto);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemService.delete(id);
    }

    private ShoppingCart create() {
        ShoppingCart shoppingCart = new ShoppingCart();
        User currentUser = new User();
        currentUser.setId(getCurrentUserId());
        shoppingCart.setUser(currentUser);
        return shoppingCartRepository.save(shoppingCart);
    }

    private Long getCurrentUserId() {
        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return currentUser.getId();
    }
}
