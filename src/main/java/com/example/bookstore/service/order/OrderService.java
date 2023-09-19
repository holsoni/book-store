package com.example.bookstore.service.order;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.dto.order.PlaceOrderRequestDto;
import com.example.bookstore.dto.order.UpdateOrderStatusRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    void placeOrder(PlaceOrderRequestDto requestDto, Authentication authentication);

    List<OrderDto> getOrderHistory(Authentication authentication,
                                   Pageable pageable);

    void updateOrderStatus(Long id,
                               UpdateOrderStatusRequest requestDto,
                               Authentication authentication);

    List<OrderItemDto> getOrderItems(Long orderId,
                                     Pageable pageable);

    OrderItemDto getByIdAndOrderId(Long orderId, Long itemId);
}
