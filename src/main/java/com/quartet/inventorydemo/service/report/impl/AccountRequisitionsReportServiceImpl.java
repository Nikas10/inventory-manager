package com.quartet.inventorydemo.service.report.impl;

import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.report.AccountRequisitionsReportService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service("AccountRequisitionsReportService")
public class AccountRequisitionsReportServiceImpl implements AccountRequisitionsReportService {

  @Autowired
  private AccountService accountService;

  @Override
  public JasperPrint report(String login) throws IOException, JRException {
    Optional<Account> accountOptional = accountService.getByLogin(login);
    Account account = accountOptional.orElseThrow(
        () ->
            new ResourceNotFoundException(
                "Account with login: " + login + " not found"));

    List<Map<String, Object>> result = new ArrayList<>();
    Set<Requisition> requisitions = account.getRequisitions();
    for (Requisition requisition : requisitions) {
      Map<String, Object> fieldValueMap = new HashMap<>();
      fieldValueMap.put("requisitionId", requisition.getId());
      fieldValueMap.put("status", requisition.getStatus());
      fieldValueMap.put("creationDate", requisition.getCreationDate());
      fieldValueMap.put("dueDate", requisition.getDueDate());
      fieldValueMap.put("description", requisition.getDescription());
      result.add(fieldValueMap);
    }
    if (!result.isEmpty()) {
      result.get(0).put("login", login);
    } else {
      Map<String, Object> fieldValueMap = new HashMap<>();
      fieldValueMap.put("login", login);
      result.add(fieldValueMap);
    }

    JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(result);
    InputStream inputStream =
        new ClassPathResource("report/Simple_Blue2.jrxml", this.getClass().getClassLoader())
            .getInputStream();
    JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

    return JasperFillManager.fillReport(jasperReport, null, jrBeanCollectionDataSource);
  }
}
