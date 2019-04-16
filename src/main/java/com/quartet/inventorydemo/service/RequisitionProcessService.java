package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;

public interface RequisitionProcessService {
    void create(Requisition requisition);
    void update(Requisition requisition);
    void delete(Requisition requisition);
}