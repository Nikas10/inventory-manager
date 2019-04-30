package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.CreateAndDeleteLinksForm;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RoleService;
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
@RequestMapping("api/role")
@Validated
public class RoleController {
    private final RoleService roleService;
    private final HolderService holderService;
    private final InventoryPositionService inventoryPositionService;

    @Autowired
    public RoleController(@Qualifier("RoleService") final RoleService roleService,
                          @Qualifier("HolderService") final HolderService holderService,
                          @Qualifier("InventoryPositionService") final InventoryPositionService inventoryPositionService) {
        this.roleService = roleService;
        this.holderService = holderService;
        this.inventoryPositionService = inventoryPositionService;
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getRole(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Role role = roleService.getByRoleID(uuid);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        Role newRole = roleService.add(role);
        return new ResponseEntity<>(newRole, HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRole(@PathVariable("uuid") @UUIDString @Valid String stringUuid,
                                        @RequestBody Role role) {
        UUID uuid = UUID.fromString(stringUuid);
        Role roleToUpdate = roleService.getByRoleID(uuid);
        BeanUtils.copyProperties(role, roleToUpdate, "id");
        Role updatedRole = roleService.update(roleToUpdate);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }


    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRole(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Role roleToDelete = roleService.getByRoleID(uuid);
        roleService.remove(roleToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/holder", method = RequestMethod.GET)
    public ResponseEntity<?> getRoleLinksToInventoryHolders(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Role roleWithHolders = roleService.getByRoleID(uuid);
        return new ResponseEntity<>(roleWithHolders.getHolders(), HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/position", method = RequestMethod.GET)
    public ResponseEntity<?> getRoleLinksToInventoryPosition(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Role roleWithInventoryPositions = roleService.getByRoleID(uuid);
        return new ResponseEntity<>(roleWithInventoryPositions.getInventoryPositions(), HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/position", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateRoleLinksToInventoryPosition(@PathVariable("uuid") @UUIDString @Valid String stringUuid,
                                                                @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {

        UUID uuid = UUID.fromString(stringUuid);
        Role roleWithInventoryPositions = roleService.getByRoleID(uuid);
        Set<InventoryPosition> currentInventoryPositions = roleWithInventoryPositions.getInventoryPositions();

        Set<UUID> addByIds = createAndDeleteLinksForm.convertAndGetAddIds();
        Set<InventoryPosition> inventoryPositionsToAdd = (Set<InventoryPosition>) inventoryPositionService.getByPositionIDs(addByIds);
        currentInventoryPositions.addAll(inventoryPositionsToAdd);

        Set<UUID> removeByIds = createAndDeleteLinksForm.convertAndGetRemoveIds();
        Set<InventoryPosition> inventoryPositionsToRemove = (Set<InventoryPosition>) inventoryPositionService.getByPositionIDs(removeByIds);
        currentInventoryPositions.removeAll(inventoryPositionsToRemove);

        Role roleWithUpdatedInventoryPositions = roleService.update(roleWithInventoryPositions);
        return new ResponseEntity<>(roleWithUpdatedInventoryPositions.getInventoryPositions(), HttpStatus.OK);
    }
}