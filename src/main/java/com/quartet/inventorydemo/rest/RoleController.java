package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.service.InventoryHolderService;
import com.quartet.inventorydemo.service.InventoryPositionService;
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
@RequestMapping("api/role")
public class RoleController {

    @Autowired
    @Qualifier("RoleService")
    private RoleService roleService;

    @Autowired
    @Qualifier("InventoryHolderService")
    private InventoryHolderService inventoryHolderService;

    @Autowired
    @Qualifier("InventoryPositionService")
    private InventoryPositionService inventoryPositionService;

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getRole(@PathVariable("uuid") String stringUuid) {
        try {
            Role byRoleID = getRoleById(stringUuid);
            return Response.createResponse(byRoleID);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        String requestRoleName = role.getName();
        if (requestRoleName == null || "".equals(requestRoleName)) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Role name is not correct.");
        }
        boolean nameExists = checkIfRoleWithSameNameExists(requestRoleName);
        if (nameExists) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Role with same name already exists.");
        }
        Role result = roleService.add(role);
        return Response.createResponse(result);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRole(@PathVariable("uuid") String stringUuid,
                                        @RequestBody Role role) {
        try {
            Role byRoleID = getRoleById(stringUuid);

            String requestRoleName = role.getName();
            String requestRoleDescription = role.getDescription();

            if (requestRoleName == null || "".equals(requestRoleName)) {
                return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Role name is not correct.");
            }
            boolean nameExists = checkIfRoleWithSameNameExists(requestRoleName);
            if (nameExists) {
                return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Role with same name already exists.");
            }

            byRoleID.setName(requestRoleName);
            byRoleID.setDescription(requestRoleDescription);

            Role result = roleService.update(byRoleID);
            return Response.createResponse(result);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRole(@PathVariable("uuid") String stringUuid) {
        try {
            Role byRoleID = getRoleById(stringUuid);
            roleService.remove(byRoleID);
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
    @RequestMapping(value = "/{uuid}/holder", method = RequestMethod.GET)
    public ResponseEntity<?> getRoleLinksToInventoryHolders(@PathVariable("uuid") String stringUuid) {
        try {
            Role roleById = getRoleById(stringUuid);
            Set<Holder> inventoryHolders = roleById.getHolders();
            return Response.createResponse(inventoryHolders);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/position", method = RequestMethod.GET)
    public ResponseEntity<?> getRoleLinksToInventoryPosition(@PathVariable("uuid") String stringUuid) {
        try {
            Role roleById = getRoleById(stringUuid);
            Set<InventoryPosition> roleInventoryPositions = roleById.getInventoryPositions();
            return Response.createResponse(roleInventoryPositions);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/position", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateRoleLinksToInventoryPosition(@PathVariable("uuid") String stringUuid,
                                                                @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {
        try {
            Role roleById = getRoleById(stringUuid);
            Set<InventoryPosition> roleInventoryPositions = roleById.getInventoryPositions();

            Set<UUID> addByIds = createAndDeleteLinksForm.getAddIds();
            Set<InventoryPosition> addPositions = inventoryPositionService.getByPositionIDs(addByIds);
            roleInventoryPositions.addAll(addPositions);

            Set<UUID> removeByIds = createAndDeleteLinksForm.getRemoveIds();
            Set<InventoryPosition> removePositions = inventoryPositionService.getByPositionIDs(removeByIds);
            roleInventoryPositions.removeAll(removePositions);

            Role update = roleService.update(roleById);
            return Response.createResponse(update);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private Role getRoleById(String stringUuid) throws NotFoundException {
        UUID uuid = UUID.fromString(stringUuid);
        Role byRoleID = roleService.getByRoleID(uuid);
        if (byRoleID == null) {
            throw new NotFoundException("no role with uuid: " + stringUuid + " found");
        }
        return byRoleID;
    }

    private boolean checkIfRoleWithSameNameExists(String roleName) {
        Role byRoleName = roleService.getByRoleName(roleName);
        return byRoleName != null;
    }
}