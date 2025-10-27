package com.canteen.repository;

import com.canteen.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByToken(int token);
    List<Order> findAllPending();
    void updateStatus(int token, boolean isComplete);
}