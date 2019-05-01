package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Requisition;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface RequisitionProcessService {

  void create(@NotNull @Valid Requisition requisition);

  void update(@NotNull @Valid Requisition requisition);

  void delete(@NotNull @Valid Requisition requisition);

  void approve(@NotNull @Valid Requisition requisition);

  void reject(@NotNull @Valid Requisition requisition);

  void requestClarification(@NotNull @Valid Requisition requisition, @NotNull @Valid String reason);

  void makeChanges(@NotNull @Valid Requisition requisition);

  void complete(@NotNull @Valid Requisition requisition);
}
