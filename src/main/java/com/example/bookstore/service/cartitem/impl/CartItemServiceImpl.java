package com.example.bookstore.service.cartitem.impl;

import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.service.cartitem.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemDto create(CartItem cartItem) {
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void update(Long id, UpdateCartItemRequestDto requestDto) {
        CartItem cartItemFromDb = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Sorry! There is no item in cart with id " + id));
        cartItemFromDb.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItemFromDb);
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}
