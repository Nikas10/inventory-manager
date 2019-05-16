package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.dto.RequirementValueUpdateDTO;
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
      @NotNull @Valid RequirementValueUpdateDTO requirementValueUpdateDTO);

  RequirementValue update(
      @NotNull @Valid UUID positionID,
      @NotNull @Valid UUID requirementID,
      @NotNull @Valid RequirementValueUpdateDTO requirementValueUpdateDTO);

  Collection<RequirementValue> getRequirementsValues(@NotNull @Valid UUID positionID);

  void remove(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID);
}
