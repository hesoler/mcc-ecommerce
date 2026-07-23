package com.ecommerce.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record InventoryRequestDTO(

        @NotBlank(message = "SKU cannot be blank")
        String sku,

        @Min(value = 0, message = "Quantity cannot be negative")
        Integer quantity) {

}
