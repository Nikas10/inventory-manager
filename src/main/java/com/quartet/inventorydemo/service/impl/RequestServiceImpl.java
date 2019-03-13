package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Employee;
import com.quartet.inventorydemo.model.Request;
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
    public List<Request> getAll() {
        return requestRepo.findAll();
    }

    @Override
    public Request getByEmployeeID(UUID requestID) {
        return requestRepo.findByRequestID(requestID);
    }

    @Override
    public Request add(Request request) {
        request.setRequestID(UUID.randomUUID());
        return requestRepo.saveAndFlush(request);
    }

    @Override
    public Request update(Request request) {
        return requestRepo.saveAndFlush(request);
    }
}
