package com.quartet.inventorydemo.rest;

import static java.util.Objects.isNull;
import com.quartet.inventorydemo.dto.RequisitionDTO;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import com.quartet.inventorydemo.service.RequisitionService;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("api/requisitions")
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
  @RequestMapping(value = "/new", method = RequestMethod.POST)
  public ResponseEntity<?> createRequisition(@RequestBody RequisitionDTO requisitionDTO) {

    String login = requisitionDTO.getLogin();
    Date creationDate = requisitionDTO.getCreationDate();
    String description = requisitionDTO.getDescription();
    Date dueDate = requisitionDTO.getDueDate();
    String status = requisitionDTO.getStatus();
    String holderUUID = requisitionDTO.getHolderUUID();
    List<String> inventoryPositionUUIDs = requisitionDTO.getInventoryPositionUUIDs();

    Requisition newRequisition =
        requisitionService.add(login, creationDate, description, dueDate, status, UUID.fromString(holderUUID), inventoryPositionUUIDs);
    requisitionProcessService.create(newRequisition);
    return new ResponseEntity<>(newRequisition, HttpStatus.OK);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ResponseEntity<?> getAll() {
    Collection<Requisition> requisitions = requisitionService.getAll();
    return new ResponseEntity<>(requisitions, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> getById(
      @PathVariable("id") UUID id) {
    Requisition requisition = requisitionService.getById(id).orElseThrow(
        () -> new ResourceNotFoundException("Requisition with id " + id + " is not found!"));
    RequisitionDTO result = new RequisitionDTO(
        requisition.getAccount().getLogin(),
        isNull(requisition.getAssignedtoAccount())
            ? StringUtils.EMPTY : requisition.getAssignedtoAccount().getLogin(),
        requisition.getStatus(),
        requisition.getCreationDate(),
        requisition.getDueDate(),
        requisition.getDescription(),
        requisition.getHolder().getName(),
        requisition.getHolder().getId().toString(),
        Collections.emptyList());
    result.setId(requisition.getId().toString());
    return new ResponseEntity<>(result, HttpStatus.OK);
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
