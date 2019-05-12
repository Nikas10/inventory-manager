package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.dto.Bundle_InventoryPositionDTO;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.Bundle_InventoryPositionRepository;
import com.quartet.inventorydemo.service.Bundle_InventoryPositionService;
import com.quartet.inventorydemo.service.InventoryItemService;
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
  private final InventoryItemService inventoryItemService;

  @Autowired
  public Bundle_InventoryPositionServiceImpl(
      @Qualifier("Bundle_InventoryPositionRepository") final Bundle_InventoryPositionRepository bundle_InventoryPositionRepo,
      @Qualifier("InventoryPositionService") final InventoryPositionService positionService,
      @Qualifier("InventoryItemService") final InventoryItemService inventoryItemService) {
    this.bundle_InventoryPositionRepo = bundle_InventoryPositionRepo;
    this.positionService = positionService;
    this.inventoryItemService = inventoryItemService;
  }

  @Override
  public Integer getAmount(InventoryPosition bundle, InventoryPosition partOfInventoryPosition) {
    Optional<Bundle_InventoryPosition> optionalBundleContents =
        bundle_InventoryPositionRepo.findByInventoryPositionAndBundlePosition(
            bundle, partOfInventoryPosition);

    Bundle_InventoryPosition bundleContents = optionalBundleContents.orElseThrow(
        () ->
            new ResourceNotFoundException(
                "Bundle with id: "
                    + bundle.getId()
                    + " does not contains position with id: "
                    + partOfInventoryPosition.getId()
                    + " .")
    );

    return bundleContents.getAmount();
  }

  @Override
  public Bundle_InventoryPosition update(@NotNull @Valid UUID bundleId,
                                         @NotNull @Valid UUID positionId,
                                         @NotNull @Valid Bundle_InventoryPositionDTO bundle_inventoryPositionDTO) {

    Optional<InventoryPosition> optionalBundle = positionService.getByPositionID(bundleId);
    Optional<InventoryPosition> optionalPosition = positionService.getByPositionID(positionId);

    InventoryPosition bundle = optionalBundle.orElseThrow(
        () -> new ResourceNotFoundException("Bundle with id: " + bundleId + " not found."));
    InventoryPosition position = optionalPosition.orElseThrow(
        () ->
            new ResourceNotFoundException("Position with id: " + positionId + " not found."));

    Optional<Bundle_InventoryPosition> bundlePositionOptional =
        bundle_InventoryPositionRepo.findByInventoryPositionAndBundlePosition(position, bundle);

    Optional<InventoryItem> optionalBundleItem = inventoryItemService.getByInventoryPositionIdInStorage(bundleId);
    InventoryItem bundleItem = optionalBundleItem.orElseThrow(() -> new ResourceNotFoundException("Item of"));
    Integer amount = bundleItem.getAmount();
    inventoryItemService.unpackBundlesInStorage(bundleId, amount);

    Bundle_InventoryPosition toChange = bundlePositionOptional.orElseThrow(() ->
                                              new ResourceNotFoundException(
                                                  "Bundle with id: "
                                                      + bundleId
                                                      + " does not contains position with id: "
                                                      + positionId
                                                      + " ."));
    toChange.setAmount(bundle_inventoryPositionDTO.getAmount());
    return bundle_InventoryPositionRepo.saveAndFlush(toChange);
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
