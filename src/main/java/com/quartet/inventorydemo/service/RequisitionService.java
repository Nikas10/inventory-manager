package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.util.IdNull;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

public interface RequisitionService {

  Collection<Requisition> getAll();

  @Validated(IdNull.class)
  Optional<Requisition> getById(@NotNull @Valid UUID id);

  Requisition add(
      @NotNull @Valid String login,
      @NotNull @Valid Date creationDate,
      @NotNull @Valid String description,
      @NotNull @Valid Date dueDate,
      @NotNull @Valid String status,
      @NotNull @Valid UUID holderId,
      @NotEmpty @Valid Map<String, Integer> stringInventoryPositionUUIDs);

  Requisition update(@NotNull @Valid Requisition requisition);

  void remove(@NotNull @Valid Requisition requisition);
}
