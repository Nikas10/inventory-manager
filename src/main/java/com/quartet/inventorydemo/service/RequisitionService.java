package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.dto.RequisitionDTO;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.util.IdNull;
import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

public interface RequisitionService {

  Collection<Requisition> getAll();

  @Validated(IdNull.class)
  Requisition getById(@NotNull @Valid UUID id);

  Requisition add(
      @NotNull @Valid RequisitionDTO dto);

  Requisition update(@NotNull @Valid Requisition requisition);

  void remove(@NotNull @Valid Requisition requisition);
}
