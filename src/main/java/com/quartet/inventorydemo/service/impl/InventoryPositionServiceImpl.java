package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.dto.InventoryPositionDTO;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.Bundle_InventoryPositionRepository;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service("InventoryPositionService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class InventoryPositionServiceImpl implements InventoryPositionService {

  private final InventoryPositionRepository positionRepo;
  private final InventoryItemService inventoryItemService;
  private final Bundle_InventoryPositionRepository bundle_InventoryPositionRepo;

  @Autowired
  public InventoryPositionServiceImpl(
      @Qualifier("InventoryPositionRepository") final InventoryPositionRepository positionRepo,
      @Lazy @Qualifier("InventoryItemService") final InventoryItemService inventoryItemService,
      @Qualifier("Bundle_InventoryPositionRepository") final Bundle_InventoryPositionRepository bundle_InventoryPositionRepo) {
    this.positionRepo = positionRepo;
    this.inventoryItemService = inventoryItemService;
    this.bundle_InventoryPositionRepo = bundle_InventoryPositionRepo;
  }

  @Override
  public List<InventoryPosition> getAll() {
    return positionRepo.findAll();
  }

  @Override
  public Optional<InventoryPosition> getByPositionID(@NotNull @Valid UUID positionID) {
    return positionRepo.findById(positionID);
  }

  @Override
  public Optional<InventoryPosition> getByName(@NotBlank @Valid String name) {
    return positionRepo.findByName(name);
  }

  @Override
  public Set<InventoryPosition> getByPositionIDs(@NotNull @Valid Set<UUID> positionIDs) {
    return positionRepo.findByIdIn(positionIDs);
  }

  @Override
  public InventoryPosition add(@NotBlank @Valid String name, @NotNull @Valid String description, boolean isBundle) {

    Optional<InventoryPosition> optionalPosition = getByName(name);

    if (optionalPosition.isPresent()) {
      throw new ResourceAlreadyExistsException("Position with name: " + name + " already exists");
    }

    InventoryPosition newPosition = new InventoryPosition(name, description, isBundle);
    return positionRepo.saveAndFlush(newPosition);
  }

  @Override
  public InventoryPosition update(
      @NotNull @Valid UUID id, @NotNull @Valid InventoryPositionDTO positionDTO) {
    Optional<InventoryPosition> optionalPosition = getByPositionID(id);

    InventoryPosition positionToModify =
        optionalPosition.orElseThrow(
            () -> new ResourceNotFoundException("Position with id: " + id + " not found"));

    if ((!"".equals(positionDTO.getDescription())) &&
        (positionDTO.getDescription() != null)) {
      positionToModify.setDescription(positionDTO.getDescription());
    }

    if ((!"".equals(positionDTO.getName())) &&
        (positionDTO.getName() != null)) {
      positionToModify.setName(positionDTO.getName());
    }

    if ((!"".equals(positionDTO.getIsBundle())) &&
        (positionDTO.getIsBundle() != null)) {
      switch (positionDTO.getIsBundle()) {
        case "true":
          positionToModify.setBundle(true);
          break;
        case "false":
          positionToModify.setBundle(false);
          break;
        default:
          break;
      }

    }

    return positionRepo.saveAndFlush(positionToModify);
  }

  @Override
  public void remove(@NotNull @Valid UUID id) {
    Optional<InventoryPosition> optionalInventoryPosition = positionRepo.findById(id);
    InventoryPosition positionToRemove =
        optionalInventoryPosition.orElseThrow(
            () -> new ResourceNotFoundException("Position with id: " + id + " not found."));

    if (!positionToRemove.getBundleInventoryPositions().isEmpty()) {
      // inventoryPosition is bundle
      removeBundle(positionToRemove);
    } else {
      // inventoryPosition is not bundle
      removeInventoryPosition(positionToRemove);
    }
  }

  @Override
  public InventoryPosition setBundle(@NotNull @Valid UUID id, @NotNull @Valid Boolean value) {
    InventoryPosition position = getByPositionID(id).orElseThrow(
      () -> new ResourceNotFoundException("Position with id: " + id + " not found"));
    if (position.isBundle() && !value) {
      if (position.getBundleInventoryPositions().isEmpty())
        position.setBundle(value);
      else throw new IllegalStateException("Bundle has assigned items!");
    } else {
      position.setBundle(value);
    }
    return position;
  }

  private void removeInventoryPosition(InventoryPosition position) {
    Optional<InventoryPosition> itemsWithSelectedPosition = positionRepo.findById(position.getId());

    List<Bundle_InventoryPosition> bundlesWithThisPosition =
        bundle_InventoryPositionRepo.findByInventoryPosition(position);

    for (Bundle_InventoryPosition bundle : bundlesWithThisPosition) {
      bundle_InventoryPositionRepo.delete(bundle);
    }

    if (itemsWithSelectedPosition == null) {
      positionRepo.delete(position);
      return;
    }
  }

  private void removeBundle(InventoryPosition position) {
    // check if everyone in storage else throw exception

    Optional<InventoryItem> optionalBundleItem = inventoryItemService.getByInventoryPositionIdInStorage(position.getId());
    InventoryItem bundleItem = optionalBundleItem.orElseThrow(() -> new ResourceNotFoundException("Bundle with id: "
        + position.getId() + " does not exist as item."));
    inventoryItemService.unpackBundlesInStorage(position.getId(), bundleItem.getAmount());

    positionRepo.delete(position);
  }

  private boolean isExists(@NotNull @Valid InventoryPosition position) {
    ExampleMatcher uniqueMatcher =
        ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
            .withIgnorePaths(
                "id",
                "description",
                "roles",
                "requisitionInventoryPositions",
                "inventoryItems",
                "bundleInventoryPositions");
    Example<InventoryPosition> accountExample = Example.of(position, uniqueMatcher);
    Optional<InventoryPosition> alreadyExists = positionRepo.findOne(accountExample);

    return alreadyExists.isPresent();
  }
}
