package com.example.bookstore.service.order.impl;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.dto.order.PlaceOrderRequestDto;
import com.example.bookstore.dto.order.UpdateOrderStatusRequest;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.Status;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.order.OrderItemRepository;
import com.example.bookstore.repository.order.OrderRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.service.order.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public void placeOrder(PlaceOrderRequestDto requestDto, Authentication authentication) {
        ShoppingCart shoppingCart = cartRepository.findByUserId(getCurrentUserId(authentication))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sorry! Can't find shopping cart for user with id "
                                + getCurrentUserId(authentication)));
        Order order = createOrder(requestDto, authentication, shoppingCart);
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getOrderItems());
    }

    @Override
    public List<OrderDto> getOrderHistory(Authentication authentication, Pageable pageable) {
        return orderRepository
                .getAllByUserId(getCurrentUserId(authentication), pageable).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(Long id,
                                  UpdateOrderStatusRequest requestDto) {
        Order orderFromDb = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sorry! Can't find order with id " + id));
        orderFromDb.setStatus(Arrays.stream(
                Status.values())
                .filter(status -> status.name().equals(requestDto.getStatus()))
                .findFirst()
                .orElse(Status.PROCESSING));
        orderRepository.save(orderFromDb);
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId,
                                            Pageable pageable) {
        return orderItemRepository.getAllByOrderId(orderId, pageable).stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDto getByIdAndOrderId(Long itemId, Long orderId) {
        return orderItemMapper.toDto(orderItemRepository.findByIdAndOrderId(
                        itemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sorry! Can't find order item with id  " + itemId
                        + ", order id " + orderId)));
    }

    private Order createOrder(PlaceOrderRequestDto requestDto,
                              Authentication authentication,
                              ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setUser((User) authentication.getPrincipal());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PROCESSING);
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setOrderItems(getOrderItemsFromCart(shoppingCart, order));
        order.setTotal(countTotal(order.getOrderItems()));
        return order;
    }

    private BigDecimal countTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private Set<OrderItem> getOrderItemsFromCart(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(orderItemMapper::toOrderItem)
                .peek(item -> item.setOrder(order))
                .collect(Collectors.toSet());
    }

    private Long getCurrentUserId(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }
}
