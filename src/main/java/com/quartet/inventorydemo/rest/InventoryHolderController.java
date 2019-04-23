package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Holder;
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
import org.springframework.web.bind.annotation.*;

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


    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolder(@PathVariable("uuid") String stringUuid) {
        try {
            Holder byHolderID = getInventoryHolderById(stringUuid);
            return Response.createResponse(byHolderID);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createInventoryHolder(@RequestBody Holder inventoryHolder) {
        String requestInventoryHolderName = inventoryHolder.getName();
        if (requestInventoryHolderName == null || "".equals(requestInventoryHolderName)) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory holder name is not correct.");
        }
        boolean nameExists = checkIfHolderWithSameNameExists(requestInventoryHolderName);
        if (nameExists) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory holder with same name already exists.");
        }

        Holder result = inventoryHolderService.add(inventoryHolder);
        return Response.createResponse(result);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateInventoryHolder(@PathVariable("uuid") String stringUuid,
                                                   @RequestBody Holder inventoryHolder) {
        try {
            Holder byHolderID = getInventoryHolderById(stringUuid);

            String requestInventoryHolderName = inventoryHolder.getName();
            String requestInventoryHolderDescription = inventoryHolder.getDescription();

            if (requestInventoryHolderName == null || "".equals(requestInventoryHolderName)) {
                return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory holder name is not correct.");
            }
            boolean nameExists = checkIfHolderWithSameNameExists(requestInventoryHolderName);
            if (nameExists) {
                return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Inventory holder with same name already exists.");
            }

            byHolderID.setName(requestInventoryHolderName);
            byHolderID.setDescription(requestInventoryHolderDescription);

            Holder result = inventoryHolderService.update(byHolderID);
            return Response.createResponse(result);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteInventoryHolder(@PathVariable("uuid") String stringUuid) {
        try {
            Holder byHolderID = getInventoryHolderById(stringUuid);
            inventoryHolderService.remove(byHolderID);
            return Response.createResponse();
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DeletionNotSupportedException e) {
            return Response.createErrorResponse(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/role", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolderLinksToRoles(@PathVariable("uuid") String stringUuid) {
        try {
            Holder byHolderID = getInventoryHolderById(stringUuid);
            Set<Role> currentRoles = byHolderID.getRoles();
            return Response.createResponse(currentRoles);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/role", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateInventoryHolderLinksToRoles(@PathVariable("uuid") String stringUuid,
                                                               @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        try {
            Holder byHolderID = getInventoryHolderById(stringUuid);
            Set<Role> currentRoles = byHolderID.getRoles();

            Set<UUID> addByIds = createAndDeleteLinksForm.getAddIds();
            Set<Role> addRoles = roleService.getByRoleIDs(addByIds);
            currentRoles.addAll(addRoles);

            Set<UUID> removeByIds = createAndDeleteLinksForm.getRemoveIds();
            Set<Role> removeRoles = roleService.getByRoleIDs(removeByIds);
            currentRoles.removeAll(removeRoles);

            Holder update = inventoryHolderService.update(byHolderID);
            return Response.createResponse(update);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/account", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolderLinksToAccounts(@PathVariable("uuid") String stringUuid) {
        try {
            Holder byHolderID = getInventoryHolderById(stringUuid);
            Set<Account> employeesWithHolder = byHolderID.getAccounts();
            return Response.createResponse(employeesWithHolder);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/account", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateInventoryHolderLinksToAccounts(@PathVariable("uuid") String stringUuid,
                                                                  @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        try {
            Holder byHolderID = getInventoryHolderById(stringUuid);
            Set<Account> employeesWithHolder = byHolderID.getAccounts();

            Set<UUID> addByIds = createAndDeleteLinksForm.getAddIds();
            Set<Account> addAccounts = accountService.getByAccountIDs(addByIds);
            employeesWithHolder.addAll(addAccounts);

            Set<UUID> removeByIds = createAndDeleteLinksForm.getRemoveIds();
            Set<Account> removeAccounts = accountService.getByAccountIDs(addByIds);
            employeesWithHolder.removeAll(removeAccounts);

            Holder update = inventoryHolderService.update(byHolderID);
            return Response.createResponse(update);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/item", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryHolderLinksToHoldedItems(@PathVariable("uuid") String stringUuid) {
        try {
            Holder byHolderID = getInventoryHolderById(stringUuid);
            Set<InventoryItem> holdedItems = byHolderID.getInventoryItems();
            return Response.createResponse(holdedItems);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/item", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateInventoryHolderLinksToHoldedItems(@PathVariable("uuid") String stringUuid,
                                                                     @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        try {
            Holder byHolderID = getInventoryHolderById(stringUuid);
            Set<InventoryItem> holdedItems = byHolderID.getInventoryItems();

          /*  Set<UUID> addByIds = createAndDeleteLinksForm.getAddIds();
            Set<InventoryItem> addInventoryItems = inventoryItemService.getByInventoryItemIDs(addByIds);
            holdedItems.addAll(addInventoryItems);

            Set<UUID> removeByIds = createAndDeleteLinksForm.getRemoveIds();
            Set<InventoryItem> removeInventoryItems = inventoryItemService.getByInventoryItemIDs(removeByIds);
            holdedItems.removeAll(removeInventoryItems);*/

            Holder update = inventoryHolderService.update(byHolderID);
            return Response.createResponse(update);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private Holder getInventoryHolderById(String stringUuid) throws NotFoundException {
        UUID uuid = UUID.fromString(stringUuid);
        Holder byHolderID = inventoryHolderService.getByHolderID(uuid);
        if (byHolderID == null) {
            throw new NotFoundException("No inventory holder with uuid: " + stringUuid + " found");
        }
        return byHolderID;
    }

    private boolean checkIfHolderWithSameNameExists(String holderName) {
        try {
            Holder byHolderName = inventoryHolderService.getByHolderName(holderName);
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }
}
