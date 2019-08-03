package com.example.SpringBootHtmlToPdfConverter.HtmlToPdfController;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringBootHtmlToPdfConverter.HtmlToPdfService.ReportDownloadService;


@RestController
@RequestMapping("/v1/HTML-to-PDF")
public class ReportController {

  @Autowired
  ReportDownloadService reportDownloadService;

  /**
   * Generate the report for Exploratory Data Analysis in PDF Format
   * 
   * @param filename. It is in HTML Format. Downloads PDF file.
   */
  @PostMapping(value = "/download", produces = MediaType.APPLICATION_PDF_VALUE)
  public void generateReport(HttpServletResponse response, @RequestParam String inputFileName) {
    reportDownloadService.generateReport(response, inputFileName);
  }
}
