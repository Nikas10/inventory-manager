package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.Bundle_InventoryPositionRepository;
import com.quartet.inventorydemo.service.Bundle_InventoryPositionService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import java.util.List;
import java.util.Optional;
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
  private final InventoryPositionService positionService;

  @Autowired
  public Bundle_InventoryPositionServiceImpl(
      @Qualifier("Bundle_InventoryPositionRepository") final Bundle_InventoryPositionRepository bundle_InventoryPositionRepo,
      @Qualifier("InventoryPositionService") final InventoryPositionService positionService) {
    this.bundle_InventoryPositionRepo = bundle_InventoryPositionRepo;
    this.positionService = positionService;
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
  public List<InventoryPosition> getBundleFirstLevelContents(@NotNull @Valid UUID bundleId) {
    Optional<InventoryPosition> bundle = positionService.getByPositionID(bundleId);
    InventoryPosition result = bundle.orElseThrow(
        () -> new ResourceNotFoundException("Requested bundle is not found!"));
    return result.getBundleInventoryPositions()
        .parallelStream()
        .map(Bundle_InventoryPosition::getInventoryPosition)
        .collect(Collectors.toList());
  }
}
