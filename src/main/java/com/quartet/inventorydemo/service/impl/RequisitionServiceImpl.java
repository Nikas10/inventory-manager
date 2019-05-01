package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.repository.RequisitionRepository;
import com.quartet.inventorydemo.service.RequisitionService;
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

  @Autowired
  public RequisitionServiceImpl(
      @Qualifier("RequisitionRepository") final RequisitionRepository requisitionRepository) {
    this.requisitionRepository = requisitionRepository;
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
  public Requisition add(@NotNull @Valid Requisition requisition) {
    return requisitionRepository.saveAndFlush(requisition);
  }

  @Override
  public Requisition update(@NotNull @Valid Requisition requisition) {
    return requisitionRepository.saveAndFlush(requisition);
  }

  @Override
  public void remove(@NotNull @Valid Requisition requisition) {
  }
}
