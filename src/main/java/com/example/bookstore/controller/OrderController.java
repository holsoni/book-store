package com.example.bookstore.controller;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.dto.order.PlaceOrderRequestDto;
import com.example.bookstore.dto.order.UpdateOrderStatusRequest;
import com.example.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    @Operation(summary = "Place an order", description = "Place a new order")
    public void placeOrder(@RequestBody PlaceOrderRequestDto requestDto,
                           Authentication authentication) {
        orderService.placeOrder(requestDto, authentication);
    }

    @GetMapping
    @Operation(summary = "Get order history",
            description = "Get the order history for the current user")
    public List<OrderDto> getOrderHistory(Authentication authentication,
                                          Pageable pageable) {
        return orderService.getOrderHistory(authentication, pageable);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update order status",
            description = "Update the status of an existing order")
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody UpdateOrderStatusRequest requestDto,
                                  Authentication authentication) {
        orderService.updateOrderStatus(id, requestDto, authentication);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items", description = "Get the items for a specific order")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId,
                                            Pageable pageable) {
        return orderService.getOrderItems(orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item by ID",
            description = "Get a specific order item by its ID")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId,
                                         @PathVariable Long itemId) {
        return orderService.getByIdAndOrderId(orderId, itemId);
    }
}
