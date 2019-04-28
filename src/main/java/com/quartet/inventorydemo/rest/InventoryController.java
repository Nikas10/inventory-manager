package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.InventoryPositionDTO;
import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.model.*;
import com.quartet.inventorydemo.service.*;
import com.quartet.inventorydemo.util.Response;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/inventory")
public class InventoryController {

    @Autowired
    @Qualifier("InventoryHolderService")
    private InventoryHolderService inventoryHolderService;

    @Autowired
    @Qualifier("InventoryPositionService")
    private InventoryPositionService inventoryPositionService;

    @Autowired
    @Qualifier("InventoryPositionContentsService")
    private InventoryPositionContentsService inventoryPositionContentsService;

    @Autowired
    @Qualifier("RequirementService")
    private RequirementService requirementService;

    @Autowired
    @Qualifier("Requirement_InventoryPositionService")
    private Requirement_InventoryPositionService requirement_inventoryPositionService;

    @Autowired
    @Qualifier("InventoryItemService")
    private InventoryItemService inventoryItemService;

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryPosition(@PathVariable("uuid") String stringUuid) {
        try {
            InventoryPosition byPositionID = getPositionById(stringUuid);
            return Response.createResponse(byPositionID);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createInventoryPosition(@RequestBody InventoryPositionDTO position) {
        String name = position.getName();
        if (name == null || "".equals(name)) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory position name is not correct.");
        }

        boolean nameExists = checkIfPositionWithSameNameExists(name);
        if (nameExists) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Role with same name already exists.");
        }

        String description = position.getDescription();
        InventoryPosition newPosition = inventoryPositionService.add(new InventoryPosition(name, description));
        return Response.createResponse(newPosition);
    }

    @RequestMapping(value = "position/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteInventoryPosition(@PathVariable("uuid") String stringUuid) {
        try {
            InventoryPosition byPositionID = getPositionById(stringUuid);
            inventoryPositionService.removeInventoryItem(byPositionID);
            return Response.createResponse();
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DeletionNotSupportedException e) {
            return Response.createErrorResponse(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, e.getMessage());
        }
    }

    @RequestMapping(value = "bundle/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBundle(@PathVariable("uuid") String stringUuid) {
        try {
            InventoryPosition byPositionID = getPositionById(stringUuid);
            inventoryPositionService.removeInventoryItem(byPositionID);
            return Response.createResponse();
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DeletionNotSupportedException e) {
            return Response.createErrorResponse(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, e.getMessage());
        }
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.POST)
    public ResponseEntity<?> createRequirement(@PathVariable("name") String name) {
        Requirement requirement = requirementService.getByRequirementName(name);

        if (requirement != null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement already exists.");
        }

        requirementService.add(new Requirement(name));

        return Response.createResponse(requirement);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRequirement(@PathVariable("id") UUID id) {
        Requirement requirement = requirementService.getByRequirementID(id);

        if (requirement == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement not found.");
        }

        requirementService.remove(requirement);

        return Response.createResponse(requirement);
    }


    @RequestMapping(value = "create/{positionID}/{requirementID}/{value}", method = RequestMethod.POST)
    public ResponseEntity<?> createRequirementForInventoryPosition( @PathVariable("positionID") UUID positionID,
                                                                    @PathVariable("requirementID") UUID requirementID,
                                                                    @PathVariable("value") String value) {
        InventoryPosition position = inventoryPositionService.getByPositionID(positionID);
        Requirement requirement = requirementService.getByRequirementID(requirementID);

        if (position == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory position not found.");
        }

        if (requirement == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement not found.");
        }

        Requirement_InventoryPosition newProperty = requirement_inventoryPositionService.getByPositionIDAndRequirementID(positionID, requirementID);

        if (newProperty != null) {
            //if (newProperty.getValue().equals(value)) {
                return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Property already exists.");
            //}
        }

        newProperty = requirement_inventoryPositionService.changeRequirement(positionID, requirementID, value);

        return Response.createResponse(newProperty);
    }

    @RequestMapping(value = "change/{positionID}/{requirementID}/{value}", method = RequestMethod.POST)
    public ResponseEntity<?> changeRequirementForInventoryPosition( @PathVariable("positionID") UUID positionID,
                                                                    @PathVariable("requirementID") UUID requirementID,
                                                                    @PathVariable("value") String value) {
        InventoryPosition position = inventoryPositionService.getByPositionID(positionID);
        Requirement requirement = requirementService.getByRequirementID(requirementID);

        if (position == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory position not found.");
        }

        if (requirement == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement not found.");
        }

        Requirement_InventoryPosition newProperty = requirement_inventoryPositionService.getByPositionIDAndRequirementID(positionID, requirementID);

        if (newProperty == null) {
            //if (newProperty.getValue().equals(value)) {
                return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Property does not exist.");
            //}
        }

        newProperty = requirement_inventoryPositionService.changeRequirement(positionID, requirementID, value);

        return Response.createResponse(newProperty);
    }

    @RequestMapping(value = "/{positionID}/{requirementID}/{value}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRequirementForInventoryPosition( @PathVariable("positionID") UUID positionID,
                                                                    @PathVariable("requirementID") UUID requirementID,
                                                                    @PathVariable("value") String value) {
        InventoryPosition position = inventoryPositionService.getByPositionID(positionID);
        Requirement requirement = requirementService.getByRequirementID(requirementID);

        if (position == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory position not found.");
        }

        if (requirement == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement not found.");
        }

        Requirement_InventoryPosition property = requirement_inventoryPositionService.getByPositionIDAndRequirementID(positionID, requirementID);

        if (property == null) {
                return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Property does not exist.");
        }

        requirement_inventoryPositionService.remove(property);

        return Response.createResponse();
    }

    private InventoryPosition getPositionById(String stringUuid) throws NotFoundException {
        UUID uuid = UUID.fromString(stringUuid);
        InventoryPosition byPositionID = inventoryPositionService.getByPositionID(uuid);
        if (byPositionID == null) {
            throw new NotFoundException("no role with uuid: " + stringUuid + " found");
        }
        return byPositionID;
    }

    private boolean checkIfPositionWithSameNameExists(String roleName) {
        InventoryPosition byRoleName = inventoryPositionService.getByName(roleName);

        return byRoleName != null;   //if list is not empty, then role with same name already exists
    }
}