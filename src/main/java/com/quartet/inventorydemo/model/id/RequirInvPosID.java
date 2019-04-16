package com.quartet.inventorydemo.model.id;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;

import java.io.Serializable;

public class RequirInvPosID implements Serializable {

    private Requirement requirement;
    private InventoryPosition inventoryPosition;

    public RequirInvPosID(Requirement requirement, InventoryPosition inventoryPosition) {
        this.requirement = requirement;
        this.inventoryPosition = inventoryPosition;
    }

}
