package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.dto.InventoryItemDTO;
import com.quartet.inventorydemo.model.InventoryItem;
import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface InventoryItemService {

  Collection<InventoryItem> getAll();

  InventoryItem add(@NotNull @Valid String holderName,
                    @NotNull @Valid String positionName,
                    @NotNull @Valid String status,
                    @NotNull @Valid Integer amount);

  InventoryItem update(@NotNull @Valid String holderName,
                       @NotNull @Valid String positionName,
                       @NotNull @Valid String status,
                       @NotNull @Valid Integer amount);

  void remove(@NotNull @Valid UUID UUID);
}
