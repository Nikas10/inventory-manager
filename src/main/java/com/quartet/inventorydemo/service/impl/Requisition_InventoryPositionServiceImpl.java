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
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
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
      @Lazy @Qualifier("RequisitionService") final RequisitionService requisitionService,
      @Qualifier("InventoryPositionService") final InventoryPositionService positionService) {
    this.requisition_inventoryPositionRepo = requisition_inventoryPositionRepo;
    this.requisitionService = requisitionService;
    this.positionService = positionService;
  }

  @Override
  public Optional<Requisition_InventoryPosition> findByInventoryPosition_IdAndRequisition_Id(
      UUID requisition, UUID position) {
    return requisition_inventoryPositionRepo.findByInventoryPosition_IdAndRequisition_Id(position, requisition);
  }

  @Override
  public void update(@NotNull @Valid UUID requisitionId,
                     @NotNull @Valid UUID positionId,
                     @NotNull @Valid Integer amount) {

    Optional<Requisition_InventoryPosition> validation =
        requisition_inventoryPositionRepo.findByInventoryPosition_IdAndRequisition_Id(positionId, requisitionId);

    if (validation.isPresent()) {
      validation.get().setAmount(amount);
      requisition_inventoryPositionRepo.saveAndFlush(validation.get());
    } else {
      throw new UpdateNotSupportedException("Trying to add an already existing link!");
    }
  }

  @Override
  public void remove(@NotNull @Valid UUID requisitionId, @NotNull @Valid UUID positionId) {
    requisition_inventoryPositionRepo
        .delete(requisition_inventoryPositionRepo
                .findByInventoryPosition_IdAndRequisition_Id(positionId, requisitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position with id: "
                    + positionId
                    + " for requisition with id: "
                    + requisitionId
                    + " not found.")
                )
        );
  }
}
