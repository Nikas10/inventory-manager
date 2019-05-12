package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.dto.Bundle_InventoryPositionDTO;
import com.quartet.inventorydemo.exception.NotBundleException;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.RequirementValue;
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
  public Bundle_InventoryPosition add(
      @NotNull @Valid UUID bundleId,
      @NotNull @Valid UUID positionId,
      @NotNull Bundle_InventoryPositionDTO bundle_inventoryPositionDTO) {
    Optional<InventoryPosition> optionalBundle = positionService.getByPositionID(bundleId);
    Optional<InventoryPosition> optionalPosition = positionService.getByPositionID(positionId);

    InventoryPosition bundle = optionalBundle.orElseThrow(
        () -> new ResourceNotFoundException("Bundle with id: " + bundleId + " not found."));
    InventoryPosition position = optionalPosition.orElseThrow(
        () ->
            new ResourceNotFoundException("Position with id: " + positionId + " not found."));

    if (!bundle.isBundle()) {
      throw new NotBundleException("Inventory position with id: "
                                   + bundleId
                                   + " is not bundle.");
    }

    Optional<Bundle_InventoryPosition> optionalBundle_inventoryPosition =
        bundle_InventoryPositionRepo.findByInventoryPositionAndBundlePosition(position, bundle);

    if (optionalBundle_inventoryPosition.isPresent()) {
      throw new ResourceAlreadyExistsException(
          "Bundle with id:"
              + bundleId
              + " already has position with id: "
              + positionId
              + " .");
    }

    Bundle_InventoryPosition newBundleInventoryPosition =
        new Bundle_InventoryPosition(position, bundle, bundle_inventoryPositionDTO.getAmount());
    return bundle_InventoryPositionRepo.saveAndFlush(newBundleInventoryPosition);
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

    if (!bundle.isBundle()) {
      throw new NotBundleException("Inventory position with id: "
          + bundleId
          + " is not bundle.");
    }

    Optional<InventoryItem> optionalBundleItem = inventoryItemService.getByInventoryPositionIdInStorage(bundleId);
    if (optionalBundleItem.isPresent()) {
      InventoryItem bundleItem = optionalBundleItem.get();
      Integer amount = bundleItem.getAmount();
      inventoryItemService.unpackBundlesInStorage(bundleId, amount);
    }

    Optional<Bundle_InventoryPosition> bundlePositionOptional =
        bundle_InventoryPositionRepo.findByInventoryPositionAndBundlePosition(position, bundle);

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
  public void remove(@NotNull @Valid UUID bundleId, @NotNull @Valid UUID positionId) {

    Optional<InventoryPosition> optionalBundle = positionService.getByPositionID(bundleId);
    Optional<InventoryPosition> optionalPosition = positionService.getByPositionID(positionId);

    InventoryPosition bundle = optionalBundle.orElseThrow(
        () -> new ResourceNotFoundException("Bundle with id: " + bundleId + " not found."));
    InventoryPosition position = optionalPosition.orElseThrow(
        () ->
            new ResourceNotFoundException("Position with id: " + positionId + " not found."));

    if (!bundle.isBundle()) {
      throw new NotBundleException("Inventory position with id: "
          + bundleId
          + " is not bundle.");
    }

    Optional<InventoryItem> optionalBundleItem = inventoryItemService.getByInventoryPositionIdInStorage(bundleId);
    if (optionalBundleItem.isPresent()) {
      InventoryItem bundleItem = optionalBundleItem.get();
      Integer amount = bundleItem.getAmount();
      inventoryItemService.unpackBundlesInStorage(bundleId, amount);
    }

    Optional<Bundle_InventoryPosition> bundlePositionOptional =
        bundle_InventoryPositionRepo.findByInventoryPositionAndBundlePosition(position, bundle);

    Bundle_InventoryPosition bundlePosition = bundlePositionOptional.orElseThrow(() ->
        new ResourceNotFoundException("Bundle with id: "
                                      + bundleId
                                      + " does not contains position with id: "
                                      + positionId
                                      + " ."));

    bundle_InventoryPositionRepo.delete(bundlePosition);
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
