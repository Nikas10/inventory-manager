package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.InventoryPositionContents;

public interface InventoryPositionContentsService {

    int getAmount(InventoryPosition bundle, InventoryPosition partOfInventoryPosition);
    InventoryPositionContents change(InventoryPosition bundle, InventoryPosition partOfInventoryPosition, int value);
}
