package com.quartet.inventorydemo.service.comunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


public final class PrintHello implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        String name = (String) execution.getVariable("name");

        if (name == null) {
            name = "Stranger";
        }

        System.out.println("Hello, " + name + "!");

        for (String variableName : execution.getVariableNames()) {
            System.out.println(variableName);
        }
    }
}
