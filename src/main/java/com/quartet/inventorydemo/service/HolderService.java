package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.dto.InventoryItemDTO;
import com.quartet.inventorydemo.model.Holder;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface HolderService {

  Collection<Holder> getAll();

  Optional<Holder> getByHolderID(@NotNull @Valid UUID holderID);

  Optional<Holder> getByHolderName(@NotBlank @Valid String holderName);

  Collection<Holder> getByHolderIDs(@NotNull @Valid Set<UUID> holderIDs);

  Holder getStorageHolder();

  Holder add(@NotNull @Valid String description, @NotNull @Valid String name);

  Holder update(
      @NotNull @Valid UUID uuid, @NotNull @Valid String description, @NotNull @Valid String name);

  void remove(@NotNull @Valid UUID uuid);

  Holder addRoles(@NotNull @Valid UUID holderId, @NotNull @Valid Set<UUID> roleIds);

  Holder removeRoles(@NotNull @Valid UUID holderId, @NotNull @Valid Set<UUID> roleIds);

  Collection<InventoryItemDTO> getHolderItems(@NotNull @Valid UUID holderId);
}
