package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.dto.AmountDTO;
import com.quartet.inventorydemo.exception.NotBundleException;
import com.quartet.inventorydemo.exception.NotEnoughItemsException;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.InventoryItemRepository;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service("InventoryItemService")
@Validated
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {

  private final InventoryItemRepository inventoryItemRepository;
  private final HolderService holderService;
  private final InventoryPositionService inventoryPositionService;

  @Autowired
  public InventoryItemServiceImpl(
      @Qualifier("InventoryItemRepository") final InventoryItemRepository inventoryItemRepository,
      @Qualifier("HolderService") final HolderService holderService,
      @Qualifier("InventoryPositionService") final InventoryPositionService inventoryPositionService) {
    this.inventoryItemRepository = inventoryItemRepository;
    this.holderService = holderService;
    this.inventoryPositionService = inventoryPositionService;
  }

  @Override
  public Set<InventoryItem> getAll() {
    return inventoryItemRepository.findAllToSet();
  }

  @Override
  public Optional<InventoryItem> getByInventoryPositionIdAndHolderId(
      @NotNull @Valid UUID inventoryPositionId, @NotNull @Valid UUID holderId) {
    return inventoryItemRepository
        .findByInventoryPosition_IdAndHolder_Id(inventoryPositionId, holderId);
  }

  @Override
  public InventoryItem add(@NotNull @Valid String holderName,
      @NotNull @Valid String positionName,
      @NotNull @Valid String status,
      @NotNull @Valid Integer amount) {

    Optional<Holder> optionalHolder = holderService.getByHolderName(holderName);
    Optional<InventoryPosition> optionalPosition = inventoryPositionService.getByName(positionName);

    Holder holder = optionalHolder.orElseThrow(
        () -> new ResourceNotFoundException("Holder with name: " + holderName + " not found."));
    InventoryPosition position = optionalPosition.orElseThrow(
        () -> new ResourceNotFoundException("Position with name: " + positionName + " not found."));

    Optional<InventoryItem> optionalItem = inventoryItemRepository
        .findByInventoryPosition_IdAndHolder_Id(position.getId(), holder.getId());

    optionalItem.orElseThrow(() -> new ResourceAlreadyExistsException(
        "Item with position id: " + position.getId() + " and holderId: " + holder.getId()
            + " already exists."));

    return inventoryItemRepository
        .saveAndFlush(new InventoryItem(holder, position, status, amount));
  }

  @Override
  public InventoryItem update(@NotNull @Valid String holderName,
      @NotNull @Valid String positionName,
      @NotNull @Valid String status,
      @NotNull @Valid Integer amount) {
    Optional<Holder> optionalHolder = holderService.getByHolderName(holderName);
    Optional<InventoryPosition> optionalPosition = inventoryPositionService.getByName(positionName);

    Holder holder = optionalHolder.orElseThrow(
        () -> new ResourceNotFoundException("Holder with name: " + holderName + " not found."));
    InventoryPosition position = optionalPosition.orElseThrow(
        () -> new ResourceNotFoundException("Position with name: " + positionName + " not found."));

    Optional<InventoryItem> optionalItem = inventoryItemRepository
        .findByInventoryPosition_IdAndHolder_Id(position.getId(), holder.getId());

    optionalItem.orElseThrow(() -> new ResourceAlreadyExistsException(
        "Item with position id: " + position.getId() + " and holderId: " + holder.getId()
            + " does not exist."));

    return inventoryItemRepository
        .saveAndFlush(new InventoryItem(holder, position, status, amount));
  }

  @Override
  public void remove(@NotNull @Valid UUID inventoryPositionId, @NotNull @Valid UUID holderId) {

  }

  @Override
  public Optional<InventoryItem> getByInventoryPositionIdInStorage(
      @NotNull @Valid UUID inventoryPositionId) {
    Holder storageHolder = holderService.getStorageHolder();
    Set<InventoryItem> inventoryItems = storageHolder.getInventoryItems();
    return inventoryItems.stream().filter(
        inventoryItem -> Objects
            .equals(inventoryItem.getInventoryPosition().getId(), inventoryPositionId))
        .findFirst();
  }

  @Override
  public InventoryItem moveFromHolderToStorage(@NotNull @Valid UUID inventoryPositionId,
      @NotNull @Valid UUID holderId, @NotNull @Valid AmountDTO amountDTO) {
    InventoryPosition inventoryPosition = getInventoryPositionOrThrowException(
        inventoryPositionId);
    Optional<InventoryItem> inventoryItemOptional = getByInventoryPositionIdAndHolderId(
        inventoryPositionId, holderId);
    InventoryItem holderInventoryItem = inventoryItemOptional.orElseThrow(
        () -> new ResourceNotFoundException(
            "Holder doesn't hold item with this inventory position"));
    Integer holderAmount = holderInventoryItem.getAmount();
    if (holderAmount < amountDTO.getAmount()) {
      throw new NotEnoughItemsException("Holder doesn't have enough items to move");
    }
    holderInventoryItem.setAmount(holderAmount - amountDTO.getAmount());

    Holder storageHolder = holderService.getStorageHolder();
    InventoryItem inventoryItemInStorage = getByInventoryPositionIdInStorage(inventoryPositionId)
        .map(e -> {
          e.setAmount(e.getAmount() + amountDTO.getAmount());
          return e;
        })
        .orElseGet(() -> new InventoryItem(storageHolder,
            inventoryPosition,
            "In storage", amountDTO.getAmount()));
    if (holderInventoryItem.getAmount() == 0) {
      inventoryItemRepository.delete(holderInventoryItem);
    }
    return inventoryItemRepository.saveAndFlush(inventoryItemInStorage);
  }


  @Override
  public InventoryItem moveFromStorageToHolder(@NotNull @Valid UUID inventoryPositionId,
      @NotNull @Valid UUID holderId, @NotNull @Valid AmountDTO amountDTO) {
    Holder holder = getHolderOrThrowException(holderId);
    InventoryPosition inventoryPosition = getInventoryPositionOrThrowException(
        inventoryPositionId);

    InventoryItem inventoryItemInStorage = getByInventoryPositionIdInStorage(inventoryPositionId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Storage doesn't hold item with this inventory position"));
    Integer amountInStorage = inventoryItemInStorage.getAmount();
    if (amountInStorage < amountDTO.getAmount()) {
      throw new NotEnoughItemsException("Storage doesn't contain enough items");
    }
    inventoryItemInStorage.setAmount(amountInStorage - amountDTO.getAmount());

    InventoryItem holderInventoryItem = getByInventoryPositionIdAndHolderId(inventoryPositionId,
        holderId)
        .map((e) -> {
          e.setAmount(e.getAmount() + amountDTO.getAmount());
          return e;
        })
        .orElseGet(() -> new InventoryItem(holder, inventoryPosition, "Holding",
            amountDTO.getAmount())); //TODO status
    if (inventoryItemInStorage.getAmount() == 0) {
      inventoryItemRepository.delete(inventoryItemInStorage);
    }
    return inventoryItemRepository.saveAndFlush(holderInventoryItem);
  }

  @Override
  public InventoryItem packBundlesInStorage(@NotNull @Valid UUID bundleInventoryPositionId,
      @Positive @Valid Integer amountOfBundles) {
    InventoryPosition bundleInventoryPosition = getInventoryPositionOrThrowException(
        bundleInventoryPositionId);
    Set<Bundle_InventoryPosition> partsOfBundleInventoryPosition = bundleInventoryPosition
        .getBundleInventoryPositions();
    if (!bundleInventoryPosition.isBundle()) {
      throw new NotBundleException("Trying to pack not a bundle inventory position");
    }

    Set<InventoryItem> inventoryItemsToSave = new HashSet<>();
    Set<InventoryItem> inventoryItemsToDelete = new HashSet<>();

    Holder storageHolder = holderService.getStorageHolder();
    Set<InventoryItem> inventoryItemsInStorage = storageHolder.getInventoryItems();

    //TODO FUCK YOU PISS OF SHIT. THIS IS SELECTS IN FOR LOOP. OPTIMIZE AND REFACTOR
    for (Bundle_InventoryPosition bundle_inventoryPosition : partsOfBundleInventoryPosition) {
      Integer amountOfItemsToFormBundle = bundle_inventoryPosition.getAmount();
      Integer amountOfItemsToFormAllBundles = amountOfItemsToFormBundle * amountOfBundles;

      InventoryPosition requriedInventoryPosition = bundle_inventoryPosition.getInventoryPosition();
      boolean foundByPositionInStorage = false;

      for (InventoryItem inventoryItemInStorage : inventoryItemsInStorage) {
        if (inventoryItemInStorage.getInventoryPosition().equals(requriedInventoryPosition)) {
          foundByPositionInStorage = true;
          if (inventoryItemInStorage.getAmount() < amountOfItemsToFormAllBundles) {
            throw new NotEnoughItemsException("Not enough items in storage to form all bundles");
          }
          inventoryItemInStorage
              .setAmount(inventoryItemInStorage.getAmount() - amountOfItemsToFormAllBundles);
          if (inventoryItemInStorage.getAmount() == 0) {
            inventoryItemsToDelete.add(inventoryItemInStorage);
          } else {
            inventoryItemsToSave.add(inventoryItemInStorage);
          }
          break;
        }
      }
      if (!foundByPositionInStorage) {
        throw new NotEnoughItemsException("Storage doesn't have all positions to form bundle");
      }
    }

    InventoryItem inventoryItemBundle = getByInventoryPositionIdInStorage(bundleInventoryPositionId)
        .map(e -> {
          e.setAmount(e.getAmount() + amountOfBundles);
          return e;
        })
        .orElseGet(() -> new InventoryItem(storageHolder,
            bundleInventoryPosition,
            "In storage", amountOfBundles));

    inventoryItemRepository.deleteAll(inventoryItemsToDelete);
    inventoryItemRepository.saveAll(inventoryItemsToSave);

    return inventoryItemRepository.saveAndFlush(inventoryItemBundle);
  }

  @Override
  public Set<InventoryItem> unpackBundlesInStorage(@NotNull @Valid UUID bundleInventoryPositionId,
      @Positive @Valid Integer amountOfBundles) {
    Holder storageHolder = holderService.getStorageHolder();
    InventoryPosition bundleInventoryPosition = getInventoryPositionOrThrowException(
        bundleInventoryPositionId);
    Set<Bundle_InventoryPosition> partsOfBundleInventoryPosition = bundleInventoryPosition
        .getBundleInventoryPositions();
    if (!bundleInventoryPosition.isBundle()) {
      throw new NotBundleException("Trying to unpack not a bundle inventory position");
    }
    InventoryItem bundleInStorageToUnpack = getByInventoryPositionIdInStorage(
        bundleInventoryPositionId)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                "Storage doesn't hold item with this inventory position")
        );
    if (bundleInStorageToUnpack.getAmount() < amountOfBundles) {
      throw new NotEnoughItemsException("Not enough bundles to unpack");
    }
    bundleInStorageToUnpack.setAmount(bundleInStorageToUnpack.getAmount() - amountOfBundles);

    Set<InventoryItem> inventoryItemsToSave = new HashSet<>();

    for (Bundle_InventoryPosition bundle_inventoryPosition : partsOfBundleInventoryPosition) {
      Integer amountOfItemsToReturnFromBundle = bundle_inventoryPosition.getAmount();
      Integer amountOfItemsToReturnFromAllBundles =
          amountOfItemsToReturnFromBundle * amountOfBundles;

      InventoryPosition requiredInventoryPosition = bundle_inventoryPosition.getBundlePosition();

      InventoryItem inventoryItem = getByInventoryPositionIdInStorage(
          requiredInventoryPosition.getId()).map(e -> {
        e.setAmount(e.getAmount() + amountOfItemsToReturnFromAllBundles);
        return e;
      }).orElseGet(() -> new InventoryItem(storageHolder,
          requiredInventoryPosition,
          "In storage", amountOfItemsToReturnFromAllBundles));
      inventoryItemsToSave.add(inventoryItem);
    }
    if (bundleInStorageToUnpack.getAmount() == 0) {
      inventoryItemRepository.delete(bundleInStorageToUnpack);
    }
    return new HashSet<>(inventoryItemRepository.saveAll(inventoryItemsToSave));
  }

  @Override
  public InventoryItem addToStorage(@NotNull @Valid UUID inventoryPositionId,
      @NotNull @Valid AmountDTO amountDTO) {
    InventoryPosition inventoryPosition = getInventoryPositionOrThrowException(
        inventoryPositionId);
    Holder storageHolder = holderService.getStorageHolder();
    InventoryItem inventoryItem = getByInventoryPositionIdInStorage(inventoryPosition.getId())
        .map(e -> {
          e.setAmount(e.getAmount() + amountDTO.getAmount());
          return e;
        })
        .orElseGet(() -> new InventoryItem(storageHolder, inventoryPosition, "In storage", amountDTO
            .getAmount()));
    return inventoryItemRepository.save(inventoryItem);
  }

  @Override
  public void removeFromStorage(@NotNull @Valid UUID inventoryPositionId,
      @NotNull @Valid AmountDTO amountDTO) {
    InventoryItem inventoryItem = getByInventoryPositionIdInStorage(inventoryPositionId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Inventory item not found in storage")
        );
    if (inventoryItem.getAmount() < amountDTO.getAmount()) {
      throw new NotEnoughItemsException("Can not delete more than storage contains");
    }
    inventoryItem.setAmount(inventoryItem.getAmount() - amountDTO.getAmount());
    if (inventoryItem.getAmount() == 0) {
      inventoryItemRepository.delete(inventoryItem);
    }
  }

  private Holder getHolderOrThrowException(
      @Valid @NotNull UUID holderId) {
    Optional<Holder> holderOptional = holderService.getByHolderID(holderId);
    return holderOptional.orElseThrow(
        () -> new ResourceNotFoundException(
            "inventory holder with id: " + holderId + "not found"));
  }

  private InventoryPosition getInventoryPositionOrThrowException(
      @NotNull @Valid UUID inventoryPositionId) {
    Optional<InventoryPosition> inventoryPositionOptional = inventoryPositionService
        .getByPositionID(inventoryPositionId);
    return inventoryPositionOptional.orElseThrow(
        () -> new ResourceNotFoundException(
            "Position with id: " + inventoryPositionId + " not found."));
  }
}
