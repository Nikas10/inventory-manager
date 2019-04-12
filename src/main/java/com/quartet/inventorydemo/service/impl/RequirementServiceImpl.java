package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.repository.RequirementRepository;
import com.quartet.inventorydemo.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("RequirementService")
public class RequirementServiceImpl implements RequirementService {

    @Autowired
    RequirementRepository requirementRepo;

    @Override
    public List<Requirement> getAll() {
        return requirementRepo.findAll();
    }

    @Override
    public Requirement getByRequirementID(UUID employeeID) {
        return requirementRepo.findByRequirementID(employeeID);
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
}
