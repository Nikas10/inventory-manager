package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.dto.RequisitionInventoryPositionDTO;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface Requisition_InventoryPositionService {

  Optional<Requisition_InventoryPosition> findByInventoryPosition_IdAndRequisition_Id(UUID requisition, UUID position);

  void update(@NotNull @Valid UUID requisitionId,
              @NotNull @Valid UUID positionId,
              @NotNull @Valid Integer amount);

  void remove(@NotNull @Valid UUID requisition, @NotNull @Valid UUID position);

  Collection<Requisition_InventoryPosition> addAll(@NotBlank @Valid Collection<Requisition_InventoryPosition> positionLinks);

  Collection<Requisition_InventoryPosition> addAllByInventory(@NotBlank @Valid Requisition requisitionToAdd,
      @NotBlank @Valid Collection<RequisitionInventoryPositionDTO> positionsToAdd);
}
