package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryPosition;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface InventoryPositionService {

  Collection<InventoryPosition> getAll();

  Optional<InventoryPosition> getByPositionID(@NotNull @Valid UUID positionID);

  Optional<InventoryPosition> getByName(@NotBlank @Valid String name);

  Collection<InventoryPosition> getByPositionIDs(@NotNull @Valid Set<UUID> positionIDs);

  InventoryPosition add(@NotNull @Valid String name, @NotNull @Valid String description);

  InventoryPosition update(@NotNull @Valid UUID id, @NotNull @Valid InventoryPosition position);

  void remove(@NotNull @Valid UUID id);
}
