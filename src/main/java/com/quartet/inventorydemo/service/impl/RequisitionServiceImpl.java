package com.quartet.inventorydemo.service.impl;

import static java.util.Objects.isNull;
import com.quartet.inventorydemo.dto.AddUpdatePositionDTO;
import com.quartet.inventorydemo.dto.RequisitionDTO;
import com.quartet.inventorydemo.exception.ResourceNotAvailableException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.repository.RequisitionRepository;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequisitionService;
import com.quartet.inventorydemo.service.Requisition_InventoryPositionService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service("RequisitionService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class RequisitionServiceImpl implements RequisitionService {

  private final RequisitionRepository requisitionRepository;
  private final AccountService accountService;
  private final HolderService holderService;
  private final InventoryPositionService inventoryPositionService;
  private final Requisition_InventoryPositionService requisition_InventoryPositionService;

  @Autowired
  public RequisitionServiceImpl(
      @Qualifier("RequisitionRepository") final RequisitionRepository requisitionRepository,
      @Qualifier("AccountService") final AccountService accountService,
      @Qualifier("HolderService") final HolderService holderService,
      @Qualifier("InventoryPositionService") final InventoryPositionService inventoryPositionService,
      @Qualifier("Requisition_InventoryPositionService")
    final Requisition_InventoryPositionService requisition_InventoryPositionService) {
    this.requisitionRepository = requisitionRepository;
    this.accountService = accountService;
    this.holderService = holderService;
    this.inventoryPositionService = inventoryPositionService;
    this.requisition_InventoryPositionService = requisition_InventoryPositionService;
  }

  @Override
  public List<Requisition> getAll() {
    return requisitionRepository.findAll();
  }

  @Override
  public Optional<Requisition> getById(@NotNull @Valid UUID id) {
    return requisitionRepository.getById(id);
  }

  @Override
  public Requisition add(
      @NotNull @Valid RequisitionDTO requisitionDTO) {
    Holder accountHolder = holderService
        .getByHolderID(UUID.fromString(requisitionDTO.getHolderUUID()))
        .orElseThrow(
        () -> new ResourceNotFoundException("Holder with id: "
            + requisitionDTO.getHolderUUID() + " does not exist."));
    Account account = accountService.getByLogin(requisitionDTO.getLogin())
        .orElseThrow(
        () ->  new ResourceNotFoundException("Account with login: "
            + requisitionDTO.getLogin() + " does not exist."));
    Requisition savedRequisition = requisitionRepository.saveAndFlush(new Requisition(
            account,
            requisitionDTO.getStatus(),
            requisitionDTO.getCreationDate(),
            requisitionDTO.getDueDate(),
            requisitionDTO.getDescription(),
            accountHolder));
    Requisition requisitionToAdd = requisitionRepository.findById(savedRequisition.getId())
        .orElseThrow(() ->  new ResourceNotFoundException("Requisition with id: "
            + savedRequisition.getId() + " does not exist."));
    List<AddUpdatePositionDTO> positionsToPatch = requisitionDTO.getInventoryPositions();
    if (!isNull(positionsToPatch)) {
      Map<InventoryPosition, Integer> positions = positionsToPatch
          .parallelStream()
          .collect(Collectors.toMap(
              e -> (inventoryPositionService.getByPositionID(UUID.fromString(e.getId()))
          .orElseThrow(() -> new ResourceNotFoundException(
              "Position with id " + e + "is not found."))),
              AddUpdatePositionDTO::getAmount));
      Set<InventoryPosition> availablePositions =
          requisitionToAdd
              .getAccount()
              .getHolders()
              .parallelStream()
              .flatMap(e -> e.getRoles().stream())
              .flatMap(e -> e.getInventoryPositions().stream())
          .collect(Collectors.toSet());
      if (availablePositions.containsAll(positions.keySet())) {
        Set<Requisition_InventoryPosition> toAdd = new HashSet<>();
        positions.forEach((key, value) -> {
          toAdd.add(new Requisition_InventoryPosition(key, requisitionToAdd, value));
        });
        requisitionToAdd.setRequisitionInventoryPositions(toAdd);
      }
    }
    return requisitionRepository.saveAndFlush(requisitionToAdd);
  }

  @Override
  public Requisition update(@NotNull @Valid Requisition requisition) {
    return requisitionRepository.saveAndFlush(requisition);
  }

  @Override
  public void remove(@NotNull @Valid Requisition requisition) {
  }

  private Set<UUID> getHolderInventoryPositionsIds(Holder holder){
    Set<InventoryPosition> allPositions = new HashSet<>();

    for (Role currentRole: holder.getRoles()) {
      allPositions.addAll(currentRole.getInventoryPositions());
    }

    Set<UUID> positionsIds = new HashSet<>();
    for (InventoryPosition currentPosition : allPositions) {
      positionsIds.add(currentPosition.getId());
    }
    return positionsIds;
  }

  private void checkPositionsExistance(Set<UUID> positionsIDs) {
    for (UUID currentId :positionsIDs) {
      Optional<InventoryPosition> currentPosition = inventoryPositionService.getByPositionID(currentId);
      currentPosition.orElseThrow(() -> new ResourceNotFoundException("Positon with id: " + currentId + " not found."));
    }
  }

  private void checkPositionsAvailability(Set<UUID> availablePositions, Set<UUID> requestedPositions) {
    for (UUID currentId: requestedPositions) {
      if (!availablePositions.contains(currentId)) {
        throw new ResourceNotAvailableException("Inventory position with id:" + currentId + " is not available.");
      }
    }
  }
}
