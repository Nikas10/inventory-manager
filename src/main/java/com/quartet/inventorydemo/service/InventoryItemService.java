package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface InventoryItemService {

    Collection<InventoryItem> getAll();

    @Validated(OnCreate.class)
    InventoryItem add(@NotNull @Valid InventoryItem item);

    @Validated(OnUpdate.class)
    InventoryItem update(@NotNull @Valid InventoryItem item);

    @Validated(OnUpdate.class)
    void remove(@NotNull @Valid InventoryItem item);
}
