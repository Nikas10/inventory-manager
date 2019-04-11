package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryItem;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface InventoryItemService {
    List<InventoryItem> getAll();
    InventoryItem getByInventoryItemID(UUID inventoryItemID);

    Set<InventoryItem> getByInventoryItemIDs(Set<UUID> itemIDs);

    InventoryItem add(InventoryItem item);
    InventoryItem update(InventoryItem item);
}
