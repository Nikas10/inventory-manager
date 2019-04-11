package com.quartet.inventorydemo.service.comunda.requisition;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.RequisitionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

public final class MarkAsComplete implements JavaDelegate {
    @Autowired
    private RequisitionService requisitionService;

    @Override
    public void execute(DelegateExecution execution) {
        String idString = execution.getProcessBusinessKey();
        UUID id = UUID.fromString(idString);

        Optional<Requisition> requisition = requisitionService.get(id);

        requisition.ifPresent(req -> {
            req.setStatus("COMPLETED");
            requisitionService.update(req);
        });
    }
}
