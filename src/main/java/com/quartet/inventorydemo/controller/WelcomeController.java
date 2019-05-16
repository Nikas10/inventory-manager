package com.quartet.inventorydemo.controller;

import com.quartet.inventorydemo.service.report.AccountRequisitionsReportService;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

  @Autowired
  AccountRequisitionsReportService accountRequisitionsReportService;

  @GetMapping("api/report")
  @PreAuthorize("hasAuthority('USER')")
  public void report(HttpServletResponse httpServletResponse, Principal principal)
      throws JRException, IOException {
    httpServletResponse.setContentType("text/html");

    JasperPrint report = accountRequisitionsReportService.report(principal.getName());

    HtmlExporter htmlExporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
    htmlExporter.setExporterInput(new SimpleExporterInput(report));
    htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(httpServletResponse.getWriter()));
    htmlExporter.exportReport();
  }
}
