package com.canteen.service;

import com.canteen.model.Order;
import com.canteen.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderSystemLogic {
    private final OrderRepository repository; 
    private final List<OrderUpdateListener> listeners;

    public OrderSystemLogic(OrderRepository repository) {
        this.repository = repository;
        this.listeners = new ArrayList<>();
    }

    public void addOrder(Order order) {
        Order persistedOrder = repository.save(order);
        System.out.println("Order added to DB: Token #" + persistedOrder.getToken() + " - " + persistedOrder.getItemName());
        notifyOrderAdded(persistedOrder);
    }

    public void markOrderAsComplete(int token) {
        repository.updateStatus(token, true);

        Optional<Order> orderToComplete = repository.findByToken(token);

        orderToComplete.ifPresent(order -> {
            System.out.println("Order marked complete in DB: Token #" + order.getToken());
            notifyOrderStatusChanged(order);
        });
    }

    public List<Order> getPendingOrders() {
        return repository.findAllPending();
    }

    public void addOrderUpdateListener(OrderUpdateListener listener) {
        listeners.add(listener);
    }

    public interface OrderUpdateListener {
        void orderAdded(Order order);
        void orderStatusChanged(Order order);
    }

    private void notifyOrderAdded(Order order) {
        for (OrderUpdateListener listener : listeners) {
            listener.orderAdded(order);
        }
    }

    private void notifyOrderStatusChanged(Order order) {
        for (OrderUpdateListener listener : listeners) {
            listener.orderStatusChanged(order);
        }
    }
}