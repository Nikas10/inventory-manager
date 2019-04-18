package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.*;
import com.quartet.inventorydemo.service.*;
import com.quartet.inventorydemo.util.Response;
import javassist.NotFoundException;
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

    private InventoryPosition getPositionById(String stringUuid) throws NotFoundException {
        UUID uuid = UUID.fromString(stringUuid);
        InventoryPosition byPositionID = inventoryPositionService.getByPositionID(uuid);
        if (byPositionID == null) {
            throw new NotFoundException("no role with uuid: " + stringUuid + " found");
        }
        return byPositionID;
    }
}
