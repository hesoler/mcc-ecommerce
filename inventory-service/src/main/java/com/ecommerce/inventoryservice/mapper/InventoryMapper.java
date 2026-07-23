package com.ecommerce.inventoryservice.mapper;

import com.ecommerce.inventoryservice.dto.InventoryRequestDTO;
import com.ecommerce.inventoryservice.dto.InventoryResponseDTO;
import com.ecommerce.inventoryservice.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "id", ignore = true)
    Inventory toEntity(InventoryRequestDTO inventoryRequestDTO);

    @Mapping(target = "inStock", expression = "java(inventory.getQuantity() > 0)")
    InventoryResponseDTO toResponseDTO(Inventory inventory);
}
