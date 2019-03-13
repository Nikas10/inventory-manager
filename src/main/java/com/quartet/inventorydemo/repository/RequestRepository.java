package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Employee;
import com.quartet.inventorydemo.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    List<Request> findAll();
    Request findByRequestID(UUID requestID);
}
