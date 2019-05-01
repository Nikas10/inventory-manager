package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryItem;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface InventoryItemService {

  Collection<InventoryItem> getAll();

  InventoryItem add(@NotNull @Valid InventoryItem item);

  InventoryItem update(@NotNull @Valid InventoryItem item);

  void remove(@NotNull @Valid InventoryItem item);
}
