package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryItem;

import java.util.List;

public interface InventoryItemService {
    List<InventoryItem> getAll();
    InventoryItem add(InventoryItem item);
    InventoryItem update(InventoryItem item);
}
