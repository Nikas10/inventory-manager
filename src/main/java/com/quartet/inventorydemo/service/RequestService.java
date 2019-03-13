package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Employee;
import com.quartet.inventorydemo.model.Request;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    List<Request> getAll();
    Request getByEmployeeID(UUID requestID);
    Request add(Request request);
    Request update(Request request);
}
