package com.ecommerce.inventoryservice.dto;

public record InventoryResponseDTO(
        Long id,
        String sku,
        Integer quantity,
        boolean inStock
) {

}
