package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.InventoryItemRepository;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.util.IdNotNull;
import com.quartet.inventorydemo.util.IdNull;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.swing.text.Position;
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
  private final InventoryPositionService positionService;

  @Autowired
  public InventoryItemServiceImpl(
      @Qualifier("InventoryItemRepository") final InventoryItemRepository inventoryItemRepository,
      @Qualifier("HolderService") final HolderService holderService,
      @Qualifier("InventoryPositionService") final InventoryPositionService positionService) {
    this.inventoryItemRepository = inventoryItemRepository;
    this.holderService = holderService;
    this.positionService = positionService;
  }

  @Override
  public Set<InventoryItem> getAll() {
    return inventoryItemRepository.findAllToSet();
  }

  @Validated(IdNull.class)
  @Override
  public InventoryItem add(@NotNull @Valid String holderName,
                           @NotNull @Valid String positionName,
                           @NotNull @Valid String status,
                           @NotNull @Valid Integer amount) {

    Optional<Holder> optionalHolder = holderService.getByHolderName(holderName);
    Optional<InventoryPosition> optionalPosition = positionService.getByName(positionName);

    Holder holder = optionalHolder.orElseThrow(() -> new ResourceNotFoundException("Holder with name: " + holderName + " not found."));
    InventoryPosition position = optionalPosition.orElseThrow(() -> new ResourceNotFoundException("Position with name: " + positionName + " not found."));

    Optional<InventoryItem> optionalItem = inventoryItemRepository.findByHolder_IdAndInventoryPosition_Id(holder.getId(), position.getId());

    optionalItem.ifPresent(optItem -> {throw new ResourceAlreadyExistsException("Item with position id: "
                                                                                + position.getId()
                                                                                + " and holderId: "
                                                                                + holder.getId()
                                                                                + " already exists.");}
    );

    return inventoryItemRepository.saveAndFlush(new InventoryItem(holder, position, status, amount));
  }

  @Validated(IdNotNull.class)
  @Override
  public InventoryItem update(@NotNull @Valid String holderName,
                              @NotNull @Valid String positionName,
                              @NotNull @Valid String status,
                              @NotNull @Valid Integer amount) {
    Optional<Holder> optionalHolder = holderService.getByHolderName(holderName);
    Optional<InventoryPosition> optionalPosition = positionService.getByName(positionName);

    Holder holder = optionalHolder.orElseThrow(() -> new ResourceNotFoundException("Holder with name: " + holderName + " not found."));
    InventoryPosition position = optionalPosition.orElseThrow(() -> new ResourceNotFoundException("Position with name: " + positionName + " not found."));

    Optional<InventoryItem> optionalItem = inventoryItemRepository.findByHolder_IdAndInventoryPosition_Id(holder.getId(), position.getId());

    optionalItem.orElseThrow(() -> {throw new ResourceAlreadyExistsException("Item with position id: "
        + position.getId()
        + " and holderId: "
        + holder.getId()
        + " does not exist.");}
    );

    return inventoryItemRepository.saveAndFlush(new InventoryItem(holder, position, status, amount));
  }

  @Validated(IdNotNull.class)
  @Override
  public void remove(@NotNull @Valid UUID uuid) {}
}
