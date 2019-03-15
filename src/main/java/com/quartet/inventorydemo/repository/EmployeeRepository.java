package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Employee;
import com.quartet.inventorydemo.model.InventoryHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findAll();
    Employee findByEmployeeID(UUID employeeID);
}
