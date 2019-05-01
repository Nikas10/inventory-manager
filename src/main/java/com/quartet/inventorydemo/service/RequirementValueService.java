package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.RequirementValue;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface RequirementValueService {

  Collection<RequirementValue> getAll();

  Optional<RequirementValue> getByPositionIDAndRequirementID(
      @NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID);

  RequirementValue add(
      @NotNull @Valid UUID positionID,
      @NotNull @Valid UUID requirementID,
      @NotNull @Valid RequirementValue requirementValue);

  RequirementValue update(
      @NotNull @Valid UUID positionID,
      @NotNull @Valid UUID requirementID,
      @NotNull @Valid RequirementValue requirementValue);

  void remove(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID);
}
