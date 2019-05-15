package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.exception.UpdateNotSupportedException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import com.quartet.inventorydemo.repository.Requisition_InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequisitionService;
import com.quartet.inventorydemo.service.Requisition_InventoryPositionService;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service("Requisition_InventoryPositionService")
@Validated
@Transactional
public class Requisition_InventoryPositionServiceImpl implements
    Requisition_InventoryPositionService
{

  private final Requisition_InventoryPositionRepository requisition_inventoryPositionRepo;
  private final RequisitionService requisitionService;
  private final InventoryPositionService positionService;

  @Autowired
  public Requisition_InventoryPositionServiceImpl(
      @Qualifier("Requisition_InventoryPositionRepository") final Requisition_InventoryPositionRepository requisition_inventoryPositionRepo,
      @Qualifier("RequisitionService") final RequisitionService requisitionService,
      @Qualifier("InventoryPositionService") final InventoryPositionService positionService) {
    this.requisition_inventoryPositionRepo = requisition_inventoryPositionRepo;
    this.requisitionService = requisitionService;
    this.positionService = positionService;
  }

  @Override
  public Optional<Requisition_InventoryPosition> findByRequisitionAndInventoryPosition(
      Requisition requisition, InventoryPosition position) {
    return requisition_inventoryPositionRepo.findByInventoryPositionAndRequisition(position, requisition);
  }

  public void update(@NotNull @Valid UUID requisitionId,
      @NotNull @Valid UUID positionId,
      @NotNull @Valid Integer amount) {

    Requisition requisition = requisitionService.getById(requisitionId);
    InventoryPosition position = positionService.getByPositionID(positionId).orElseThrow(
        () -> new ResourceNotFoundException("Position with id " + positionId + " is not found!"));
    Optional<Requisition_InventoryPosition> validation = requisition
        .getRequisitionInventoryPositions()
        .stream()
        .filter(e -> e.getInventoryPosition().equals(position))
        .findFirst();

    if (validation.isPresent()) {
      validation.get().setAmount(amount);
      requisition_inventoryPositionRepo.saveAndFlush(validation.get());
    } else {
      throw new UpdateNotSupportedException("Trying to add an already existing link!");
    }
  }
}
