package com.example.SpringBootHtmlToPdfConverter.HtmlToPdfService;

import javax.servlet.http.HttpServletResponse;

public interface ReportDownloadService {
  void generateReport(HttpServletResponse response, String sourceFile);
}
