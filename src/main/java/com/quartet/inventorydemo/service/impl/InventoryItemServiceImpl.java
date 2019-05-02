package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.repository.InventoryItemRepository;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.util.IdNotNull;
import com.quartet.inventorydemo.util.IdNull;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

  @Autowired
  public InventoryItemServiceImpl(
      @Qualifier("InventoryItemRepository") final InventoryItemRepository inventoryItemRepository) {
    this.inventoryItemRepository = inventoryItemRepository;
  }

  @Override
  public Set<InventoryItem> getAll() {
    return inventoryItemRepository.findAllToSet();
  }

  @Validated(IdNull.class)
  @Override
  public InventoryItem add(@NotNull @Valid InventoryItem item) {
    return inventoryItemRepository.saveAndFlush(item);
  }

  @Validated(IdNotNull.class)
  @Override
  public InventoryItem update(@NotNull @Valid InventoryItem item) {
    return inventoryItemRepository.saveAndFlush(item);
  }

  @Validated(IdNotNull.class)
  @Override
  public void remove(@NotNull @Valid InventoryItem item) {}
}
