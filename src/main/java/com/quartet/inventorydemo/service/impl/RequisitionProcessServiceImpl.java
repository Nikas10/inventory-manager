package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("RequisitionProcessService")
public final class RequisitionProcessServiceImpl implements RequisitionProcessService {

  private static final String PROCESS_NAME = "requisitionExecution";

  private static final String MESSAGE_REVIEW = "Message_Review";
  private static final String MESSAGE_COMPLETE = "Message_Complete";
  private static final String MESSAGE_MAKE_CHANGES = "Message_MakeChanges";

  private static final String VARIABLE_REVIEW_RESULT = "reviewResult";
  private static final String VARIABLE_DUE_DATE = "dueDate";

  private static final String VALUE_APPROVE = "APPROVE";
  private static final String VALUE_REJECT = "REJECT";
  private static final String VALUE_REQUIRE_CLARIFICATION = "REQUIRE_CLARIFICATION";

  @Autowired
  private RuntimeService runtimeService;

  @Override
  public void create(Requisition requisition) {
    String businessKey = requisition.getId().toString();
    Date dueDate = requisition.getDueDate();

    ProcessInstance process = runtimeService.startProcessInstanceByKey(PROCESS_NAME, businessKey);
    runtimeService.setVariable(process.getProcessInstanceId(), VARIABLE_DUE_DATE, dueDate);
  }

  @Override
  public void update(Requisition requisition) {
    Date dueDate = requisition.getDueDate();

    ProcessInstance process = getProcessFor(requisition);

    runtimeService.setVariable(process.getProcessInstanceId(), VARIABLE_DUE_DATE, dueDate);
  }

  @Override
  public void approve(Requisition requisition) {
    ProcessInstance process = getProcessFor(requisition);

    Map<String, Object> variables = new HashMap<>();
    variables.put(VARIABLE_REVIEW_RESULT, VALUE_APPROVE);

    runtimeService.correlateMessage(MESSAGE_REVIEW, process.getBusinessKey(), variables);
  }

  @Override
  public void reject(Requisition requisition) {
    ProcessInstance process = getProcessFor(requisition);

    Map<String, Object> variables = new HashMap<>();
    variables.put(VARIABLE_REVIEW_RESULT, VALUE_REJECT);

    runtimeService.correlateMessage(MESSAGE_REVIEW, process.getBusinessKey(), variables);
  }

  @Override
  public void requestClarification(Requisition requisition, String reason) {
    ProcessInstance process = getProcessFor(requisition);

    Map<String, Object> variables = new HashMap<>();
    variables.put(VARIABLE_REVIEW_RESULT, VALUE_REQUIRE_CLARIFICATION);

    runtimeService.correlateMessage(MESSAGE_REVIEW, process.getBusinessKey(), variables);
  }

  @Override
  public void makeChanges(Requisition requisition) {
    ProcessInstance process = getProcessFor(requisition);

    runtimeService.correlateMessage(MESSAGE_MAKE_CHANGES, process.getBusinessKey());
  }

  @Override
  public void complete(Requisition requisition) {
    ProcessInstance process = getProcessFor(requisition);

    runtimeService.correlateMessage(MESSAGE_COMPLETE, process.getBusinessKey());
  }

  @Override
  public void delete(@NotNull @Valid Requisition requisition) {
  }

  private ProcessInstance getProcessFor(Requisition requisition) {
    String businessKey = requisition.getId().toString();

    return runtimeService
        .createProcessInstanceQuery()
        .processInstanceBusinessKey(businessKey)
        .list()
        .get(0);
  }
}
