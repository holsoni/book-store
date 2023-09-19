package com.example.bookstore.service.order;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.dto.order.PlaceOrderRequestDto;
import com.example.bookstore.dto.order.UpdateOrderStatusRequest;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface OrderService {
    void placeOrder(PlaceOrderRequestDto requestDto, Authentication authentication);

    List<OrderDto> getOrderHistory(Authentication authentication);

    void updateOrderStatus(Long id,
                               UpdateOrderStatusRequest requestDto,
                               Authentication authentication);

    List<OrderItemDto> getOrderItems(Long orderId, Authentication authentication);

    OrderItemDto getByIdAndOrderIdForCurrentUser(Long orderId, Long itemId,
                                        Authentication authentication);
}
