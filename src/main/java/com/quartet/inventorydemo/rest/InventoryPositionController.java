package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.InventoryPositionDTO;
import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.RequirementValue;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequirementService;
import com.quartet.inventorydemo.service.RequirementValueService;
import com.quartet.inventorydemo.util.Response;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/position")
public class InventoryPositionController {

    private InventoryPositionService positionService;
    private RequirementValueService requirementValueService;
    private RequirementService requirementService;

    @Autowired
    public InventoryPositionController(@Qualifier("InventoryPositionService") final InventoryPositionService positionService,
                                       @Qualifier("RequirementService") final RequirementService requirementService,
                                       @Qualifier("RequirementValueService") final RequirementValueService requirementValueService) {

        this.positionService = positionService;
        this.requirementService = requirementService;
        this.requirementValueService = requirementValueService;
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable("uuid") String stringUuid) {
            UUID id = UUID.fromString(stringUuid);
            Optional<InventoryPosition> optionalPosition = positionService.getByPositionID(id);
            optionalPosition.orElseThrow(() -> new ResourceNotFoundException("Position with id: " + id + " not found."));
            return new ResponseEntity<>(optionalPosition.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody InventoryPosition position) {
        InventoryPosition newPosition = positionService.add(position);
        return new ResponseEntity<>(newPosition, HttpStatus.OK);
    }

    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("uuid") String stringUuid) {
            UUID id = UUID.fromString(stringUuid);
            Optional<InventoryPosition> byPositionID = positionService.getByPositionID(id);
            byPositionID.orElseThrow(() -> new ResourceNotFoundException("Position with id: " + id + " not found."));
            InventoryPosition removed = byPositionID.get();
            positionService.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(value = "{positionID}/requirement/{requirementID}/{value}", method = RequestMethod.POST)
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

    @RequestMapping(value = "{positionID}/requirement/{requirementID}/{value}", method = RequestMethod.PATCH)
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

    @RequestMapping(value = "{positionID}/requirement/{requirementID}/", method = RequestMethod.DELETE)
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