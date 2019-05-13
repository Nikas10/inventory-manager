package com.quartet.inventorydemo.rest;

import static java.util.Objects.isNull;
import com.quartet.inventorydemo.dto.RequisitionDTO;
import com.quartet.inventorydemo.dto.RequisitionInventoryPositionDTO;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import com.quartet.inventorydemo.service.RequisitionService;
import com.quartet.inventorydemo.util.UUIDString;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.plugin.dom.exception.InvalidStateException;

@RestController
@RequestMapping("api/requisitions")
public class RequisitionController {

  private final InventoryPositionService positionService;
  private final RequisitionService requisitionService;
  private final RequisitionProcessService requisitionProcessService;
  private final AccountService accountService;

  @Autowired
  public RequisitionController(
      @Qualifier("RequisitionService") final RequisitionService requisitionService,
      @Qualifier("RequisitionProcessService") final RequisitionProcessService requisitionProcessService,
      @Qualifier("InventoryPositionService") final InventoryPositionService positionService,
      @Qualifier("AccountService") final AccountService accountService) {
    this.requisitionService = requisitionService;
    this.requisitionProcessService = requisitionProcessService;
    this.positionService = positionService;
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
    List<RequisitionDTO> result = requisitionService.getAll().parallelStream()
        .map(e -> new RequisitionDTO(
            e.getId().toString(),
            e.getAccount().getLogin(),
            isNull(e.getAssignedtoAccount())
                ? StringUtils.EMPTY : e.getAssignedtoAccount().getLogin(),
            e.getStatus(),
            e.getCreationDate(),
            e.getDueDate(),
            e.getDescription(),
            e.getHolder().getName(),
            e.getHolder().getId().toString(),
            Collections.emptyList()
        )).collect(Collectors.toList());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> getById(
      @PathVariable("id") @NotBlank @UUIDString String id) {
    UUID requestId = UUID.fromString(id);
    Requisition requisition = requisitionService.getById(requestId).orElseThrow(
        () -> new ResourceNotFoundException("Requisition with id " + id + " is not found!"));
    RequisitionDTO result = new RequisitionDTO(
        requisition.getId().toString(),
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
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}/positions", method = RequestMethod.GET)
  public ResponseEntity<?> getPositionsById(
      @PathVariable("id") @NotBlank @UUIDString String id) {
    UUID requestId = UUID.fromString(id);
    Requisition requisition = requisitionService.getById(requestId).orElseThrow(
        () -> new ResourceNotFoundException("Requisition with id " + id + " is not found!"));
    List<RequisitionInventoryPositionDTO> requestedItems =
        requisition.getRequisitionInventoryPositions()
            .parallelStream()
            .map(e -> new RequisitionInventoryPositionDTO(
            e.getInventoryPosition().getId().toString(),
            e.getInventoryPosition().getName(),
            e.getInventoryPosition().getDescription(),
            e.getAmount()))
            .collect(Collectors.toList());
    return new ResponseEntity<>(requestedItems, HttpStatus.OK);
  }

  @RequestMapping(value = "/{requisitionId}/positions", method = RequestMethod.POST)
  public ResponseEntity<?> addNewPositionLink(
      @PathVariable("requisitionId") @NotBlank @UUIDString String requisitionId,
      @RequestParam("positionId") @NotBlank @UUIDString String positionId,
      @RequestParam("amount") @NotNull @Positive Integer amount) {
    UUID requestId = UUID.fromString(requisitionId);
    UUID posId = UUID.fromString(positionId);
    Requisition requisition = requisitionService.getById(requestId).orElseThrow(
        () -> new ResourceNotFoundException("Requisition with id " + requestId + " is not found!"));
    InventoryPosition position = positionService.getByPositionID(posId).orElseThrow(
        () -> new ResourceNotFoundException("Position with id " + posId + " is not found!"));
    Optional<Requisition_InventoryPosition> validation = requisition
        .getRequisitionInventoryPositions()
        .stream()
        .filter(e -> e.getInventoryPosition().equals(position))
        .findFirst();
    if (validation.isPresent()) {
      throw new InvalidStateException("Trying to add an already existing link!");
    }
    Requisition_InventoryPosition linkToAdd = new Requisition_InventoryPosition(position, requisition, amount);
    requisition.getRequisitionInventoryPositions().add(linkToAdd);
    requisitionService.update(requisition);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{requisitionId}/positions/{positionId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> removePositionsLinkById(
      @PathVariable("requisitionId") @NotBlank @UUIDString String requisitionId,
      @RequestParam("positionId") @NotBlank @UUIDString String positionId) {
    UUID requestId = UUID.fromString(requisitionId);
    UUID posId = UUID.fromString(positionId);
    Requisition requisition = requisitionService.getById(requestId).orElseThrow(
        () -> new ResourceNotFoundException("Requisition with id " + requestId + " is not found!"));
    InventoryPosition position = positionService.getByPositionID(posId).orElseThrow(
        () -> new ResourceNotFoundException("Position with id " + posId + " is not found!"));
    Requisition_InventoryPosition linkToRemove = requisition
        .getRequisitionInventoryPositions()
        .stream()
        .filter(e -> e.getInventoryPosition().equals(position))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Link between requisition and position does not exist!"));
    requisition.getRequisitionInventoryPositions().remove(linkToRemove);
    requisitionService.update(requisition);
    return new ResponseEntity<>(HttpStatus.CREATED);
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
