package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Employee;
import com.quartet.inventorydemo.model.InventoryHolder;
import com.quartet.inventorydemo.repository.EmployeeRepository;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("EmployeeService")
public class EmployeeServiceImpl  implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepo;

    @Override
    public List<Employee> getAll() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee getByEmployeeID(UUID employeeID) {
        return employeeRepo.findByEmployeeID(employeeID);
    }

    @Override
    public Employee add(Employee holder) {
        holder.setHolderID(UUID.randomUUID());
        return employeeRepo.saveAndFlush(holder);
    }

    @Override
    public Employee update(Employee holder) {
        return employeeRepo.saveAndFlush(holder);
    }
}
