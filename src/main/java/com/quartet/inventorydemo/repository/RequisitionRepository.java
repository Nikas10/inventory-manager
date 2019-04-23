package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RequisitionRepository extends JpaRepository<Requisition, UUID> {
    List<Requisition> findAll();
}
