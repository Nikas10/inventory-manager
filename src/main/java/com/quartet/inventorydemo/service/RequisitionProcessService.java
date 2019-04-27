package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface RequisitionProcessService {

    @Validated(OnUpdate.class)
    void create(@NotNull @Valid Requisition requisition);

    @Validated(OnUpdate.class)
    void update(@NotNull @Valid Requisition requisition);

    @Validated(OnUpdate.class)
    void delete(@NotNull @Valid Requisition requisition);
}