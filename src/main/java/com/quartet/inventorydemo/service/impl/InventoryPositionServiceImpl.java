package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.dto.InventoryPositionDTO;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.exception.UpdateNotSupportedException;
import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.Bundle_InventoryPositionRepository;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
  public InventoryPosition add(@NotBlank @Valid String name, @NotNull @Valid String description,
      boolean isBundle) {

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

    if ((positionDTO.getDescription() != null) &&
        (!"".equals(positionDTO.getDescription())))
    {
      positionToModify.setDescription(
          positionDTO.getDescription());  //TODO check if string valid and throw exception
    }

    if ((positionDTO.getName() != null) &&
        (!"".equals(positionDTO.getName())))
    {
      positionToModify.setName(positionDTO.getName());  //TODO check uniqueness
    }

    if (positionDTO.getBundle() != null) {
      setBundle(positionToModify, positionDTO.getBundle());
    }

    return positionRepo.saveAndFlush(positionToModify);
  }

  private void setBundle(InventoryPosition positionToModify, Boolean bundle) {
    if (!positionToModify.isBundle()) {
      positionToModify.setBundle(bundle);
      return;
    }

    if (!positionToModify.getBundleInventoryPositions().isEmpty()) {
      throw new UpdateNotSupportedException("Bundle has assigned items!");
    }
    positionToModify.setBundle(bundle);
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

    Optional<InventoryItem> optionalBundleItem = inventoryItemService
        .getByInventoryPositionIdInStorage(position.getId());
    InventoryItem bundleItem = optionalBundleItem
        .orElseThrow(() -> new ResourceNotFoundException("Bundle with id: "
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
