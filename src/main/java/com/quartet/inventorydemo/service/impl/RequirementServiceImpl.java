package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.Requirement_InventoryPosition;
import com.quartet.inventorydemo.repository.RequirementRepository;
import com.quartet.inventorydemo.repository.Requirement_InventoryPositionRepository;
import com.quartet.inventorydemo.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("RequirementService")
public class RequirementServiceImpl implements RequirementService {

    @Autowired
    private RequirementRepository requirementRepo;

    @Autowired
    private Requirement_InventoryPositionRepository requirement_inventoryPositionRepo;

    @Override
    public List<Requirement> getAll() {
        return requirementRepo.findAll();
    }

    @Override
    public Requirement getByRequirementID(UUID employeeID) {
        return requirementRepo.findByRequirementID(employeeID);
    }

    @Override
    public Requirement getByRequirementName(String name) {
        return requirementRepo.findByName(name);
    }

    @Override
    public Requirement add(Requirement holder) {
        holder.setRequirementID(UUID.randomUUID());
        return requirementRepo.saveAndFlush(holder);
    }

    @Override
    public Requirement update(Requirement holder) {
        return requirementRepo.saveAndFlush(holder);
    }

    @Override
    public void remove(Requirement requirement) {
        List<Requirement_InventoryPosition> occurencesThisRequirement = requirement_inventoryPositionRepo.findByRequirement(requirement);

        for (Requirement_InventoryPosition currentOccurence: occurencesThisRequirement) {
            requirement_inventoryPositionRepo.delete(currentOccurence);
        }
    }
}
