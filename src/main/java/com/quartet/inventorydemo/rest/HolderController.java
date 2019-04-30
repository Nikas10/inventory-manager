package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.CreateAndDeleteLinksForm;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.service.RoleService;
import com.quartet.inventorydemo.util.Response;
import com.quartet.inventorydemo.util.UUIDString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("api/holder")
@Validated
public class HolderController {
    private final HolderService holderService;
    private final RoleService roleService;
    private final AccountService accountService;
    private final InventoryItemService inventoryItemService;

    @Autowired
    public HolderController(@Qualifier("HolderService") final HolderService holderService,
                            @Qualifier("RoleService") final RoleService roleService,
                            @Qualifier("AccountService") final AccountService accountService,
                            @Qualifier("InventoryItemService") final InventoryItemService inventoryItemService) {
        this.holderService = holderService;
        this.roleService = roleService;
        this.accountService = accountService;
        this.inventoryItemService = inventoryItemService;
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolder(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Holder holder = holderService.getByHolderID(uuid);
        return new ResponseEntity<>(holder, HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createInventoryHolder(@RequestBody Holder holder) {
        Holder newHolder = holderService.add(holder);
        return new ResponseEntity<>(newHolder, HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateInventoryHolder(@PathVariable("uuid") @UUIDString @Valid String stringUuid,
                                                   @RequestBody Holder holder) {
        UUID uuid = UUID.fromString(stringUuid);
        Holder holderToUpdate = holderService.getByHolderID(uuid);
        BeanUtils.copyProperties(holder, holderToUpdate, "id");
        Holder updatedHolder = holderService.update(holderToUpdate);
        return new ResponseEntity<>(updatedHolder, HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteInventoryHolder(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Holder holderToDelete = holderService.getByHolderID(uuid);
        holderService.remove(holderToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/role", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolderLinksToRoles(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Holder holderWithRoles = holderService.getByHolderID(uuid);
        return new ResponseEntity<>(holderWithRoles.getRoles(), HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/role", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateInventoryHolderLinksToRoles(@PathVariable("uuid") @UUIDString @Valid String stringUuid,
                                                               @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        UUID uuid = UUID.fromString(stringUuid);
        Holder holderWithRoles = holderService.getByHolderID(uuid);
        Set<Role> currentRoles = holderWithRoles.getRoles();

        Set<UUID> addByIds = createAndDeleteLinksForm.convertAndGetAddIds();
        Set<Role> addRoles = (Set<Role>) roleService.getByRoleIDs(addByIds);
        currentRoles.addAll(addRoles);

        Set<UUID> removeByIds = createAndDeleteLinksForm.convertAndGetRemoveIds();
        Set<Role> removeRoles = (Set<Role>) roleService.getByRoleIDs(removeByIds);
        currentRoles.removeAll(removeRoles);
        Holder holderWithUpdatedRoles = holderService.update(holderWithRoles);

        return new ResponseEntity<>(holderWithUpdatedRoles.getRoles(), HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/account", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolderLinksToAccounts(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Holder holderWithAccounts = holderService.getByHolderID(uuid);
        return new ResponseEntity<>(holderWithAccounts.getAccounts(), HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/account", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateInventoryHolderLinksToAccounts(@PathVariable("uuid") @UUIDString @Valid String stringUuid,
                                                                  @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        UUID uuid = UUID.fromString(stringUuid);
        Holder holderWithAccounts = holderService.getByHolderID(uuid);
        Set<Account> currentAccounts = holderWithAccounts.getAccounts();

        Set<UUID> addByIds = createAndDeleteLinksForm.convertAndGetAddIds();
        Set<Account> accountsToAdd = (Set<Account>) accountService.getByAccountIDs(addByIds);
        currentAccounts.addAll(accountsToAdd);

        Set<UUID> removeByIds = createAndDeleteLinksForm.convertAndGetRemoveIds();
        Set<Account> accountsToDelete = (Set<Account>) accountService.getByAccountIDs(addByIds);
        currentAccounts.removeAll(accountsToDelete);

        Holder holderWithUpdatedAccounts = holderService.update(holderWithAccounts);
        return new ResponseEntity<>(holderWithUpdatedAccounts.getAccounts(), HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/item", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolderLinksToHoldedItems(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Holder holderWithInventoryItems = holderService.getByHolderID(uuid);
        return new ResponseEntity<>(holderWithInventoryItems.getInventoryItems(), HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/item", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateInventoryHolderLinksToHoldedItems(@PathVariable("uuid") @UUIDString @Valid String stringUuid,
                                                                     @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        UUID uuid = UUID.fromString(stringUuid);
        Holder holderWithInventoryItems = holderService.getByHolderID(uuid);
        Set<InventoryItem> currentInventoryItems = holderWithInventoryItems.getInventoryItems();

        //TODO item functional

        Holder update = holderService.update(holderWithInventoryItems);
        return Response.createResponse(update);

    }
}
