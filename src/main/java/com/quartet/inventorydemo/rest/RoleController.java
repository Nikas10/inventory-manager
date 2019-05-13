package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.CreateAndDeleteLinksForm;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.service.RoleService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/roles")
@Validated
public class RoleController {

  private final RoleService roleService;

  @Autowired
  public RoleController(@Qualifier("RoleService") final RoleService roleService) {
    this.roleService = roleService;
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
  public ResponseEntity<?> getRole(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
    UUID uuid = UUID.fromString(stringUuid);
    Optional<Role> roleOptional = roleService.getByRoleID(uuid);
    roleOptional.orElseThrow(
        () -> new ResourceNotFoundException("Role with id: " + uuid + "not found"));
    return new ResponseEntity<>(roleOptional.get(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ResponseEntity<?> getRoles() {
    return new ResponseEntity<>(roleService.getAll(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/new", method = RequestMethod.POST)
  public ResponseEntity<?> createRole(@RequestBody Role role) {
    Role newRole = roleService.add(role);
    return new ResponseEntity<>(newRole, HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateRole(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid, @RequestBody Role role) {
    UUID uuid = UUID.fromString(stringUuid);
    Role updatedRole = roleService.update(uuid, role);
    return new ResponseEntity<>(updatedRole, HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteRole(@PathVariable("uuid") @UUIDString @Valid String stringUuid) {
    UUID uuid = UUID.fromString(stringUuid);
    roleService.remove(uuid);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/{uuid}/holders/", method = RequestMethod.GET)
  public ResponseEntity<?> getRoleLinksToInventoryHolders(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid) {
    UUID uuid = UUID.fromString(stringUuid);
    Optional<Role> roleOptional = roleService.getByRoleID(uuid);
    Role roleWithHolders =
        roleOptional.orElseThrow(
            () -> new ResourceNotFoundException("Role with id: " + uuid + "not found"));
    return new ResponseEntity<>(roleWithHolders.getHolders(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/{uuid}/positions/", method = RequestMethod.GET)
  public ResponseEntity<?> getRoleLinksToInventoryPosition(
      @PathVariable("uuid") @UUIDString @Valid String stringUuid) {
    UUID uuid = UUID.fromString(stringUuid);
    Optional<Role> roleOptional = roleService.getByRoleID(uuid);
    Role roleWithInventoryPositions =
        roleOptional.orElseThrow(
            () -> new ResourceNotFoundException("Role with id: " + uuid + "not found"));
    return new ResponseEntity<>(roleWithInventoryPositions.getInventoryPositions(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{roleId}/positions", method = RequestMethod.POST)
  public ResponseEntity<?> addLinkToInventoryPosition(
      @PathVariable("roleId") @UUIDString @Valid String roleId,
      @RequestParam("positionId") @UUIDString @Valid String positionId) {
    UUID role = UUID.fromString(roleId);
    UUID position = UUID.fromString(positionId);
    roleService.addInventoryPosition(role, position);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{roleId}/positions/{positionId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> removeLinkFromInventoryPosition(
      @PathVariable("roleId") @UUIDString @Valid String roleId,
      @PathVariable("positionId") @UUIDString @Valid String positionId) {
    UUID role = UUID.fromString(roleId);
    UUID position = UUID.fromString(positionId);
    roleService.removeInventoryPosition(role, position);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
