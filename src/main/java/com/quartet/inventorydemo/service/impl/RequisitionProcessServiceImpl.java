package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("RequisitionProcessService")
public final class RequisitionProcessServiceImpl implements RequisitionProcessService {
    private final static String PROCESS_NAME = "requisitionExecution";
    private final static String DUE_DATE = "dueDate";

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void create(Requisition requisition) {
        String businessKey = requisition.getRequestID().toString();
        Date dueDate = requisition.getDueDate();

        ProcessInstance process = runtimeService.startProcessInstanceByKey(PROCESS_NAME, businessKey);
        runtimeService.setVariable(process.getProcessInstanceId(), DUE_DATE, dueDate);
    }

    @Override
    public void update(Requisition requisition) {
        String businessKey = requisition.getRequestID().toString();
        Date dueDate = requisition.getDueDate();

        ProcessInstance process = getByBusinessKey(businessKey);

        runtimeService.setVariable(process.getProcessInstanceId(), DUE_DATE, dueDate);
    }

    @Override
    public void delete(Requisition requisition) {

    }

    private ProcessInstance getByBusinessKey(String key) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(key)
                .list()
                .get(0);
    }
}
