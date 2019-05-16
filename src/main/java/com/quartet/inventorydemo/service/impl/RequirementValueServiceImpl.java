package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.dto.RequirementValueUpdateDTO;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.RequirementValue;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.repository.RequirementRepository;
import com.quartet.inventorydemo.repository.RequirementValueRepository;
import com.quartet.inventorydemo.service.RequirementValueService;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service("RequirementValueService")
@Validated
@Transactional
public class RequirementValueServiceImpl implements RequirementValueService {

  private final RequirementValueRepository requirementValueRepo;
  private final InventoryPositionRepository positionRepo;
  private final RequirementRepository requirementRepo;

  RequirementValueServiceImpl(
      @Qualifier("RequirementValueRepository") final RequirementValueRepository requirementValueRepo,
      @Qualifier("InventoryPositionRepository") final InventoryPositionRepository positionRepo,
      @Qualifier("RequirementRepository") final RequirementRepository requirementRepo) {
    this.requirementValueRepo = requirementValueRepo;
    this.positionRepo = positionRepo;
    this.requirementRepo = requirementRepo;
  }

  @Override
  public RequirementValue add(
      @NotNull @Valid UUID positionID,
      @NotNull @Valid UUID requirementID,
      @NotNull @NotNull RequirementValueUpdateDTO requirementValueUpdateDTO) {
    Optional<InventoryPosition> optionalPosition = positionRepo.findById(positionID);
    Optional<Requirement> optionalRequirement = requirementRepo.findById(requirementID);

    InventoryPosition position = optionalPosition.orElseThrow(
        () -> new ResourceNotFoundException("Position with id: " + positionID + " not found."));
    Requirement requirement = optionalRequirement.orElseThrow(
        () ->
            new ResourceNotFoundException("Requirement with id: " + requirementID + " not found."));

    Optional<RequirementValue> requirementValueOptional =
        requirementValueRepo.findByRequirement_IdAndInventoryPosition_Id(requirementID, positionID);

    if (requirementValueOptional.isPresent()) {
      throw new ResourceAlreadyExistsException(
          "Requirement with id:"
              + requirementID
              + " for position with id: "
              + positionID
              + "already exists.");
    }

    if (!"".equals(requirementValueUpdateDTO.getRequirementValue()) &&
        (requirementValueUpdateDTO.getRequirementValue() != null)) {
      RequirementValue newRequirementValue = new RequirementValue(requirement, position,
          requirementValueUpdateDTO.getRequirementValue());
      newRequirementValue.setValue(requirementValueUpdateDTO.getRequirementValue());
      return requirementValueRepo.saveAndFlush(newRequirementValue);
    }

    return null;
  }

  @Override
  public RequirementValue update(
      @NotNull @Valid UUID positionID,
      @NotNull @Valid UUID requirementID,
      @NotNull @NotNull RequirementValueUpdateDTO requirementValueUpdateDTO) {
    Optional<InventoryPosition> optionalPosition = positionRepo.findById(positionID);
    Optional<Requirement> optionalRequirement = requirementRepo.findById(requirementID);

    optionalPosition.orElseThrow(
        () -> new ResourceNotFoundException("Position with id: " + positionID + " not found."));
    optionalRequirement.orElseThrow(
        () ->
            new ResourceNotFoundException("Requirement with id: " + requirementID + " not found."));

    Optional<RequirementValue> requirementValueOptional =
        requirementValueRepo.findByRequirement_IdAndInventoryPosition_Id(requirementID, positionID);

    requirementValueOptional.orElseThrow(
        () ->
            new ResourceNotFoundException(
                "Requirement with id: "
                    + requirementID
                    + " for position with id: "
                    + positionID
                    + " not found."));
    RequirementValue toChange = requirementValueOptional.get();

    if ("".equals(requirementValueUpdateDTO.getRequirementValue()) &&
        (requirementValueUpdateDTO.getRequirementValue() != null)) {
      toChange.setValue(requirementValueUpdateDTO.getRequirementValue());
    }

    return requirementValueRepo.saveAndFlush(toChange);
  }

  @Override
  public Collection<RequirementValue> getRequirementsValues(@NotNull @Valid UUID positionID) {
    return requirementValueRepo.findAllByInventoryPosition_Id(positionID);
  }

  @Override
  public Optional<RequirementValue> getByPositionIDAndRequirementID(
      @NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID) {
    return requirementValueRepo.findByRequirement_IdAndInventoryPosition_Id(
        requirementID, positionID);
  }

  @Override
  public List<RequirementValue> getAll() {
    return requirementValueRepo.findAll();
  }

  @Override
  public void remove(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID) {

    Optional<InventoryPosition> optionalPosition = positionRepo.findById(positionID);
    Optional<Requirement> optionalRequirement = requirementRepo.findById(requirementID);

    optionalPosition.orElseThrow(
        () -> new ResourceNotFoundException("Position with id: " + positionID + " not found."));
    optionalRequirement.orElseThrow(
        () ->
            new ResourceNotFoundException("Requirement with id: " + requirementID + " not found."));

    Optional<RequirementValue> optionalRequirementValue =
        requirementValueRepo.findByRequirement_IdAndInventoryPosition_Id(requirementID, positionID);

    requirementValueRepo.delete(
        optionalRequirementValue.orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Requirement with id: "
                        + requirementID
                        + " for position with id: "
                        + positionID
                        + " not found.")));
  }
}
