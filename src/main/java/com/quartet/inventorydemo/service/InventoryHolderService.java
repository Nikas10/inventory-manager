package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryHolder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface InventoryHolderService {
    List<InventoryHolder> getAll();
    InventoryHolder getByHolderID(UUID holderID);

    Set<InventoryHolder> getByHolderIDs(Set<UUID> holderIDs);

    List<InventoryHolder> getByHolderName(String holderName);

    InventoryHolder add(InventoryHolder holder);
    InventoryHolder update(InventoryHolder holder);

    void remove(InventoryHolder holder);
}
