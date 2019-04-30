package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.InventoryPosition;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface Bundle_InventoryPositionService {
    int getAmount(@NotNull @Valid InventoryPosition bundle, @NotNull @Valid InventoryPosition partOfInventoryPosition);

    Bundle_InventoryPosition update(@NotNull @Valid InventoryPosition bundle, @NotNull @Valid InventoryPosition partOfInventoryPosition, @NotNull @Valid int value);
}
