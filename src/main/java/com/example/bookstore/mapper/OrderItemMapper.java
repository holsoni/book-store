package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfiguration;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class, uses = BookMapper.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "orderItem.book.id")
    OrderItemDto toDto(OrderItem orderItem);
}
