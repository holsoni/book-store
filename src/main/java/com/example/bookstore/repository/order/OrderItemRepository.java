package com.example.bookstore.repository.order;

import com.example.bookstore.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getAllByOrderId(Long orderId);

    @Query("FROM OrderItem i WHERE i.id = :id "
            + "AND i.order.id = :orderId "
            + "AND i.order.user.id = :userId")
    Optional<OrderItem> findByIdAndOrderIdForCurrentUser(Long orderId, Long id, Long userId);
}
