package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;

public interface RequisitionProcessService {
    void create(Requisition requisition);
    void update(Requisition requisition);

    void approve(Requisition requisition);
    void reject(Requisition requisition);
    void requestClarification(Requisition requisition, String reason);

    void makeChanges(Requisition requisition);

    void complete(Requisition requisition);


}