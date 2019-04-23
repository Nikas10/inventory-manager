package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Holder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface InventoryHolderService {
    List<Holder> getAll();

    Holder getByHolderID(UUID holderID);

    Set<Holder> getByHolderIDs(Set<UUID> holderIDs);

    Holder getByHolderName(String holderName);

    Holder add(Holder holder);

    Holder update(Holder holder);

    void remove(Holder holder);
}
