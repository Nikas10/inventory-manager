package com.quartet.inventorydemo.rest;

import static java.util.Objects.isNull;

import com.quartet.inventorydemo.dto.RequisitionDTO;
import com.quartet.inventorydemo.dto.RequisitionInventoryPositionDTO;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.exception.UpdateNotSupportedException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import com.quartet.inventorydemo.service.RequisitionService;
import com.quartet.inventorydemo.service.Requisition_InventoryPositionService;
import com.quartet.inventorydemo.util.UUIDString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("api/requisitions")
public class RequisitionController {

  private final InventoryPositionService positionService;
  private final RequisitionService requisitionService;
  private final RequisitionProcessService requisitionProcessService;
  private final AccountService accountService;
  private final Requisition_InventoryPositionService requisition_InventoryPositionService;

  @Autowired
  public RequisitionController(
      @Qualifier("RequisitionService") final RequisitionService requisitionService,
      @Qualifier("RequisitionProcessService") final RequisitionProcessService requisitionProcessService,
      @Qualifier("InventoryPositionService") final InventoryPositionService positionService,
      @Qualifier("AccountService") final AccountService accountService,
      @Qualifier("Requisition_InventoryPositionService") final Requisition_InventoryPositionService requisition_InventoryPositionService) {
    this.requisitionService = requisitionService;
    this.requisitionProcessService = requisitionProcessService;
    this.positionService = positionService;
    this.accountService = accountService;
    this.requisition_InventoryPositionService = requisition_InventoryPositionService;
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/", method = RequestMethod.POST)
  public ResponseEntity<?> createRequisition(@RequestBody RequisitionDTO requisitionDTO) {
    Requisition newRequisition =
        requisitionService.add(requisitionDTO);
    requisitionProcessService.create(newRequisition);
    RequisitionInventoryPositionDTO requisitionInventoryPositionDTO = new RequisitionInventoryPositionDTO();
    List<RequisitionInventoryPositionDTO> requisition_inventoryPositionDTOs = new ArrayList<>();
    for (Requisition_InventoryPosition current : newRequisition
        .getRequisitionInventoryPositions()) {
      requisition_inventoryPositionDTOs.add(
          new RequisitionInventoryPositionDTO(current.getInventoryPosition().getId().toString(),
              current.getAmount(),
              current.getInventoryPosition().getName(),
              current.getInventoryPosition().getDescription()));
    }
    requisitionInventoryPositionDTO
        .setAmount(requisitionDTO.getInventoryPositions().get(0).getAmount());
    RequisitionDTO resultRequisitionDTO = new RequisitionDTO(newRequisition.getId().toString(),
        newRequisition.getAccount().getLogin(),
        null,
        newRequisition.getStatus(),
        newRequisition.getCreationDate(),
        newRequisition.getDueDate(),
        newRequisition.getDescription(),
        newRequisition.getHolder().getName(),
        newRequisition.getHolder().getId().toString(),
        requisition_inventoryPositionDTOs);
    return new ResponseEntity<>(resultRequisitionDTO, HttpStatus.OK);
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
            e.getRequisitionInventoryPositions()
                .parallelStream()
                .map(x -> new RequisitionInventoryPositionDTO(
                    x.getInventoryPosition().getId().toString(),
                    x.getAmount(),
                    x.getInventoryPosition().getName(),
                    x.getInventoryPosition().getDescription()
                ))
                .collect(Collectors.toList())
        ))
        .collect(Collectors.toList());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> getById(
      @PathVariable("id") @NotBlank @Valid @UUIDString String id) {
    UUID requestId = UUID.fromString(id);
    Requisition requisition = requisitionService.getById(requestId);
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
        requisition.getRequisitionInventoryPositions()
            .parallelStream()
            .map(x -> new RequisitionInventoryPositionDTO(
                x.getInventoryPosition().getId().toString(),
                x.getAmount(),
                x.getInventoryPosition().getName(),
                x.getInventoryPosition().getDescription()
            ))
            .collect(Collectors.toList()));
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}/positions/", method = RequestMethod.GET)
  public ResponseEntity<?> getPositionsById(
      @PathVariable("id") @NotBlank @Valid @UUIDString String id) {
    UUID requestId = UUID.fromString(id);
    Requisition requisition = requisitionService.getById(requestId);
    List<RequisitionInventoryPositionDTO> requestedItems =
        requisition.getRequisitionInventoryPositions()
            .parallelStream()
            .map(e -> new RequisitionInventoryPositionDTO(
                e.getInventoryPosition().getId().toString(),
                e.getAmount(),
                e.getInventoryPosition().getName(),
                e.getInventoryPosition().getDescription()
            ))
            .collect(Collectors.toList());
    return new ResponseEntity<>(requestedItems, HttpStatus.OK);
  }

  @RequestMapping(value = "/{requisitionId}/positions/", method = RequestMethod.POST)
  public ResponseEntity<?> addNewPositionLink(
      @PathVariable("requisitionId") @NotBlank @Valid @UUIDString String requisitionId,
      @RequestBody RequisitionInventoryPositionDTO addUpdatePositionDTO) {
    UUID requestId = UUID.fromString(requisitionId);
    UUID posId = UUID.fromString(addUpdatePositionDTO.getId());
    Requisition requisition = requisitionService.getById(requestId);
    InventoryPosition position = positionService.getByPositionID(posId).orElseThrow(
        () -> new ResourceNotFoundException("Position with id " + posId + " is not found!"));
    Optional<Requisition_InventoryPosition> validation = requisition
        .getRequisitionInventoryPositions()
        .stream()
        .filter(e -> e.getInventoryPosition().equals(position))
        .findFirst();
    if (validation.isPresent()) {
      throw new UpdateNotSupportedException("Trying to add an already existing link!");
    }
    Requisition_InventoryPosition linkToAdd = new Requisition_InventoryPosition(position,
        requisition, addUpdatePositionDTO.getAmount());
    requisition.getRequisitionInventoryPositions().add(linkToAdd);
    requisitionService.update(requisition);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/{requisitionId}/positions/{positionId}", method = RequestMethod.PATCH)
  public ResponseEntity<?> updatePositionLink(
      @PathVariable("requisitionId") @NotBlank @Valid @UUIDString String requisitionId,
      @PathVariable("positionId") @NotBlank @Valid @UUIDString String positionId,
      @RequestBody RequisitionInventoryPositionDTO amount) {
    UUID requestId = UUID.fromString(requisitionId);
    UUID posId = UUID.fromString(positionId);
    Integer amountVal = amount.getAmount();
    requisition_InventoryPositionService.update(requestId, posId, amountVal);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{requisitionId}/positions/{positionId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> removePositionsLinkById(
      @PathVariable("requisitionId") @NotBlank @Valid @UUIDString String requisitionId,
      @PathVariable("positionId") @NotBlank @Valid @UUIDString String positionId) {
    UUID requestId = UUID.fromString(requisitionId);
    UUID posId = UUID.fromString(positionId);
    requisition_InventoryPositionService.remove(requestId, posId);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
  public ResponseEntity<?> update(
      @PathVariable("id") @NotBlank @Valid @UUIDString String id,
      @RequestBody RequisitionDTO requisitionDTO) {
    UUID reqId = UUID.fromString(id);
    Requisition original = requisitionService.getById(reqId);
    String oldStatus = original.getStatus();
    String newStatus = requisitionDTO.getStatus();

    if (newStatus != null && !oldStatus.equalsIgnoreCase(newStatus)) {
      switch (newStatus.toUpperCase()) {
        case "APPROVED":
          requisitionProcessService.approve(original);
          break;
        case "REJECTED":
          requisitionProcessService.reject(original);
          break;
        case "REQUIRE_CLARIFICATION":
          requisitionProcessService.requestClarification(original, "");
          break;
        case "REVIEW_NEEDED":
          requisitionProcessService.makeChanges(original);
          break;
        case "COMPLETED":
          requisitionProcessService.complete(original);
          break;
      }
    }
    String assignedLogin = requisitionDTO.getAssignedTo();
    if (StringUtils.isNotBlank(assignedLogin)) {
      Account assignedTo = accountService.getByLogin(assignedLogin)
          .orElseThrow(() -> new ResourceNotFoundException(
              "User with name " + assignedLogin + "is not found."));
      original.setAssignedtoAccount(assignedTo);
    }
    String description = requisitionDTO.getDescription();
    if (StringUtils.isNotBlank(description)) {
      original.setDescription(description);
    }
    Date dueDate = requisitionDTO.getDueDate();
    if (!isNull(dueDate)) {
      original.setDueDate(dueDate);
    }
    String holder = requisitionDTO.getHolderUUID();
    if (StringUtils.isNotBlank(holder)) {
      Holder toSet = original.getAccount()
          .getHolders()
          .stream()
          .filter(entry -> entry.getId().equals(UUID.fromString(holder)))
          .findFirst()
          .orElseThrow(() -> new ResourceNotFoundException(
              "Holder with id " + holder + "is not found."));
      original.setHolder(toSet);
    }

    requisitionService.update(original);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);

  }
}
