package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfiguration;
import com.example.bookstore.dto.shoppingcart.AddCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.model.CartItem;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class, uses = BookMapper.class)
public interface CartItemMapper {

    @Mapping(target = "bookId", source = "cartItem.book.id")
    @Mapping(target = "title", source = "cartItem.book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toEntity(AddCartItemRequestDto cartItemRequestDto);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toEntity(CartItemDto dto);

    @Named("itemFromId")
    default CartItem bookFromId(Long id) {
        return Optional.ofNullable(id)
                .map(CartItem::new)
                .orElse(null);
    }
}
