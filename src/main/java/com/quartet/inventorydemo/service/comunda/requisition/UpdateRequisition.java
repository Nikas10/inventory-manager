package com.quartet.inventorydemo.service.comunda.requisition;


import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.RequisitionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("UpdateRequisition")
public final class UpdateRequisition {
    @Autowired
    private RequisitionService requisitionService;

    public void setStatus(DelegateExecution execution, String status) {
        String idString = execution.getProcessBusinessKey();
        UUID id = UUID.fromString(idString);

        Requisition requisition = requisitionService.getById(id);
        requisition.setStatus(status);
        requisitionService.update(requisition);
    }
}
