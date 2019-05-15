package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import com.quartet.inventorydemo.repository.Requisition_InventoryPositionRepository;
import com.quartet.inventorydemo.service.Requisition_InventoryPositionService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service("Requisition_InventoryPositionService")
@Validated
@Transactional
public class Requisition_InventoryPositionServiceImpl implements
    Requisition_InventoryPositionService
{

  @Autowired
  Requisition_InventoryPositionRepository requisition_inventoryPositionRepo;

  @Override
  public Optional<Requisition_InventoryPosition> findByInventoryPosition_IdAndRequisition_Id(
      UUID requisition, UUID position) {
    return requisition_inventoryPositionRepo.findByInventoryPosition_IdAndRequisition_Id(position, requisition);
  }

  public void update(Requisition_InventoryPosition requisition_inventoryPosition) {
    requisition_inventoryPositionRepo.saveAndFlush(requisition_inventoryPosition);
  }
}
