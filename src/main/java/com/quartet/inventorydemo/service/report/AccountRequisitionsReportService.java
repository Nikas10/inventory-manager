package com.quartet.inventorydemo.service.report;

import java.io.IOException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface AccountRequisitionsReportService {

  JasperPrint report(String login) throws IOException, JRException;
}
