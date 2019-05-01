package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.repository.RequirementRepository;
import com.quartet.inventorydemo.service.RequirementService;
import com.quartet.inventorydemo.util.IdNull;
import com.quartet.inventorydemo.util.IdNotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
    public Optional<Requirement> getByRequirementID(@NotNull @Valid UUID employeeID) {
        return requirementRepo.findById(employeeID);
    }

    @Override
    public Optional<Requirement> getByRequirementName(@NotBlank @Valid String name) {
        return requirementRepo.findByName(name);
    }

    @Override
    public Requirement add(@NotNull @Valid Requirement requirement) {
        if (isExists(requirement)) {
            throw new ResourceAlreadyExistsException("Requirement with same name already exists. Can not make changes.");
        }

        Requirement newRequirement = new Requirement(requirement.getName());
        return requirementRepo.saveAndFlush(newRequirement);
    }

    
    @Override
    public Requirement update(@NotBlank @Valid UUID id, @NotNull @Valid Requirement requirement) {
        Optional<Requirement> holderOptional = getByRequirementID(id);
        if (isExists(requirement)) {
            throw new ResourceAlreadyExistsException("Requirement with same name already exists. Can not make changes.");
        }

        Requirement requirementToModify = holderOptional.orElseThrow(() -> new ResourceNotFoundException("Requirement with id: " + id + " not found"));

        BeanUtils.copyProperties(requirement, requirementToModify, "id", "requirementValues");
        return requirementRepo.saveAndFlush(requirement);
    }

    
    @Override
    public void remove(@NotNull @Valid UUID id) {
        Optional<Requirement> requirementOptional = getByRequirementID(id);

        requirementRepo.delete(requirementOptional
                .orElseThrow(() -> new ResourceNotFoundException("Requirement with id: " + id + " not found")));
    }

    private boolean isExists(@NotNull @Valid Requirement requirement) {
        ExampleMatcher uniqueMatcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id", "requirementValues");
        Example<Requirement> accountExample = Example.of(requirement, uniqueMatcher);
        Optional<Requirement> alreadyExists = requirementRepo.findOne(accountExample);

        return alreadyExists.isPresent();
    }
}
