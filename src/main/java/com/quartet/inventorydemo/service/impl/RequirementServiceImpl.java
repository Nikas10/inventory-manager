package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.repository.RequirementRepository;
import com.quartet.inventorydemo.service.RequirementService;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service("RequirementService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class RequirementServiceImpl implements RequirementService {

  private final RequirementRepository requirementRepo;

  @Autowired
  public RequirementServiceImpl(
      @Qualifier("RequirementRepository") final RequirementRepository requirementRepo) {
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
      throw new ResourceAlreadyExistsException(
          "Requirement with same name already exists. Can not make changes.");
    }

    Requirement newRequirement = new Requirement(requirement.getName());
    return requirementRepo.saveAndFlush(newRequirement);
  }

  @Override
  public Requirement update(@NotBlank @Valid UUID id, @NotNull @Valid String name) {
    Optional<Requirement> optionalRequirement = getByRequirementID(id);

    Requirement requirementToModify =
        optionalRequirement.orElseThrow(
            () -> new ResourceNotFoundException("Requirement with id: " + id + " not found"));

    requirementToModify.setName(name);
    return requirementRepo.saveAndFlush(requirementToModify);
  }

  @Override
  public void remove(@NotNull @Valid UUID id) {
    Optional<Requirement> requirementOptional = getByRequirementID(id);

    requirementRepo.delete(
        requirementOptional.orElseThrow(
            () -> new ResourceNotFoundException("Requirement with id: " + id + " not found")));
  }

  private boolean isExists(@NotNull @Valid Requirement requirement) {
    ExampleMatcher uniqueMatcher =
        ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
            .withIgnorePaths("id", "requirementValues");
    Example<Requirement> accountExample = Example.of(requirement, uniqueMatcher);
    Optional<Requirement> alreadyExists = requirementRepo.findOne(accountExample);

    return alreadyExists.isPresent();
  }
}
