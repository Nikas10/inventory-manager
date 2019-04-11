package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.InventoryHolder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.InventoryHolderService;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.service.RoleService;
import com.quartet.inventorydemo.util.CreateAndDeleteLinksForm;
import com.quartet.inventorydemo.util.Response;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/holder")
public class InventoryHolderController {

    @Autowired
    @Qualifier("InventoryHolderService")
    private InventoryHolderService inventoryHolderService;

    @Autowired
    @Qualifier("RoleService")
    private RoleService roleService;

    @Autowired
    @Qualifier("AccountService")
    private AccountService accountService;

    @Autowired
    @Qualifier("InventoryItemService")
    private InventoryItemService inventoryItemService;


    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolder(@PathVariable("uuid") String stringUuid) {
        try {
            InventoryHolder byHolderID = getInventoryHolderById(stringUuid);
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
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory holder name is not correct.");
        }
        boolean nameExists = checkIfHolderWithSameNameExists(requestInventoryHolderName);
        if (nameExists) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory holder with same name already exists.");
        } else {
            InventoryHolder result = inventoryHolderService.add(inventoryHolder);
            return Response.createResponse(result);
        }
    }


    @PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteInventoryHolder(@PathVariable("uuid") String stringUuid) {
        try {
            InventoryHolder byHolderID = getInventoryHolderById(stringUuid);
            inventoryHolderService.remove(byHolderID);
            return Response.createResponse();
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/role", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateInventoryHolderLinksToRoles(@PathVariable("uuid") String stringUuid,
                                                               @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        try {
            InventoryHolder byHolderID = getInventoryHolderById(stringUuid);
            Set<Role> currentRoles = byHolderID.getCurrentRoles();

            Set<UUID> addByIds = createAndDeleteLinksForm.getAddIds();
            Set<Role> addRoles = roleService.getByRoleIDs(addByIds);
            currentRoles.addAll(addRoles);

            Set<UUID> removeByIds = createAndDeleteLinksForm.getRemoveIds();
            Set<Role> removeRoles = roleService.getByRoleIDs(removeByIds);
            currentRoles.removeAll(removeRoles);

            InventoryHolder update = inventoryHolderService.update(byHolderID);
            return Response.createResponse(update);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/account", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateInventoryHolderLinksToAccounts(@PathVariable("uuid") String stringUuid,
                                                                  @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        try {
            InventoryHolder byHolderID = getInventoryHolderById(stringUuid);
            Set<Role> currentRoles = byHolderID.getCurrentRoles();
            Set<Account> employeesWithHolder = byHolderID.getEmployeesWithHolder();

            Set<UUID> addByIds = createAndDeleteLinksForm.getAddIds();
            Set<Account> addAccounts = accountService.getByAccountIDs(addByIds);
            employeesWithHolder.addAll(addAccounts);

            Set<UUID> removeByIds = createAndDeleteLinksForm.getRemoveIds();
            Set<Account> removeAccounts = accountService.getByAccountIDs(addByIds);
            employeesWithHolder.removeAll(removeAccounts);

            InventoryHolder update = inventoryHolderService.update(byHolderID);
            return Response.createResponse(update);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/item", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateInventoryHolderLinksToHoldedItems(@PathVariable("uuid") String stringUuid,
                                                                     @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        try {
            InventoryHolder byHolderID = getInventoryHolderById(stringUuid);
            Set<InventoryItem> holdedItems = byHolderID.getHoldedItems();

            Set<UUID> addByIds = createAndDeleteLinksForm.getAddIds();
            Set<InventoryItem> addInventoryItems = inventoryItemService.getByInventoryItemIDs(addByIds);
            holdedItems.addAll(addInventoryItems);

            Set<UUID> removeByIds = createAndDeleteLinksForm.getRemoveIds();
            Set<InventoryItem> removeInventoryItems = inventoryItemService.getByInventoryItemIDs(removeByIds);
            holdedItems.removeAll(removeInventoryItems);

            InventoryHolder update = inventoryHolderService.update(byHolderID);
            return Response.createResponse(update);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private InventoryHolder getInventoryHolderById(String stringUuid) throws NotFoundException {
        UUID uuid = UUID.fromString(stringUuid);
        InventoryHolder byHolderID = inventoryHolderService.getByHolderID(uuid);
        if (byHolderID == null) {
            throw new NotFoundException("No inventory holder with uuid: " + stringUuid + " found");
        }
        return byHolderID;
    }

    private boolean checkIfHolderWithSameNameExists(String holderName) {
        List<InventoryHolder> byHolderName = inventoryHolderService.getByHolderName(holderName);   //TODO add constraint to db for name uniqueness
        return !byHolderName.isEmpty();   //if list is not empty, then holder with same name already exists
    }
}
