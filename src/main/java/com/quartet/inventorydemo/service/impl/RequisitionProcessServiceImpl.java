package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import com.quartet.inventorydemo.util.OnUpdate;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Service("RequisitionProcessService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class RequisitionProcessServiceImpl implements RequisitionProcessService {
    private final static String PROCESS_NAME = "requisitionExecution";
    private final static String DUE_DATE = "dueDate";

    private final RuntimeService runtimeService;

    @Autowired
    public RequisitionProcessServiceImpl(final RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Validated(OnUpdate.class)
    @Override
    public void create(@NotNull @Valid Requisition requisition) {
        String businessKey = requisition.getId().toString();
        Date dueDate = requisition.getDueDate();

        ProcessInstance process = runtimeService.startProcessInstanceByKey(PROCESS_NAME, businessKey);
        runtimeService.setVariable(process.getProcessInstanceId(), DUE_DATE, dueDate);
    }

    @Validated(OnUpdate.class)
    @Override
    public void update(@NotNull @Valid Requisition requisition) {
        String businessKey = requisition.getId().toString();
        Date dueDate = requisition.getDueDate();

        ProcessInstance process = getByBusinessKey(businessKey);

        runtimeService.setVariable(process.getProcessInstanceId(), DUE_DATE, dueDate);
    }

    @Validated(OnUpdate.class)
    @Override
    public void delete(@NotNull @Valid Requisition requisition) {

    }

    private ProcessInstance getByBusinessKey(String key) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(key)
                .list()
                .get(0);
    }
}
