package com.example.bookstore.service.shoppingcart.impl;

import com.example.bookstore.dto.shoppingcart.AddCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = getShoppingCartByUser(currentUser);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemDto addToCart(AddCartItemRequestDto requestDto, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = getShoppingCartByUser(currentUser);
        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void updateCartItem(Long id, UpdateCartItemRequestDto requestDto) {
        CartItem cartItemFromDb = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Sorry! There is no item in cart with id " + id));
        cartItemFromDb.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItemFromDb);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Transactional
    private ShoppingCart getShoppingCartByUser(User currentUser) {
        return shoppingCartRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(currentUser);
                    return shoppingCartRepository.save(newCart);
                });
    }
}
