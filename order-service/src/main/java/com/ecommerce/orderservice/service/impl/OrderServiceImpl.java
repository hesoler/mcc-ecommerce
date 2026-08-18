package com.ecommerce.orderservice.service.impl;

import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.mapper.OrderMapper;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.OrderService;
import com.ecommerce.orderservice.service.client.InventoryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryClient inventoryClient;

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {

        log.info("In placeOrder");
        Order order = orderMapper.toEntity(orderRequest);

        for (var item : order.getOrderLineItemsList()) {

            String sku = item.getSku();
            Integer quantity = item.getQuantity();

            try {
                // Check inventory service for stock availability
                inventoryClient.reduceStock(sku, quantity);

            } catch (Exception e) {
                log.error("Error when reducing stock for SKU {}: {}", sku, e.getMessage());
                throw new RuntimeException("Cannot place order. Insufficient stock for SKU: " + sku);
            }
        }

        order.setOrderNumber(UUID.randomUUID().toString());
        orderRepository.save(order);
        log.info("Order placed successfully with order number: {}", order.getOrderNumber());
        return orderMapper.toResponseDTO(order);
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        return orderMapper.toResponseDTO(orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId.toString())));
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponseDTO)
                .toList();
    }

    @Override
    public void deleteOrder(Long orderId) {

        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order", "id", orderId.toString());
        }
        orderRepository.deleteById(orderId);
        log.info("Order deleted successfully with id: {}", orderId);
    }
}
