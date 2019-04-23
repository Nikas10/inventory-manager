package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.repository.RequirementRepository;
import com.quartet.inventorydemo.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("RequirementService")
public class RequirementServiceImpl implements RequirementService {

    @Autowired
    private RequirementRepository requirementRepo;

    @Override
    public List<Requirement> getAll() {
        return requirementRepo.findAll();
    }

    @Override
    public Requirement getByRequirementID(UUID employeeID) {
        Optional<Requirement> byId = requirementRepo.findById(employeeID);
        byId.orElseThrow(RuntimeException::new);
        return byId.get();
    }

    @Override
    public Requirement getByRequirementName(String name) {
        Optional<Requirement> byName = requirementRepo.findByName(name);
        byName.orElseThrow(RuntimeException::new);
        return byName.get();
    }

    @Override
    public Requirement add(Requirement holder) {
        return requirementRepo.saveAndFlush(holder);
    }

    @Override
    public Requirement update(Requirement holder) {
        return requirementRepo.saveAndFlush(holder);
    }
}
