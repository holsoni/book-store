package com.example.bookstore.controller;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.dto.order.PlaceOrderRequestDto;
import com.example.bookstore.dto.order.UpdateOrderStatusRequest;
import com.example.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Controller", description = "API for managing orders")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public void placeOrder(@RequestBody PlaceOrderRequestDto requestDto,
                           Authentication authentication) {
        orderService.placeOrder(requestDto, authentication);
    }

    @GetMapping
    public List<OrderDto> getOrderHistory(Authentication authentication) {
        return orderService.getOrderHistory(authentication);
    }

    @PatchMapping("/{id}")
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody UpdateOrderStatusRequest requestDto,
                                  Authentication authentication) {
        orderService.updateOrderStatus(id, requestDto, authentication);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId,
                                            Authentication authentication) {
        return orderService.getOrderItems(orderId, authentication);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId,
                                         @PathVariable Long itemId,
                                         Authentication authentication) {
        return orderService.getByIdAndOrderIdForCurrentUser(orderId, itemId, authentication);
    }
}
