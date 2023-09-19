package com.example.bookstore.repository.order;

import com.example.bookstore.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getAllByOrderId(Long orderId, Pageable pageable);

    @Query("FROM OrderItem i WHERE i.id = :id "
            + "AND i.order.id = :orderId")
    Optional<OrderItem> findByIdAndOrderId(Long orderId, Long id);
}
