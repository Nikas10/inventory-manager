package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryPosition;

import java.util.List;
import java.util.UUID;

public interface InventoryPositionService {
    List<InventoryPosition> getAll();
    InventoryPosition getByPositionID(UUID positionID);
    InventoryPosition getByName(String name);
    InventoryPosition add(InventoryPosition position);
    InventoryPosition update(InventoryPosition position);
}
