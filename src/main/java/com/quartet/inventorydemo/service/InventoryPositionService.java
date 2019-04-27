package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface InventoryPositionService {

    Collection<InventoryPosition> getAll();

    InventoryPosition getByPositionID(@NotNull @Valid UUID positionID);

    InventoryPosition getByName(@NotBlank @Valid String name);

    Collection<InventoryPosition> getByPositionIDs(@NotNull @Valid Set<UUID> positionIDs);

    @Validated(OnCreate.class)
    InventoryPosition add(@NotNull @Valid InventoryPosition position);

    @Validated(OnUpdate.class)
    InventoryPosition update(@NotNull @Valid InventoryPosition position);

    @Validated(OnUpdate.class)
    void remove(@NotNull @Valid InventoryPosition inventoryPosition);
}
