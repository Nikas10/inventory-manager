package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryItem;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface InventoryItemService {

  Collection<InventoryItem> getAll();

  Optional<InventoryItem> getByInventoryPositionIdAndHolderId(
      @NotNull @Valid UUID inventoryPositionId, @NotNull @Valid UUID holderId);

  InventoryItem add(
      @NotNull @Valid UUID inventoryPositionId,
      @NotNull @Valid UUID holderId,
      @Positive @Valid Integer amount);

  InventoryItem update(
      @NotNull @Valid UUID inventoryPositionId,
      @NotNull @Valid UUID holderId,
      @Positive @Valid Integer amount);

  void remove(@NotNull @Valid UUID inventoryPositionId, @NotNull @Valid UUID holderId);

  Optional<InventoryItem> getByInventoryPositionIdInStorage(
      @NotNull @Valid UUID inventoryPositionId, @NotNull @Valid UUID holderId);

  InventoryItem moveFromHolderToStorage(
      @NotNull @Valid UUID inventoryPositionId,
      @NotNull @Valid UUID holderId,
      @Positive @Valid Integer amountToMove);

  InventoryItem moveFromStorageToHolder(
      @NotNull @Valid UUID inventoryPositionId,
      @NotNull @Valid UUID holderId,
      @Positive @Valid Integer amountToMove);

  InventoryItem packBundlesInStorage(
      @NotNull @Valid UUID bundleInventoryPositionId,
      @Positive @Valid Integer amountOfBundles);

  InventoryItem unpackBundlesInStorage(
      @NotNull @Valid UUID bundleInventoryPositionId,
      @Positive @Valid Integer amountOfBundles);

  InventoryItem addToStorage(
      @NotNull @Valid UUID inventoryPositionId,
      @Positive @Valid Integer amount);

  InventoryItem removeFromStorage(
      @NotNull @Valid UUID inventoryPositionId,
      @Positive @Valid Integer amount);
}
