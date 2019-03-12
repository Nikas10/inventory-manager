package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    List<Employee> getAll();
    Employee getByEmployeeID(UUID employeeID);
    Employee add(Employee employee);
    Employee update(Employee employee);
}
