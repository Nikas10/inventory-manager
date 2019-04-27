package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;

public interface RequisitionService {

    Collection<Requisition> getAll();

    Requisition getById(@NotNull @Valid UUID id);

    @Validated(OnCreate.class)
    Requisition add(@NotNull @Valid Requisition requisition);

    @Validated(OnUpdate.class)
    Requisition update(@NotNull @Valid Requisition requisition);

    @Validated(OnUpdate.class)
    void remove(@NotNull @Valid Requisition requisition);
}
