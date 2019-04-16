package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requirement_InventoryPosition;

import java.util.UUID;

public interface Requirement_InventoryPositionService {

    Requirement_InventoryPosition changeRequirement(UUID positionID, UUID requirementID, String value);
}
