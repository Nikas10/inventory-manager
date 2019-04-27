package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;

public interface RequirementService {

    Collection<Requirement> getAll();

    Requirement getByRequirementID(@NotNull @Valid UUID requirementID);

    Requirement getByRequirementName(@NotBlank @Valid String name);

    @Validated(OnCreate.class)
    Requirement add(@NotNull @Valid Requirement requirement);

    @Validated(OnUpdate.class)
    Requirement update(@NotNull @Valid Requirement requirement);

    @Validated(OnUpdate.class)
    void remove(@NotNull @Valid Requirement requirement);
}
