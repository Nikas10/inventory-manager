package com.quartet.inventorydemo.service.comunda.requisition;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("GenerateReport")
public final class GenerateReport implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) {
    // TODO: Сгенерировать отчет
  }
}
