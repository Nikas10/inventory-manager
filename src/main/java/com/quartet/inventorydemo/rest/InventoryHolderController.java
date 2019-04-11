package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.InventoryHolder;
import com.quartet.inventorydemo.service.InventoryHolderService;
import com.quartet.inventorydemo.util.Response;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/holder")
public class InventoryHolderController {

    @Autowired
    @Qualifier("InventoryHolderService")
    private InventoryHolderService inventoryHolderService;

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolder(@PathVariable("uuid") String stringUuid) {
        try {
            UUID uuid = UUID.fromString(stringUuid);
            InventoryHolder byHolderID = inventoryHolderService.getByHolderID(uuid);
            if (byHolderID == null) {
                throw new NotFoundException("No inventory holder with uuid: " + stringUuid + " found");
            }
            return Response.createResponse(byHolderID);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createInventoryHolder(@RequestBody InventoryHolder inventoryHolder) {
        String requestInventoryHolderName = inventoryHolder.getName();
        if (requestInventoryHolderName == null || "".equals(requestInventoryHolderName)) {
            boolean nameExists = checkIfHolderWithSameNameExists(requestInventoryHolderName);
            if (nameExists) {
                return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory holder with same name already exists.");
            } else {
                InventoryHolder result = inventoryHolderService.add(inventoryHolder);
                return Response.createResponse(result);
            }
        } else {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory holder name is not correct.");
        }
    }

    @PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteInventoryHolder(@PathVariable("uuid") String stringUuid) {
        try {
            UUID uuid = UUID.fromString(stringUuid);
            InventoryHolder byHolderID = inventoryHolderService.getByHolderID(uuid);
            if (byHolderID == null) {
                throw new NotFoundException("No inventory holder with uuid: " + stringUuid + " found");
            }
            inventoryHolderService.remove(byHolderID);
            return Response.createResponse();
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private boolean checkIfHolderWithSameNameExists(String holderName) {
        List<InventoryHolder> byHolderName = inventoryHolderService.getByHolderName(holderName);   //TODO add constraint to db for name uniqueness
        return !byHolderName.isEmpty();   //if list is not empty, then holder with same name already exists
    }
}
