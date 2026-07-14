package com.ecommerce.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDTO(

        @NotBlank(message = "Product name cannot be blank")
        String name,

        String description,

        @NotNull(message = "Product price cannot be null")
        @Positive(message = "Product price must be greater than zero")
        BigDecimal price
) {

}
