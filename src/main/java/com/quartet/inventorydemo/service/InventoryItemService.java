package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryItem;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface InventoryItemService {

    Collection<InventoryItem> getAll();

    InventoryItem add(@NotNull @Valid InventoryItem item);

    InventoryItem update(@NotNull @Valid InventoryItem item);

    void remove(@NotNull @Valid InventoryItem item);
}
