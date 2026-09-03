package com.klu.Controllers;

import com.klu.DTO.OrderDto;
import com.klu.DTO.UpdateCartDTO;
import com.klu.Interfaces.IOrdersRepository;
import com.klu.Interfaces.IProductRepository;
import com.klu.Interfaces.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final IOrdersRepository repository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    @Autowired
    public OrdersController(IOrdersRepository repository,
                            IProductRepository productRepository,
                            IUserRepository userRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/GetAllOrders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> allOrders = repository.getAllOrders();
        return ResponseEntity.ok(allOrders);
    }

    @GetMapping("/GetPendingOrders/{userId}")
    public ResponseEntity<List<OrderDto>> getPendingOrders(@PathVariable int userId) {
        List<OrderDto> pendingOrders = repository.getPendingOrders(userId);
        return ResponseEntity.ok(pendingOrders);
    }

    @GetMapping("/GetOrderById/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable int id) {
        Optional<OrderDto> order = repository.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/AddOrder")
    public ResponseEntity<?> addOrder(@RequestBody OrderDto orderData) {
        if (orderData == null) {
            return ResponseEntity.badRequest().body("{ \"Message\": \"Order data is null.\" }");
        }

        OrderDto order = new OrderDto();
        order.setUserId(orderData.getUserId());
        order.setProductId(orderData.getProductId());
        order.setDate(LocalDateTime.now()); // corrected line
        order.setStatus("0");

        try {
            List<OrderDto> pendingOrders = repository.getPendingOrders(orderData.getUserId());
            Optional<OrderDto> existingOrder = pendingOrders.stream()
                    .filter(o -> o.getProductId() == orderData.getProductId())
                    .findFirst();

            if (existingOrder.isPresent()) {
                repository.updateOrder(existingOrder.get());
            } else {
                repository.addOrder(order);
            }

            return ResponseEntity.ok("{ \"Message\": \"Order added successfully!\" }");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(
                    "{ \"Message\": \"An error occurred while adding the order.\", \"Details\": \"" + ex.getMessage() + "\" }"
            );
        }
    }

    @DeleteMapping("/DeleteOrder/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable int id) {
        Optional<OrderDto> order = repository.getOrderById(id);
        if (order.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteOrder(id);
        return ResponseEntity.ok("{ \"Message\": \"Order deleted successfully!\" }");
    }

    @PutMapping("/UpdateStatus/{userId}")
    public ResponseEntity<?> updateStatus(@PathVariable int userId) {
        repository.updateStatus(userId);
        return ResponseEntity.ok("{ \"Message\": \"Order status updated to 1.\" }");
    }

    @GetMapping("/GetOrdersByUserId/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable int userId) {
        List<OrderDto> userOrders = repository.getOrdersByUserId(userId);
        if (userOrders == null || userOrders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userOrders);
    }

    @PutMapping("/UpdateCartQuantity")
    public ResponseEntity<?> updateCartQuantity(@RequestBody UpdateCartDTO request) {
        boolean success = repository.updateCartQuantity(request.getNewAmount(), request.getOrderId());
        if (!success) {
            return ResponseEntity.status(404)
                    .body("{ \"Success\": false, \"Message\": \"Order not found.\" }");
        }
        return ResponseEntity.ok("{ \"Success\": true, \"Message\": \"Cart updated successfully!\" }");
    }
}
