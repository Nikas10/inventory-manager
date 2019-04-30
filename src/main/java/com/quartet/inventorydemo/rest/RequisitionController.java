package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import com.quartet.inventorydemo.service.RequisitionService;
import com.quartet.inventorydemo.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/requisitions")
public class RequisitionController {
    @Autowired
    private RequisitionService requestService;

    @Autowired
    private RequisitionProcessService requisitionProcessService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createReuisition(@RequestBody Requisition requisition) {
        Requisition r = requestService.add(requisition);

        requisitionProcessService.create(r);

        return Response.createResponse(HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        List<Requisition> requisitions = requestService.getAll();

        return Response.createResponse(requisitions);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody Requisition requisition) {
        Optional<Requisition> original = requestService.get(id);

        original.ifPresent(req -> {
            String oldStatus = req.getStatus();
            String newStatus = requisition.getStatus();

            if (!oldStatus.equalsIgnoreCase(newStatus)) {
                switch (newStatus.toUpperCase()) {
                    case "APPROVED":
                        requisitionProcessService.approve(req);
                        break;
                    case "REJECTED":
                        requisitionProcessService.reject(req);
                        break;
                    case "REQUIRE_CLARIFICATION":
                        requisitionProcessService.requestClarification(req, "");
                        break;
                    case "REVIEW_NEEDED":
                        requisitionProcessService.makeChanges(req);
                        break;
                    case "COMPLETED":
                        requisitionProcessService.complete(req);
                        break;
                }
            }

            // TODO: Обработать остальные изменения
        });

        return Response.createResponse(HttpStatus.OK);
    }
}
