package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.repository.RequirementRepository;
import com.quartet.inventorydemo.service.RequirementService;
import com.quartet.inventorydemo.util.IdNull;
import com.quartet.inventorydemo.util.IdNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service("RequirementService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class RequirementServiceImpl implements RequirementService {
    private final RequirementRepository requirementRepo;

    @Autowired
    public RequirementServiceImpl(@Qualifier("RequirementRepository") final RequirementRepository requirementRepo) {
        this.requirementRepo = requirementRepo;
    }

    @Override
    public Set<Requirement> getAll() {
        return requirementRepo.findAllToSet();
    }

    @Override
    public Requirement getByRequirementID(@NotNull @Valid UUID employeeID) {
        Optional<Requirement> byId = requirementRepo.findById(employeeID);
        byId.orElseThrow(ResourceNotFoundException::new);
        return byId.get();
    }

    @Override
    public Requirement getByRequirementName(@NotBlank @Valid String name) {
        Optional<Requirement> byName = requirementRepo.findByName(name);
        byName.orElseThrow(ResourceNotFoundException::new);
        return byName.get();
    }

    @Validated(IdNull.class)
    @Override
    public Requirement add(@NotNull @Valid Requirement holder) {
        return requirementRepo.saveAndFlush(holder);
    }

    @Validated(IdNotNull.class)
    @Override
    public Requirement update(@NotNull @Valid Requirement holder) {
        return requirementRepo.saveAndFlush(holder);
    }

    @Validated(IdNotNull.class)
    @Override
    public void remove(@NotNull @Valid Requirement requirement) {

    }
}
