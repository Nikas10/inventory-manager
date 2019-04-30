package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Holder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface HolderService {

    Collection<Holder> getAll();

    Optional<Holder> getByHolderID(@NotNull @Valid UUID holderID);

    Optional<Holder> getByHolderName(@NotBlank @Valid String holderName);

    Collection<Holder> getByHolderIDs(@NotNull @Valid Set<UUID> holderIDs);

    Holder add(@NotNull @Valid Holder holder);

    Holder update(@NotNull @Valid Holder holder);

    void remove(@NotNull @Valid Holder holder);
}
