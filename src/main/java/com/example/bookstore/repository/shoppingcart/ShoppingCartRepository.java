package com.example.bookstore.repository.shoppingcart;

import com.example.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.cartItems ci "
            + "LEFT JOIN FETCH ci.book "
            + "LEFT JOIN FETCH sc.user u WHERE u.id = :id ")
    Optional<ShoppingCart> findByUserId(Long id);
}
