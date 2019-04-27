package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface HolderService {

    Collection<Holder> getAll();

    Holder getByHolderID(@NotNull @Valid UUID holderID);

    Collection<Holder> getByHolderIDs(@NotNull @Valid Set<UUID> holderIDs);

    Holder getByHolderName(@NotBlank @Valid String holderName);

    @Validated(OnCreate.class)
    Holder add(@NotNull @Valid Holder holder);

    @Validated(OnUpdate.class)
    Holder update(@NotNull @Valid Holder holder);

    @Validated(OnUpdate.class)
    void remove(@NotNull @Valid Holder holder);
}
