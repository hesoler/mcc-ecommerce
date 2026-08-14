package com.ecommerce.inventoryservice.service.impl;

import com.ecommerce.inventoryservice.dto.InventoryRequestDTO;
import com.ecommerce.inventoryservice.dto.InventoryResponseDTO;
import com.ecommerce.inventoryservice.exception.ResourceNotFoundException;
import com.ecommerce.inventoryservice.mapper.InventoryMapper;
import com.ecommerce.inventoryservice.model.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import com.ecommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> findAllInventories() {
        return inventoryRepository.findAll().stream().map(inventoryMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponseDTO findBySku(String sku) {
        return inventoryRepository.findAll().stream()
                .filter(inventory -> inventory.getSku().equals(sku))
                .findFirst()
                .map(inventoryMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "sku", sku));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existBySku(String sku) {
        return inventoryRepository.findBySku(sku).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInStock(String sku, Integer quantity) {
        return inventoryRepository.findBySku(sku)
                .map(inventory -> inventory.getQuantity() >= quantity)
                .orElse(false);
    }

    @Override
    @Transactional
    public InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO) {

        if (existBySku(inventoryRequestDTO.sku())) {
            throw new IllegalArgumentException("Inventory with SKU " + inventoryRequestDTO.sku() + " already exists.");
        }

        Inventory inventory = inventoryMapper.toEntity(inventoryRequestDTO);
        inventory = inventoryRepository.save(inventory);

        log.info("Inventory created for SKU: {}", inventory.getSku());
        return inventoryMapper.toResponseDTO(inventory);
    }

    @Override
    @Transactional
    public InventoryResponseDTO updateInventory(Long id, InventoryRequestDTO inventoryRequestDTO) {

        return inventoryRepository.findById(id)
                .map(inventory -> {
                    inventory.setSku(inventoryRequestDTO.sku());
                    inventory.setQuantity(inventoryRequestDTO.quantity());
                    inventoryRepository.save(inventory);
                    log.info("Inventory updated for ID: {}", inventory.getId());
                    return inventoryMapper.toResponseDTO(inventory);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id.toString()));
    }

    @Override
    @Transactional
    public void deleteInventory(Long id) {
        if (inventoryRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Inventory", "id", id.toString());
        }

        inventoryRepository.deleteById(id);
    }

    @Override
    public void reduceStock(String sku, Integer quantity) {
        Inventory inventory = inventoryRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "sku", sku));

        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for SKU: " + sku);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
        log.info("Stock reduced for SKU: {}. New quantity: {}", sku, inventory.getQuantity());
    }

}
