package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfiguration;
import com.example.bookstore.dto.shoppingcart.AddCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public interface CartItemMapper {

    @Mapping(target = "bookId", source = "cartItem.book.id")
    @Mapping(target = "bookTitle", source = "cartItem.book.title")
    CartItemDto toDto(CartItem cartItem);

    CartItem toEntity(AddCartItemRequestDto cartItemRequestDto);

    @AfterMapping
    default void setBook(@MappingTarget CartItem cartItem, AddCartItemRequestDto requestDto) {
        Book book = new Book();
        book.setId(requestDto.getBookId());
        cartItem.setBook(book);
    }
}
