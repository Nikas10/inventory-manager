package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.RequirementValue;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface RequirementValueService {
    RequirementValue create(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID, @NotNull @Valid String value);
    RequirementValue update(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID, @NotNull @Valid String value);
    RequirementValue getByPositionIDAndRequirementID(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID);
    List<RequirementValue> getAll();
    void remove(@NotNull @Valid RequirementValue requirement_inventoryPosition);
}
