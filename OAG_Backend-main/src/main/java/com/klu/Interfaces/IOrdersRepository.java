package com.klu.Interfaces;

import com.klu.DTO.OrderDto;
import java.util.List;
import java.util.Optional;

public interface IOrdersRepository {

    List<OrderDto> getAllOrders();

    List<OrderDto> getPendingOrders(int userId);

    List<OrderDto> getOrdersByUserId(int userId);

    boolean updateCartQuantity(int newAmount, int orderId);

    Optional<OrderDto> getOrderById(int id);

    void addOrder(OrderDto order);

    void deleteOrder(int id);

    void updateOrder(OrderDto order);

    void updateStatus(int id);

    void save();
}