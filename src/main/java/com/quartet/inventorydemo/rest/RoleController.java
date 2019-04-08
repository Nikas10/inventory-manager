package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.service.RoleService;
import com.quartet.inventorydemo.util.Response;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/role")
public class RoleController {

    @Autowired
    @Qualifier("RoleService")
    private RoleService roleService;

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getRole(@PathVariable("uuid") String stringUuid) {
        try {
            UUID uuid = UUID.fromString(stringUuid);
            Role byRoleID = roleService.getByRoleID(uuid);
            if (byRoleID == null) {
                throw new NotFoundException("no role with uuid: " + stringUuid + " found");
            }
            return Response.createResponse(byRoleID);
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        String requestRoleName = role.getName();
        if (requestRoleName == null || "".equals(requestRoleName)) {
            boolean nameExists = checkIfRoleWithSameNameExists(requestRoleName);
            if (nameExists) {
                return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Role with same name already exists.");
            } else {
                Role result = roleService.add(role);
                return Response.createResponse(result);
            }
        } else {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Role with same name already exists.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRole(@PathVariable("uuid") String stringUuid) {
        try {
            UUID uuid = UUID.fromString(stringUuid);
            Role byRoleID = roleService.getByRoleID(uuid);
            if (byRoleID == null) {
                throw new NotFoundException("no role with uuid: " + stringUuid + " found");
            }
            roleService.remove(byRoleID);
            return Response.createResponse();
        } catch (IllegalArgumentException e) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "UUID is not correct");
        } catch (NotFoundException e) {
            return Response.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private boolean checkIfRoleWithSameNameExists(String roleName) {
        List<Role> byRoleName = roleService.findByRoleName(roleName);   //TODO add constraint to db for name uniqueness
        return !byRoleName.isEmpty();   //if list is not empty, then role with same name already exists
    }
}