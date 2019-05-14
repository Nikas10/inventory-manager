package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import java.util.Optional;
import java.util.UUID;

public interface Requisition_InventoryPositionService {

  Optional<Requisition_InventoryPosition> findByRequisitionAndInventoryPosition(Requisition requisition, InventoryPosition position);
  void update(Requisition_InventoryPosition requisition_inventoryPosition);
}
