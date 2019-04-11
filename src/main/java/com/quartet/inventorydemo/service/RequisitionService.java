package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequisitionService {
    List<Requisition> getAll();
    Optional<Requisition> get(UUID id);
    Requisition getByEmployeeID(UUID requestID);
    Requisition add(Requisition requisition);
    Requisition update(Requisition requisition);
}
