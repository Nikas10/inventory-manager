package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requirement;

import java.util.List;
import java.util.UUID;

public interface RequirementService {
    List<Requirement> getAll();
    Requirement getByRequirementID(UUID requirementID);
    Requirement add(Requirement requirement);
    Requirement update(Requirement requirement);
}
