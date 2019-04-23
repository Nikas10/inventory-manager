package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requirement_InventoryPosition;

import java.util.List;
import java.util.UUID;

public interface Requirement_InventoryPositionService {

    Requirement_InventoryPosition changeRequirement(UUID positionID, UUID requirementID, String value);
    Requirement_InventoryPosition getByPositionIDAndRequirementID(UUID positionID, UUID requirementID);
    List<Requirement_InventoryPosition> getAll();
    void remove(Requirement_InventoryPosition requirement_inventoryPosition);
}
