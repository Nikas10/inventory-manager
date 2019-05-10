package com.quartet.inventorydemo.service.impl;

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

  /*@Autowired
  public void setInventoryItemService(InventoryItemService inventoryItemService) {
    this.inventoryItemService = inventoryItemService;
  }*/

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
  public InventoryPosition add(@NotNull @Valid String name, @NotNull @Valid String description) {

    Optional<InventoryPosition> optionalPosition = getByName(name);

    if (optionalPosition.isPresent()) {
      throw new ResourceAlreadyExistsException("Position with name: " + name + " already exists");
    }

    InventoryPosition newPosition = new InventoryPosition(name, description);
    return positionRepo.saveAndFlush(newPosition);
  }

  @Override
  public InventoryPosition update(
      @NotNull @Valid UUID id, @NotNull @Valid InventoryPosition position) {
    Optional<InventoryPosition> holderOptional = getByPositionID(id);
    if (isExists(position)) {
      throw new ResourceAlreadyExistsException(
          "Position with same name already exists. Can not make changes.");
    }

    InventoryPosition positionToModify =
        holderOptional.orElseThrow(
            () -> new ResourceNotFoundException("Position with id: " + id + " not found"));

    BeanUtils.copyProperties(position, positionToModify, "id", "requirementValues");
    return positionRepo.saveAndFlush(position);
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

    Optional<InventoryItem> optionalBundleItem = inventoryItemService.getByInventoryPositionIdInStorage(position.getId());
    InventoryItem bundleItem = optionalBundleItem.orElseThrow(() -> new ResourceNotFoundException("Bundle with id: "
        + position.getId() + " does not exist as item."));
    inventoryItemService.unpackBundlesInStorage(position.getId(), bundleItem.getAmount());

    for (Bundle_InventoryPosition currentRecord: position.getBundleInventoryPositions()) {
      bundle_InventoryPositionRepo.delete(currentRecord);
    }

    positionRepo.delete(position);
    /*
    Set<Bundle_InventoryPosition> bundleInventoryPositions = position.getBundleInventoryPositions();
    List<InventoryItem> allItems = new ArrayList<>();
    Holder stockHolder = inventoryHolderRepo.findByName("stock").get();

    for (Bundle_InventoryPosition partOfBundle : bundleInventoryPositions) {
      Integer amount = partOfBundle.getAmount();
      InventoryPosition partOfInventoryPosition = partOfBundle.getInventoryPosition();
      Set<InventoryItem> currentPositionItems = partOfInventoryPosition.getInventoryItems();
      boolean existsInStorage = false;
      for (InventoryItem inventoryItem : currentPositionItems) {
        Holder inventoryHolder = inventoryItem.getHolder();
        if (stockHolder.equals(inventoryHolder)) {
          existsInStorage = true;
          inventoryItem.setAmount(amount + inventoryItem.getAmount());
          break;
        }
      }
      if (!existsInStorage) {

        InventoryItem inventoryItem =
            new InventoryItem(stockHolder, partOfInventoryPosition, "On stock", amount);
        // repo inventory item save this shit
        allItems.add(inventoryItem);
      }
    }
    inventoryItemService.saveAll(allItems);
    positionRepo.delete(
        position); // если нет каскадного удаления, то удалить отдельно inventory item и хуйню
    */
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
