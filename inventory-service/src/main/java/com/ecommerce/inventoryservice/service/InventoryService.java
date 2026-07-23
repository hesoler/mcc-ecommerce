package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.InventoryResponseDTO;

public interface InventoryService {

    InventoryResponseDTO findBySku(String sku);

    boolean existBySku(String sku);

    boolean isInStock(String sku);

    void updateInventory(String sku, Integer quantity);

    void deleteInventory(Long id);
}
