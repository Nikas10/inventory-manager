package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.util.IdNull;
import com.quartet.inventorydemo.util.IdNotNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface RequisitionService {

    Collection<Requisition> getAll();
@Validated(IdNull.class)
    Optional<Requisition> getById(@NotNull @Valid UUID id);
    Requisition add(@NotNull @Valid Requisition requisition);
    Requisition update(@NotNull @Valid Requisition requisition);
    void remove(@NotNull @Valid Requisition requisition);
}
