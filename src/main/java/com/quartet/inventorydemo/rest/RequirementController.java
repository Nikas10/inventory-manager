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

    private InventoryPositionService positionService;
    private RequirementService requirementService;
    private RequirementValueService requirementValueService;

    @Autowired
    public RequirementController(@Qualifier("InventoryPositionService") final InventoryPositionService positionService,
                                 @Qualifier("RequirementService") final RequirementService requirementService,
                                 @Qualifier("RequirementValueService") final RequirementValueService requirementValueService)
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

        Requirement removed = requirementOptional.get();
        requirementService.remove(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "requirement/{requirementID}/position/{positionID}/{value}", method = RequestMethod.POST)
    public ResponseEntity<?> createRequirementValue(@PathVariable("positionID") UUID positionID,
                                                    @PathVariable("requirementID") UUID requirementID,
                                                    @RequestBody RequirementValue requirementValue) {
        Optional<InventoryPosition> positionOptional = positionService.getByPositionID(positionID);
        Optional<Requirement> requirementOptional = requirementService.getByRequirementID(requirementID);

        positionOptional.orElseThrow(() -> new ResourceNotFoundException("Position with id: " + positionID + " not found"));
        requirementOptional.orElseThrow(() -> new ResourceNotFoundException("Requirement with id: " + requirementID + " not found"));

        Optional<RequirementValue> optionalProperty = requirementValueService.getByPositionIDAndRequirementID(positionID, requirementID);

        optionalProperty.ifPresent(property -> new ResourceAlreadyExistsException("Requirement with id: " + requirementID
                                                                        + " for position with id: " + positionID
                                                                        + " already exists."));

        RequirementValue newProperty = requirementValueService.add(positionID, requirementID, requirementValue);

        return new ResponseEntity<>(newProperty, HttpStatus.OK);
    }

    @RequestMapping(value = "requirement/{requirementID}/position/{positionID}/{value}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateRequirementValue(@PathVariable("positionID") UUID positionID,
                                    @PathVariable("requirementID") UUID requirementID,
                                    @RequestBody RequirementValue value) {
        Optional<InventoryPosition> optionalPosition = positionService.getByPositionID(positionID);
        Optional<Requirement> optionalRequirement = requirementService.getByRequirementID(requirementID);

        optionalPosition.orElseThrow(() -> new ResourceNotFoundException("Position with id:" + positionID + " not found."));
        optionalRequirement.orElseThrow(() -> new ResourceNotFoundException("Requirement with id:" + requirementID + " not found."));

        Optional<RequirementValue> optionalRequirementValue = requirementValueService.getByPositionIDAndRequirementID(positionID, requirementID);

        optionalRequirementValue.orElseThrow(() -> new ResourceNotFoundException("Requirement with id: " + requirementID
                                                                                 + " for position with id: " + positionID
                                                                                 + " not found."));

        RequirementValue newProperty = requirementValueService.update(positionID, requirementID, value);

        return new ResponseEntity<>(newProperty, HttpStatus.OK);
    }

    @RequestMapping(value = "requirement/{requirementID}/position/{positionID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRequirementValue(@PathVariable("positionID") UUID positionID,
                                    @PathVariable("requirementID") UUID requirementID)
    {
        Optional<InventoryPosition> optionalPosition = positionService.getByPositionID(positionID);
        Optional<Requirement> optionalRequirement = requirementService.getByRequirementID(requirementID);

        optionalPosition.orElseThrow(() -> new ResourceNotFoundException("Position with id:" + positionID + " not found."));
        optionalRequirement.orElseThrow(() -> new ResourceNotFoundException("Requirement with id:" + requirementID + " not found."));

        Optional<RequirementValue> optionalRequirementValue = requirementValueService.getByPositionIDAndRequirementID(positionID, requirementID);

        optionalRequirementValue.orElseThrow(() -> new ResourceNotFoundException("Requirement with id: " + requirementID
                + " for position with id: " + positionID
                + " not found."));

        requirementValueService.remove(optionalRequirementValue.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
