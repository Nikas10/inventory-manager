package com.quartet.inventorydemo.rest;


import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.util.UUIDString;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/inventoryItems")
@Validated
public class InventoryItemController {

  private final HolderService holderService;
  private final InventoryItemService inventoryItemService;

  @Autowired
  public InventoryItemController(
      @Qualifier("HolderService") final HolderService holderService,
      @Qualifier("InventoryItemService") final InventoryItemService inventoryItemService) {
    this.holderService = holderService;
    this.inventoryItemService = inventoryItemService;
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/storage/{uuid}", method = RequestMethod.GET)
  public ResponseEntity<?> getInventoryItemInStorage(
      @PathVariable("uuid") @UUIDString @Valid String positionId) {
    UUID uuid = UUID.fromString(positionId);
    Optional<InventoryItem> item = inventoryItemService.getByInventoryPositionIdInStorage(uuid);
    InventoryItem result =
        item.orElseThrow(
            () -> new ResourceNotFoundException("inventory item by position with id: " + uuid + "not found in storage"));
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/positionId/{uuid}/holderId/{holder}", method = RequestMethod.GET)
  public ResponseEntity<?> getInventoryItemByHolder(
      @PathVariable("holder") @UUIDString @Valid String holderId,
      @PathVariable("uuid") @UUIDString @Valid String positionId) {
    UUID uuid = UUID.fromString(positionId);
    UUID holder = UUID.fromString(holderId);
    Optional<InventoryItem> item = inventoryItemService.getByInventoryPositionIdAndHolderId(uuid, holder);
    InventoryItem result =
        item.orElseThrow(
            () -> new ResourceNotFoundException("inventory item by position with id: " + uuid + " and holder "+ holder +"not found"));
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/holderId/{holder}", method = RequestMethod.GET)
  public ResponseEntity<?> getInventoryItemsByHolder(
      @PathVariable("holder") @UUIDString @Valid String holderId) {
    UUID uuid = UUID.fromString(holderId);
    Optional<Holder> holderOptional = holderService.getByHolderID(uuid);
    Holder holder =
        holderOptional.orElseThrow(
            () -> new ResourceNotFoundException("inventory holder with id: " + uuid + "not found"));
    Set<InventoryItem> items = inventoryItemService.getAll()
        .stream()
        .filter(entry -> entry.getHolder().equals(holder))
        .collect(Collectors.toSet());
    return new ResponseEntity<>(items, HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/storage/positionId/{uuid}/amount/{amount}", method = RequestMethod.POST)
  public ResponseEntity<?> addInventoryItemToStorage(
      @PathVariable("uuid") @UUIDString @Valid String positionId,
      @PathVariable("amount") Integer amount) {
    UUID uuid = UUID.fromString(positionId);
    inventoryItemService.addToStorage(uuid, amount);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/storage/positionId/{uuid}/amount/{amount}", method = RequestMethod.DELETE)
  public ResponseEntity<?> removeInventoryItemFromStorage(
      @PathVariable("uuid") @UUIDString @Valid String positionId,
      @PathVariable("amount") Integer amount) {
    UUID uuid = UUID.fromString(positionId);
    inventoryItemService.removeFromStorage(uuid, amount);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "positionId/{uuid}/holderId/{holder}/amount/{amount}", method = RequestMethod.PUT)
  public ResponseEntity<?> moveInventoryItemToStorage(
      @PathVariable("holder") @UUIDString @Valid String holderId,
      @PathVariable("uuid") @UUIDString @Valid String positionId,
      @PathVariable("amount") Integer amount) {
    UUID holder = UUID.fromString(holderId);
    UUID position = UUID.fromString(positionId);
    inventoryItemService.moveFromHolderToStorage(position, holder, amount);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "storage/positionId/{uuid}/holderId/{holder}/amount/{amount}", method = RequestMethod.PUT)
  public ResponseEntity<?> moveInventoryItemFromStorage(
      @PathVariable("holder") @UUIDString @Valid String holderId,
      @PathVariable("uuid") @UUIDString @Valid String positionId,
      @PathVariable("amount") Integer amount) {
    UUID holder = UUID.fromString(holderId);
    UUID position = UUID.fromString(positionId);
    inventoryItemService.moveFromStorageToHolder(position, holder, amount);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
