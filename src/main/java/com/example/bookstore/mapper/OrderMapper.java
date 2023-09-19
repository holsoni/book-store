package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfiguration;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "order.user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderDto toDto(Order order);
}
