package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RequirementRepository extends JpaRepository<Requirement, UUID> {

    List<Requirement> findAll();

    Requirement findByRequirementID(UUID requirementID);
}
