package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.repository.RequestRepository;
import com.quartet.inventorydemo.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("RequestService")
public class RequestServiceImpl implements RequestService {

    @Autowired
    RequestRepository requestRepo;

    @Override
    public List<Requisition> getAll() {
        return requestRepo.findAll();
    }

    @Override
    public Requisition getByEmployeeID(UUID requestID) {
        return requestRepo.findByRequestID(requestID);
    }

    @Override
    public Requisition add(Requisition requisition) {
        requisition.setRequestID(UUID.randomUUID());
        return requestRepo.saveAndFlush(requisition);
    }

    @Override
    public Requisition update(Requisition requisition) {
        return requestRepo.saveAndFlush(requisition);
    }
}
