package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.RequirementValue;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.repository.RequirementRepository;
import com.quartet.inventorydemo.repository.RequirementValueRepository;
import com.quartet.inventorydemo.service.RequirementValueService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("RequirementValueService")
@Validated
@Transactional
public class RequirementValueServiceImpl implements RequirementValueService {
    private final RequirementValueRepository requirementValueRepo;
    private final InventoryPositionRepository positionRepo;
    private final RequirementRepository requirementRepo;

    RequirementValueServiceImpl(@Qualifier("RequirementValueRepository") final RequirementValueRepository requirementValueRepo,
                                @Qualifier("InventoryPositionRepository") final InventoryPositionRepository positionRepo,
                                @Qualifier("RequirementRepository") final RequirementRepository requirementRepo) {
        this.requirementValueRepo = requirementValueRepo;
        this.positionRepo = positionRepo;
        this.requirementRepo = requirementRepo;
    }

    @Override
    public RequirementValue add(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID, @NotNull @Valid RequirementValue requirementValue) {
        InventoryPosition position = positionRepo.findById(positionID).get();
        Requirement requirement = requirementRepo.findById(requirementID).get();
        Optional<RequirementValue> requirementValueOptional = requirementValueRepo.findByRequirement_IdAndInventoryPosition_Id(requirementID, positionID);
        requirementValueOptional.ifPresent(s -> {
            throw new ResourceAlreadyExistsException("Requirement with id:" + requirementID
                                                     + " for position with id: " + positionID
                                                     + "already exists.");
        });

        return requirementValueOptional.orElse(
                requirementValueRepo.saveAndFlush(new RequirementValue(requirement, position, requirementValue.getValue()))
        );
    }

    @Override
    public RequirementValue update(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID, @NotNull @Valid RequirementValue requirementValue) {
        InventoryPosition position = positionRepo.findById(positionID).get();
        Requirement requirement = requirementRepo.findById(requirementID).get();
        Optional<RequirementValue> requirementValueOptional = requirementValueRepo.findByRequirement_IdAndInventoryPosition_Id(requirementID, positionID);
        requirementValueOptional.ifPresent(s -> {
            requirementValueOptional.orElse(
                    requirementValueRepo.saveAndFlush(new RequirementValue(requirement, position, requirementValue.getValue()))
            );
        });

        throw new ResourceNotFoundException("not found");
    }

    @Override
    public Optional<RequirementValue> getByPositionIDAndRequirementID(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID) {
        return requirementValueRepo.findByRequirement_IdAndInventoryPosition_Id(requirementID, positionID);
    }

    @Override
    public List<RequirementValue> getAll() {
        return requirementValueRepo.findAll();
    }

    @Override
    public void remove(@NotNull @Valid RequirementValue requirement_inventoryPosition) {
        requirementValueRepo.delete(requirement_inventoryPosition);
    }

}
