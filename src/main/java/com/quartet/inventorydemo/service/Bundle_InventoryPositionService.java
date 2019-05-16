package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.dto.BundlePartsDTO;
import com.quartet.inventorydemo.dto.Bundle_InventoryPositionDTO;
import com.quartet.inventorydemo.model.InventoryPosition;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface Bundle_InventoryPositionService {

  Integer getAmount(
      @NotNull @Valid InventoryPosition bundle,
      @NotNull @Valid InventoryPosition partOfInventoryPosition);

  BundlePartsDTO update(
      @NotNull @Valid UUID bundleId,
      @NotNull @Valid UUID positionId,
      @NotNull @Valid Bundle_InventoryPositionDTO bundle_inventoryPositionDTO);

  BundlePartsDTO add(
      @NotNull @Valid UUID bundleId,
      @NotNull @Valid UUID positionId,
      @NotNull Bundle_InventoryPositionDTO bundle_inventoryPositionDTO);

  void remove(
      @NotNull @Valid UUID bundleId,
      @NotNull @Valid UUID positionId);

  List<InventoryPosition> getBundleFirstLevelContents(
      @NotNull @Valid UUID bundle
  );
}
