package com.ecommerce.inventoryservice.service.impl;

import com.ecommerce.inventoryservice.dto.InventoryResponseDTO;
import com.ecommerce.inventoryservice.exception.ResourceNotFoundException;
import com.ecommerce.inventoryservice.mapper.InventoryMapper;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import com.ecommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private InventoryRepository inventoryRepository;
    private InventoryMapper inventoryMapper;

    @Override
    public InventoryResponseDTO findBySku(String sku) {
        return inventoryRepository.findAll().stream()
                .filter(inventory -> inventory.getSku().equals(sku))
                .findFirst()
                .map(inventoryMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "sku", sku));
    }

    @Override
    public boolean existBySku(String sku) {
        return false;
    }

    @Override
    public boolean isInStock(String sku) {
        return false;
    }

    @Override
    public void updateInventory(String sku, Integer quantity) {

    }

    @Override
    public void deleteInventory(Long id) {
        if (!inventoryRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Inventory", "id", id.toString());
        }

        inventoryRepository.deleteById(id);
    }

}
