package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import com.quartet.inventorydemo.service.RequisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/requisition")
public class RequisitionController {
    private final RequisitionService requisitionService;
    private final RequisitionProcessService requisitionProcessService;

    @Autowired
    public RequisitionController(@Qualifier("RequisitionService") final RequisitionService requisitionService,
                                 @Qualifier("RequisitionProcessService") final RequisitionProcessService requisitionProcessService) {
        this.requisitionService = requisitionService;
        this.requisitionProcessService = requisitionProcessService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createReuisition(@RequestBody Requisition requisition) {
        Requisition newRequisition = requisitionService.add(requisition);
        requisitionProcessService.create(newRequisition);
        return new ResponseEntity<>(newRequisition, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Collection<Requisition> requisitions = requisitionService.getAll();
        return new ResponseEntity<>(requisitions, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody Requisition requisition) {
        Optional<Requisition> original = requisitionService.getById(id);

        original.ifPresent(currentRequisition -> {
            String oldStatus = currentRequisition.getStatus();
            String newStatus = requisition.getStatus();

            if (!oldStatus.equalsIgnoreCase(newStatus)) {
                switch (newStatus.toUpperCase()) {
                    case "APPROVED":
                        requisitionProcessService.approve(currentRequisition);
                        break;
                    case "REJECTED":
                        requisitionProcessService.reject(currentRequisition);
                        break;
                    case "REQUIRE_CLARIFICATION":
                        requisitionProcessService.requestClarification(currentRequisition, "");
                        break;
                    case "REVIEW_NEEDED":
                        requisitionProcessService.makeChanges(currentRequisition);
                        break;
                    case "COMPLETED":
                        requisitionProcessService.complete(currentRequisition);
                        break;
                }
            }

            // TODO: Обработать остальные изменения
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
