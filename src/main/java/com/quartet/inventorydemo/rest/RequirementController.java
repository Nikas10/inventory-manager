package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.service.RequirementService;
import com.quartet.inventorydemo.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/requirement")
public class RequirementController {

    private RequirementService requirementService;

    @Autowired
    public RequirementController(@Qualifier("RequirementService") final RequirementService requirementService) {

        this.requirementService = requirementService;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable("name") String name) {
        Requirement requirement = requirementService.getByRequirementName(name);

        if (requirement != null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement already exists.");
        }

        requirementService.add(new Requirement(name));

        return Response.createResponse(requirement);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        Requirement requirement = requirementService.getByRequirementID(id);

        if (requirement == null) {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Requirement not found.");
        }

        requirementService.remove(requirement);

        return Response.createResponse(requirement);
    }
}
