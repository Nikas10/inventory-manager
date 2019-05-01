package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.RequirementValue;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequirementService;
import com.quartet.inventorydemo.service.RequirementValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/requirement")
public class RequirementController {

    private RequirementService requirementService;

    @Autowired
    public RequirementController(@Qualifier("RequirementService") final RequirementService requirementService)
    {
        this.requirementService = requirementService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createRequiement(@RequestBody Requirement requirement) {
        Requirement newRequirement = requirementService.add(requirement);

        return new ResponseEntity<>(newRequirement, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRequirement(@PathVariable("id") UUID id) {
        Optional<Requirement> requirementOptional = requirementService.getByRequirementID(id);
        requirementOptional.orElseThrow(() -> new ResourceNotFoundException("Requirement with id: " + id + " not found"));

        requirementService.remove(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
