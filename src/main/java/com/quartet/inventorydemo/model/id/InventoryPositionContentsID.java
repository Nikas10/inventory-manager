package com.quartet.inventorydemo.model.id;

import com.quartet.inventorydemo.model.InventoryPosition;

import java.io.Serializable;

public class InventoryPositionContentsID implements Serializable {

    private InventoryPosition bundle;
    private InventoryPosition partOfInventoryPosition;

    public InventoryPositionContentsID(InventoryPosition bundle, InventoryPosition partOfInventoryPosition) {
        this.bundle = bundle;
        this.partOfInventoryPosition = partOfInventoryPosition;
    }
}
