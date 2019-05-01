package com.quartet.inventorydemo.service.comunda;

import java.util.Map;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public final class LogVariables implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) {
    String id = execution.getProcessInstanceId();

    System.out.println("Variables for process " + id);

    Map<String, Object> variables = execution.getVariables();
    for (Map.Entry<String, Object> variable : variables.entrySet()) {
      String key = variable.getKey();
      String value = variable.getValue().toString();

      System.out.println(key + "=" + value);
    }
  }
}
