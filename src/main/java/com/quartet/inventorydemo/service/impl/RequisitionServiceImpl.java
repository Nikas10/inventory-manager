package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.repository.RequisitionRepository;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.RequisitionService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

  @Autowired
  public RequisitionServiceImpl(
      @Qualifier("RequisitionRepository") final RequisitionRepository requisitionRepository,
      @Qualifier("AccountService") final AccountService accountService) {
    this.requisitionRepository = requisitionRepository;
    this.accountService = accountService;
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
  public Requisition add(@NotNull @Valid String login,
                         @NotNull @Valid Date creationDate,
                         @NotNull @Valid String description,
                         @NotNull @Valid Date dueDate,
                         @NotNull @Valid String status)
  {

    Optional<Account> optionalAccount = accountService.getByLogin(login);

    Requisition requisitionToAdd =
        new Requisition(
            optionalAccount.orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Account with login: " + login + " not found")),
            status,
            creationDate,
            dueDate,
            description);

    return requisitionRepository.saveAndFlush(requisitionToAdd);
  }

  @Override
  public Requisition update(@NotNull @Valid Requisition requisition) {
    return requisitionRepository.saveAndFlush(requisition);
  }

  @Override
  public void remove(@NotNull @Valid Requisition requisition) {}
}
