package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.InventoryPositionDTO;
import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.util.Response;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/inventoryPosition")
public class InventoryPositionController {

    private InventoryPositionService positionService;

    @Autowired
    public InventoryPositionController(@Qualifier("InventoryPositionService") final InventoryPositionService positionService) {

        this.positionService = positionService;
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable("uuid") String stringUuid) {
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
    public ResponseEntity<?> create(@RequestBody InventoryPositionDTO position) {
        String name = position.getName();
        if (name == null || "".equals(name)) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory position name is not correct.");
        }

        boolean nameExists = checkIfPositionWithSameNameExists(name);
        if (nameExists) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Role with same name already exists.");
        }

        String description = position.getDescription();
        InventoryPosition newPosition = positionService.add(new InventoryPosition(name, description));
        return Response.createResponse(newPosition);
    }

    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("uuid") String stringUuid) {
        try {
            InventoryPosition byPositionID = getPositionById(stringUuid);
            positionService.remove(byPositionID);
            return Response.createResponse();
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DeletionNotSupportedException e) {
            return Response.createErrorResponse(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, e.getMessage());
        }
    }


    private InventoryPosition getPositionById(String stringUuid) throws NotFoundException {
        UUID uuid = UUID.fromString(stringUuid);
        InventoryPosition byPositionID = positionService.getByPositionID(uuid);
        if (byPositionID == null) {
            throw new NotFoundException("no role with uuid: " + stringUuid + " found");
        }
        return byPositionID;
    }

    private boolean checkIfPositionWithSameNameExists(String roleName) {
        InventoryPosition byRoleName = positionService.getByName(roleName);

        return byRoleName != null;   //if list is not empty, then role with same name already exists
    }
}