package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.RequirementValue;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequirementValueService {

    Collection<RequirementValue> getAll();

    Optional<RequirementValue> getByPositionIDAndRequirementID(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID);

    RequirementValue add(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID, @NotNull @Valid String value);

    RequirementValue update(@NotNull @Valid UUID positionID, @NotNull @Valid UUID requirementID, @NotNull @Valid String value);

    void remove(@NotNull @Valid RequirementValue requirement_inventoryPosition);
}
