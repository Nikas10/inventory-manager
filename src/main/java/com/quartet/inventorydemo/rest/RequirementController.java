package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.RequirementValue;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequirementService;
import com.quartet.inventorydemo.service.RequirementValueService;
import com.quartet.inventorydemo.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "value/{name}", method = RequestMethod.POST)
    public ResponseEntity<?> createRequiement(@PathVariable("name") String name) {
        Requirement requirement = requirementService.getByRequirementName(name);

        if (requirement != null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement already exists.");
        }

        requirementService.add(new Requirement(name));

        return Response.createResponse(requirement);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRequirement(@PathVariable("id") UUID id) {
        Requirement requirement = requirementService.getByRequirementID(id);

        if (requirement == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement not found.");
        }

        requirementService.remove(requirement);

        return Response.createResponse(requirement);
    }

    @RequestMapping(value = "requirement/{requirementID}/position/{positionID}/{value}", method = RequestMethod.POST)
    public ResponseEntity<?> createRequirementValue(@PathVariable("positionID") UUID positionID,
                                    @PathVariable("requirementID") UUID requirementID,
                                    @PathVariable("value") String value) {
        InventoryPosition position = positionService.getByPositionID(positionID);
        Requirement requirement = requirementService.getByRequirementID(requirementID);

        if (position == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory position not found.");
        }

        if (requirement == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement not found.");
        }

        RequirementValue newProperty = requirementValueService.getByPositionIDAndRequirementID(positionID, requirementID);

        if (newProperty != null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Property already exists.");
        }

        newProperty = requirementValueService.create(positionID, requirementID, value);

        return Response.createResponse(newProperty);
    }

    @RequestMapping(value = "requirement/{requirementID}/position/{positionID}/{value}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateRequirementValue(@PathVariable("positionID") UUID positionID,
                                    @PathVariable("requirementID") UUID requirementID,
                                    @PathVariable("value") String value) {
        InventoryPosition position = positionService.getByPositionID(positionID);
        Requirement requirement = requirementService.getByRequirementID(requirementID);

        if (position == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory position not found.");
        }

        if (requirement == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement not found.");
        }

        RequirementValue newProperty = requirementValueService.getByPositionIDAndRequirementID(positionID, requirementID);

        if (newProperty == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Property does not exist.");
        }

        newProperty = requirementValueService.update(positionID, requirementID, value);

        return Response.createResponse(newProperty);
    }

    @RequestMapping(value = "requirement/{requirementID}/position/{positionID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRequirementValue(@PathVariable("positionID") UUID positionID,
                                    @PathVariable("requirementID") UUID requirementID)
    {
        InventoryPosition position = positionService.getByPositionID(positionID);
        Requirement requirement = requirementService.getByRequirementID(requirementID);

        if (position == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory position not found.");
        }

        if (requirement == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement not found.");
        }

        RequirementValue property = requirementValueService.getByPositionIDAndRequirementID(positionID, requirementID);

        if (property == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Property does not exist.");
        }

        requirementValueService.remove(property);

        return Response.createResponse();
    }
}
