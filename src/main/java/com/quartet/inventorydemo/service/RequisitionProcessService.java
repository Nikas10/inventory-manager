package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface RequisitionProcessService {

    void create(@NotNull @Valid Requisition requisition);
    void update(@NotNull @Valid Requisition requisition);
    void delete(@NotNull @Valid Requisition requisition);

    void approve(Requisition requisition);
    void reject(Requisition requisition);
    void requestClarification(Requisition requisition, String reason);

    void makeChanges(Requisition requisition);

    void complete(Requisition requisition);


}