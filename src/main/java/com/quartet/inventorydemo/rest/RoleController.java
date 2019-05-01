package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.CreateAndDeleteLinksForm;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/role")
@Validated
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(@Qualifier("RoleService") final RoleService roleService) {
        this.roleService = roleService;
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getRole(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Optional<Role> roleOptional = roleService.getByRoleID(uuid);
        roleOptional.orElseThrow(() -> new ResourceNotFoundException("Role with id: " + uuid + "not found"));
        return new ResponseEntity<>(roleOptional.get(), HttpStatus.OK);
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
        Role updatedRole = roleService.update(uuid, role);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }


    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRole(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        roleService.remove(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/holder", method = RequestMethod.GET)
    public ResponseEntity<?> getRoleLinksToInventoryHolders(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Optional<Role> roleOptional = roleService.getByRoleID(uuid);
        Role roleWithHolders = roleOptional.orElseThrow(() -> new ResourceNotFoundException("Role with id: " + uuid + "not found"));
        return new ResponseEntity<>(roleWithHolders.getHolders(), HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/{uuid}/position", method = RequestMethod.GET)
    public ResponseEntity<?> getRoleLinksToInventoryPosition(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
        UUID uuid = UUID.fromString(stringUuid);
        Optional<Role> roleOptional = roleService.getByRoleID(uuid);
        Role roleWithInventoryPositions = roleOptional.orElseThrow(() -> new ResourceNotFoundException("Role with id: " + uuid + "not found"));
        return new ResponseEntity<>(roleWithInventoryPositions.getInventoryPositions(), HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/{uuid}/position", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateRoleLinksToInventoryPosition(@PathVariable("uuid") @UUIDString @Valid String stringUuid,
                                                                @RequestBody CreateAndDeleteLinksForm createAndDeleteLinksForm) {

        UUID uuid = UUID.fromString(stringUuid);
        Set<UUID> addByIds = createAndDeleteLinksForm.convertAndGetAddIds();
        Set<UUID> removeByIds = createAndDeleteLinksForm.convertAndGetRemoveIds();
        Role result = null;
        if (!addByIds.isEmpty()) {
            result = roleService.addInventoryPositions(uuid, addByIds);
        }
        if (!removeByIds.isEmpty()) {
            result = roleService.removeInventoryPositions(uuid, removeByIds);
        }
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result.getInventoryPositions(), HttpStatus.OK);
    }
}