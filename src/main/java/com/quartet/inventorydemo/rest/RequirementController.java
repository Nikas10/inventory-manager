package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.RequirementDTO;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.service.RequirementService;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/requirements")
public class RequirementController {

  private RequirementService requirementService;

  @Autowired
  public RequirementController(
      @Qualifier("RequirementService") final RequirementService requirementService) {
    this.requirementService = requirementService;
  }

  @RequestMapping(value = "/new", method = RequestMethod.POST)
  public ResponseEntity<?> createRequirement(@RequestBody Requirement requirement) {
    Requirement newRequirement = requirementService.add(requirement);

    return new ResponseEntity<>(newRequirement, HttpStatus.OK);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ResponseEntity<?> getAllRequirements() {
    return new ResponseEntity<>(requirementService.getAll(), HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> getRequirement(@PathVariable("id") String stringId) {
    Optional<Requirement> newRequirement = requirementService
        .getByRequirementID(UUID.fromString(stringId));
    if (newRequirement.isPresent()) {
      return new ResponseEntity<>(newRequirement.get(), HttpStatus.OK);
    } else {
      throw new ResourceNotFoundException("Requirement with id" + stringId + " is not found!");
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteRequirement(@PathVariable("id") String stringId) {
    UUID id = UUID.fromString(stringId);
    requirementService.remove(id);

    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateRequirement(
      @PathVariable("id") String stringId, @RequestBody RequirementDTO requirementDTO) {
    UUID id = UUID.fromString(stringId);
    String name = requirementDTO.getName();
    requirementService.update(id, name);

    return new ResponseEntity(HttpStatus.OK);
  }
}
