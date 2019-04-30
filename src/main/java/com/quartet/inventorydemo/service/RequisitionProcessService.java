package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface RequisitionProcessService {

    void create(@NotNull @Valid Requisition requisition);

    void update(@NotNull @Valid Requisition requisition);

    void delete(@NotNull @Valid Requisition requisition);
}