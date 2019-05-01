package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.Bundle_InventoryPositionRepository;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.repository.InventoryItemRepository;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryPositionService;
import java.util.ArrayList;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service("InventoryPositionService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class InventoryPositionServiceImpl implements InventoryPositionService {

  private final InventoryPositionRepository positionRepo;
  private final InventoryItemRepository inventoryItemRepo;
  private final InventoryHolderRepository inventoryHolderRepo;
  private final Bundle_InventoryPositionRepository Bundle_InventoryPositionRepo;

  @Autowired
  public InventoryPositionServiceImpl(
      @Qualifier("InventoryPositionRepository") final InventoryPositionRepository positionRepo,
      @Qualifier("InventoryItemRepository") final InventoryItemRepository inventoryItemRepo,
      @Qualifier("InventoryHolderRepository") final InventoryHolderRepository inventoryHolderRepo,
      @Qualifier("Bundle_InventoryPositionRepository") final Bundle_InventoryPositionRepository Bundle_InventoryPositionRepo) {
    this.positionRepo = positionRepo;
    this.inventoryItemRepo = inventoryItemRepo;
    this.inventoryHolderRepo = inventoryHolderRepo;
    this.Bundle_InventoryPositionRepo = Bundle_InventoryPositionRepo;
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
  public InventoryPosition add(@NotNull @Valid InventoryPosition position) {
    if (isExists(position)) {
      throw new ResourceAlreadyExistsException(
          "Position with same name already exists. Can not make changes.");
    }

    InventoryPosition newPosition =
        new InventoryPosition(position.getName(), position.getDescription());
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
        Bundle_InventoryPositionRepo.findByInventoryPosition(position);

    for (Bundle_InventoryPosition bundle : bundlesWithThisPosition) {
      Bundle_InventoryPositionRepo.delete(bundle);
    }

    if (itemsWithSelectedPosition == null) {
      positionRepo.delete(position);
      return;
    }
  }

  private void removeBundle(InventoryPosition position) {
    // check if everyone in storage else throw exception
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
    inventoryItemRepo.saveAll(allItems);
    positionRepo.delete(
        position); // если нет каскадного удаления, то удалить отдельно inventory item и хуйню
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
