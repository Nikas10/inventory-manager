package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requirement;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface RequirementService {

    Collection<Requirement> getAll();

    Optional<Requirement> getByRequirementID(@NotNull @Valid UUID requirementID);

    Optional<Requirement> getByRequirementName(@NotBlank @Valid String name);

    Requirement add(@NotNull @Valid Requirement requirement);

    Requirement update(@NotBlank @Valid UUID id, @NotNull @Valid Requirement requirement);

    void remove(@NotNull @Valid UUID id);
}
