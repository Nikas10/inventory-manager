package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.RequisitionDTO;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import com.quartet.inventorydemo.service.RequisitionService;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/requisition")
public class RequisitionController {

  private final RequisitionService requisitionService;
  private final RequisitionProcessService requisitionProcessService;
  private final AccountService accountService;

  @Autowired
  public RequisitionController(
      @Qualifier("RequisitionService") final RequisitionService requisitionService,
      @Qualifier("RequisitionProcessService") final RequisitionProcessService requisitionProcessService,
      @Qualifier("AccountService") final AccountService accountService) {
    this.requisitionService = requisitionService;
    this.requisitionProcessService = requisitionProcessService;
    this.accountService = accountService;
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<?> createReuisition(@RequestBody RequisitionDTO requisitionDTO) {

    String login = requisitionDTO.getLogin();
    Date creationDate = requisitionDTO.getCreationDate();
    String description = requisitionDTO.getDescription();
    Date dueDate = requisitionDTO.getDueDate();
    String status = requisitionDTO.getStatus();
    String stringHolderUUID = requisitionDTO.getStringHolderUUID();
    List<String> stringInventoryPositionUUIDs = requisitionDTO.getStringInventoryPositionUUIDs();

    Requisition newRequisition =
        requisitionService.add(login, creationDate, description, dueDate, status, stringHolderUUID, stringInventoryPositionUUIDs);
    requisitionProcessService.create(newRequisition);
    return new ResponseEntity<>(newRequisition, HttpStatus.OK);
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> getAll() {
    Collection<Requisition> requisitions = requisitionService.getAll();
    return new ResponseEntity<>(requisitions, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
  public ResponseEntity<?> update(
      @PathVariable("id") UUID id, @RequestBody RequisitionDTO requisitionDTO) {
    Optional<Requisition> original = requisitionService.getById(id);

    original.ifPresent(
        currentRequisition -> {
          String oldStatus = currentRequisition.getStatus();
          String newStatus = requisitionDTO.getStatus();

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
