package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.CreateAndDeleteLinksForm;
import com.quartet.inventorydemo.dto.HolderDTO;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.util.Response;
import com.quartet.inventorydemo.util.UUIDString;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/holder")
@Validated
public class HolderController {

  private final HolderService holderService;

  private final AccountService accountService;
  private final InventoryItemService inventoryItemService;

  @Autowired
  public HolderController(
      @Qualifier("HolderService") final HolderService holderService,
      @Qualifier("AccountService") final AccountService accountService,
      @Qualifier("InventoryItemService") final InventoryItemService inventoryItemService) {
    this.holderService = holderService;
    this.accountService = accountService;
    this.inventoryItemService = inventoryItemService;
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
  public ResponseEntity<?> getInventoryHolder(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid) {
    UUID uuid = UUID.fromString(stringUuid);
    Optional<Holder> holderOptional = holderService.getByHolderID(uuid);
    Holder holder =
        holderOptional.orElseThrow(
            () -> new ResourceNotFoundException("inventory holder with id: " + uuid + "not found"));
    return new ResponseEntity<>(holder, HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<?> createInventoryHolder(@RequestBody HolderDTO holderDTO) {
    String description = holderDTO.getDescription();
    String name = holderDTO.getName();
    Holder newHolder = holderService.add(description, name);
    return new ResponseEntity<>(newHolder, HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateInventoryHolder(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid,
      @RequestBody HolderDTO holderDTO) {
    String description = holderDTO.getDescription();
    String name = holderDTO.getName();
    UUID uuid = UUID.fromString(stringUuid);

    Holder updatedHolder = holderService.update(uuid, description, name);
    return new ResponseEntity<>(updatedHolder, HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteInventoryHolder(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid) {
    UUID uuid = UUID.fromString(stringUuid);
    holderService.remove(uuid);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/{uuid}/role", method = RequestMethod.GET)
  public ResponseEntity<?> getInventoryHolderLinksToRoles(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid) {
    UUID uuid = UUID.fromString(stringUuid);
    Optional<Holder> holderOptional = holderService.getByHolderID(uuid);
    holderOptional.orElseThrow(
        () -> new ResourceNotFoundException("inventory holder with id: " + uuid + "not found"));
    return new ResponseEntity<>(holderOptional.get().getRoles(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{uuid}/role", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateInventoryHolderLinksToRoles(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid,
      @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
    UUID uuid = UUID.fromString(stringUuid);
    Set<UUID> addByIds = createAndDeleteLinksForm.convertAndGetAddIds();
    Set<UUID> removeByIds = createAndDeleteLinksForm.convertAndGetRemoveIds();
    Holder result = null;
    if (!addByIds.isEmpty()) {
      result = holderService.addRoles(uuid, addByIds);
    }
    if (!removeByIds.isEmpty()) {
      result = holderService.removeRoles(uuid, removeByIds);
    }
    if (result == null) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(result.getRoles(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/{uuid}/account", method = RequestMethod.GET)
  public ResponseEntity<?> getInventoryHolderLinksToAccounts(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid) {
    UUID uuid = UUID.fromString(stringUuid);
    Optional<Holder> holderOptional = holderService.getByHolderID(uuid);
    holderOptional.orElseThrow(
        () -> new ResourceNotFoundException("inventory holder with id: " + uuid + "not found"));
    return new ResponseEntity<>(holderOptional.get().getAccounts(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/{uuid}/item", method = RequestMethod.GET)
  public ResponseEntity<?> getInventoryHolderLinksToHoldedItems(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid) {
    UUID uuid = UUID.fromString(stringUuid);
    Optional<Holder> holderOptional = holderService.getByHolderID(uuid);
    holderOptional.orElseThrow(
        () -> new ResourceNotFoundException("inventory holder with id: " + uuid + "not found"));
    return new ResponseEntity<>(holderOptional.get().getInventoryItems(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{uuid}/item", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateInventoryHolderLinksToHoldedItems(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid,
      @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
    UUID uuid = UUID.fromString(stringUuid);
    Optional<Holder> holderOptional = holderService.getByHolderID(uuid);
    holderOptional.orElseThrow(
        () -> new ResourceNotFoundException("inventory holder with id: " + uuid + "not found"));
    Holder holderWithInventoryItems = holderOptional.get();
    Set<InventoryItem> currentInventoryItems = holderWithInventoryItems.getInventoryItems();

    // TODO item functional

    Holder update =
        holderService.update(
            uuid, holderWithInventoryItems.getDescription(), holderWithInventoryItems.getName());
    return Response.createResponse(update);
  }
}
