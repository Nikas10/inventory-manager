package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.InventoryPositionDTO;
import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.util.Response;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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
            UUID id = UUID.fromString(stringUuid);
            Optional<InventoryPosition> optionalPosition = positionService.getByPositionID(id);
            optionalPosition.orElseThrow(() -> new ResourceNotFoundException("Position with id: " + id + " not found."));
            return new ResponseEntity<>(optionalPosition, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody InventoryPosition position) {
        InventoryPosition newPosition = positionService.add(position);
        return Response.createResponse(newPosition);
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

}