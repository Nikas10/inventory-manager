package com.quartet.inventorydemo.service;

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

  void addAll(@NotBlank @Valid Collection<Requisition_InventoryPosition> positionLinks);
}
