package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.Bundle_InventoryPositionRepository;
import com.quartet.inventorydemo.service.Bundle_InventoryPositionService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service("Bundle_InventoryPositionService")
@Validated
@Transactional
public class Bundle_InventoryPositionServiceImpl implements Bundle_InventoryPositionService {

  private final Bundle_InventoryPositionRepository bundle_InventoryPositionRepo;

  @Autowired
  public Bundle_InventoryPositionServiceImpl(
      @Qualifier("Bundle_InventoryPositionRepository") final Bundle_InventoryPositionRepository bundle_InventoryPositionRepo) {
    this.bundle_InventoryPositionRepo = bundle_InventoryPositionRepo;
  }

  @Override
  public Integer getAmount(InventoryPosition bundle, InventoryPosition partOfInventoryPosition) {
    Bundle_InventoryPosition inventoryPositionContents =
        bundle_InventoryPositionRepo.findByInventoryPositionAndBundlePosition(
            bundle, partOfInventoryPosition);

    return inventoryPositionContents.getAmount();
  }

  @Override
  public Bundle_InventoryPosition update(
      InventoryPosition inventoryPosition, InventoryPosition bundlePosition, Integer value) {
    Bundle_InventoryPosition inventoryPositionContents =
        bundle_InventoryPositionRepo.findByInventoryPositionAndBundlePosition(
            inventoryPosition, bundlePosition);

    if (inventoryPositionContents == null) {
      inventoryPositionContents =
          new Bundle_InventoryPosition(inventoryPosition, bundlePosition, value);
    } else {
      inventoryPositionContents.setAmount(value);
    }

    return bundle_InventoryPositionRepo.saveAndFlush(inventoryPositionContents);
  }

  @Override
  public List<UUID> getBundleFirstLevelContents(@NotNull @Valid InventoryPosition bundle) {
    return bundle_InventoryPositionRepo.findByBundlePosition(bundle)
        .parallelStream()
        .map(Bundle_InventoryPosition::getInventoryPosition)
        .map(InventoryPosition::getId)
        .collect(Collectors.toList());
  }
}
