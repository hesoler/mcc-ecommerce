package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.InventoryRequestDTO;
import com.ecommerce.inventoryservice.dto.InventoryResponseDTO;

import java.util.List;

public interface InventoryService {

    List<InventoryResponseDTO> findAllInventories();

    InventoryResponseDTO findBySku(String sku);

    boolean existBySku(String sku);

    InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO);

    boolean isInStock(String sku, Integer quantity);

    InventoryResponseDTO updateInventory(Long id, InventoryRequestDTO inventoryRequestDTO);

    void deleteInventory(Long id);
}
