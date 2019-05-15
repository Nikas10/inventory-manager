package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import java.util.Optional;
import java.util.UUID;

public interface Requisition_InventoryPositionService {

  Optional<Requisition_InventoryPosition> findByInventoryPosition_IdAndRequisition_Id(UUID requisition, UUID position);
  void update(Requisition_InventoryPosition requisition_inventoryPosition);
}
