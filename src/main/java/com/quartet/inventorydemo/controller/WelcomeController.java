package com.quartet.inventorydemo.controller;

import com.quartet.inventorydemo.service.report.InventoryPositionReportService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomeController {

  @Autowired
  InventoryPositionReportService inventoryPositionReportService;

  // inject via application.properties
  @Value("${welcome.message}")
  private String message;

  private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

  @GetMapping("/welcome")
  public String main(Model model) {
    model.addAttribute("message", message);
    model.addAttribute("tasks", tasks);

    return "welcome"; // view
  }

  // /hello?name=kotlin
  @GetMapping("/hello")
  public String mainWithParam(
      @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {

    model.addAttribute("message", name);

    return "welcome"; // view
  }

  // /hello?name=kotlin
  @GetMapping("/report")
  public void report(HttpServletResponse httpServletResponse) throws JRException, IOException {
    httpServletResponse.setContentType("text/html");
    List<Map<String, Object>> report = inventoryPositionReportService.report();

    JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(report);

    InputStream inputStream =
        new ClassPathResource("report/Simple_Blue2.jrxml", this.getClass().getClassLoader())
            .getInputStream();
    JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
    JasperPrint jasperPrint =
        JasperFillManager.fillReport(jasperReport, null, jrBeanCollectionDataSource);
    HtmlExporter htmlExporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
    htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(httpServletResponse.getWriter()));
    htmlExporter.exportReport();
  }
}
