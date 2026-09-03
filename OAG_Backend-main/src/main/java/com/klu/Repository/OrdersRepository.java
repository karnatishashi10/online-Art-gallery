package com.klu.Repository;

import com.klu.DTO.OrderDto;
import com.klu.Interfaces.IOrdersRepository;
import com.klu.Models.Orders;
import com.klu.Models.Products;
import com.klu.Models.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class OrdersRepository implements IOrdersRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OrderDto> getAllOrders() {
        List<Orders> orders = entityManager.createQuery(
                "SELECT o FROM Orders o JOIN FETCH o.user u JOIN FETCH o.product p",
                Orders.class
        ).getResultList();

        return orders.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void updateOrder(OrderDto orderDto) {
        Orders order = entityManager.createQuery(
                "SELECT o FROM Orders o WHERE o.user.id = :userId AND o.product.id = :productId",
                Orders.class
        )
        .setParameter("userId", orderDto.getUserId())
        .setParameter("productId", orderDto.getProductId())
        .getResultStream().findFirst().orElse(null);

        if (order != null) {
            order.setAmount(order.getAmount() + 1);
            entityManager.merge(order);
        } else {
            throw new IllegalStateException("Order not found for the given user and product.");
        }
    }

    @Override
    public List<OrderDto> getPendingOrders(int userId) {
        List<Orders> orders = entityManager.createQuery(
                "SELECT o FROM Orders o JOIN FETCH o.user u JOIN FETCH o.product p " +
                        "WHERE o.user.id = :userId AND o.status = '0'",
                Orders.class
        ).setParameter("userId", userId).getResultList();

        return orders.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDto> getOrderById(int id) {
        Orders order = entityManager.find(Orders.class, id);
        return Optional.ofNullable(order).map(this::mapToDto);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(int userId) {
        List<Orders> orders = entityManager.createQuery(
                "SELECT o FROM Orders o JOIN FETCH o.user u JOIN FETCH o.product p " +
                        "WHERE o.user.id = :userId AND o.status = '1'",
                Orders.class
        ).setParameter("userId", userId).getResultList();

        return orders.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void addOrder(OrderDto orderDto) {
        Users user = entityManager.find(Users.class, orderDto.getUserId());
        Products product = entityManager.find(Products.class, orderDto.getProductId());

        if (user == null || product == null) {
            throw new IllegalArgumentException("User or Product not found.");
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setProduct(product);
        order.setDate(LocalDateTime.now());
        order.setAmount(orderDto.getAmount() > 0 ? orderDto.getAmount() : 1);
       
        order.setStatus("0");

        entityManager.persist(order);

        user.getOrders().add(order);
        product.getOrders().addAll((Collection<? extends OrderDto>) order);
    }

    @Override
    public void deleteOrder(int id) {
        Orders order = entityManager.find(Orders.class, id);
        if (order != null) {
            Users user = order.getUser();
            Products product = order.getProduct();

            entityManager.remove(order);

            if (user != null) user.getOrders().remove(order);
            if (product != null) product.getOrders().remove(order);
        }
    }

    @Override
    public void updateStatus(int userId) {
        List<Orders> orders = entityManager.createQuery(
                "SELECT o FROM Orders o WHERE o.user.id = :userId AND o.status = '0'",
                Orders.class
        ).setParameter("userId", userId).getResultList();

        for (Orders o : orders) {
            o.setStatus("1");
            entityManager.merge(o);
        }
    }

    @Override
    public boolean updateCartQuantity(int newAmount, int orderId) {
        Orders order = entityManager.find(Orders.class, orderId);
        if (order == null) return false;

        order.setAmount(newAmount);
        entityManager.merge(order);
        return true;
    }

    // Helper to map Orders -> OrderDto
    private OrderDto mapToDto(Orders o) {
        return new OrderDto(
        );
    }

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
}
