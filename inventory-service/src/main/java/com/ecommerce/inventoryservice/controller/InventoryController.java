package com.ecommerce.inventoryservice.controller;

import com.ecommerce.inventoryservice.dto.InventoryRequestDTO;
import com.ecommerce.inventoryservice.dto.InventoryResponseDTO;
import com.ecommerce.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/api/v1/inventory/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku") String sku, @RequestParam("quantity") Integer quantity) {
        return inventoryService.isInStock(sku, quantity);
    }

    @GetMapping("/api/v1/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDTO> findAll() {
        return inventoryService.findAllInventories();
    }

    @PostMapping("/api/v1/inventory")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDTO createInventory(@RequestBody InventoryRequestDTO inventoryRequestDTO) {
        return inventoryService.createInventory(inventoryRequestDTO);
    }

    @PutMapping("/api/v1/inventory/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDTO updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryRequestDTO inventoryRequestDTO) {
        return inventoryService.updateInventory(id, inventoryRequestDTO);
    }

    @PutMapping("/api/v1/inventory/reduce/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public String reduceStock(@PathVariable String sku, @RequestParam Integer quantity) {
        inventoryService.reduceStock(sku, quantity);
        return "Stock reduced successfully for SKU: " + sku + " by quantity: " + quantity;
    }

    @DeleteMapping("/api/v1/inventory/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
    }


}
