package com.example.SpringBootHtmlToPdfConverter.HtmlToPdfServiceImpl;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SpringBootHtmlToPdfConverter.HtmlToPdfService.ReportDownloadService;
import com.example.SpringBootHtmlToPdfConverter.aspects.Loggable;
import com.example.SpringBootHtmlToPdfConverter.exceptions.MyException;
import com.example.SpringBootHtmlToPdfConverter.utils.Helper;
import com.example.SpringBootHtmlToPdfConverter.utils.HtmlToPdfUtil;


@Service
public class ReportDownloadServiceImpl implements ReportDownloadService {
  private static final Logger LOG = LoggerFactory.getLogger(ReportDownloadServiceImpl.class);

  @Value("${report.wkhtmltopdf.cmd}")
  private String command;

  @Value("${report.win-download-dir}")
  private String pdfLocationPathForWindow;
  @Value("${report.lin-download-dir}")
  private String pdfLocationPathForLinux;

  private String pdfLocationPath;

  private static String systemOs;


  /**
   * Setting pdf file path according to server system
   */
  static {
    systemOs = System.getProperty("os.name");
  }

  @Override
  @Loggable
  public void generateReport(HttpServletResponse res, String sourceFile) {

    Timestamp currentTime = Helper.rightNow();

    /** To determine whether a path exists or not, create it if it does not exist */
    if (systemOs.toLowerCase().startsWith("win")) {
      pdfLocationPath = pdfLocationPathForWindow;
    } else {
      pdfLocationPath = pdfLocationPathForLinux;
    }

    File fileDir = new File(pdfLocationPath);
    if (!fileDir.exists()) {
      fileDir.setWritable(true);
      fileDir.mkdirs();
    }
    String fileName = UUID.randomUUID().toString() + ".pdf";
    String source = pdfLocationPath + fileName;
    String outputFileName = "EDA_" + currentTime.toString() + ".pdf";
    try {
      // Call the convert method in HtmlToPdfUtilz to generate pdf from html
      boolean isSuccess = HtmlToPdfUtil.convert(command, sourceFile, source, systemOs);
      if (isSuccess) {
        download(res, outputFileName, fileName);
      } else {
        LOG.error("Unable to download EDA Report");
        throw new MyException("pdf Download exception.");
      }

    } catch (Exception e) {
      LOG.error("Unable to download EDA Report Due to Exception occured.");
      throw new MyException("pdf Download exception.");
    }
  }

  /**
   * Response Download
   * 
   * @param resp
   * @param downloadName Downloaded pdf file name
   * @param fileName Real filename in the system
   */
  public void download(HttpServletResponse resp, String downloadName, String fileName) {
    try {
      downloadName = new String(downloadName.getBytes("GBK"), StandardCharsets.UTF_8);
    } catch (UnsupportedEncodingException ex) {
      LOG.error("Unsupported Encoding Exception.class {}", ex);
    }
    String realPath = pdfLocationPath;
    String path = realPath + fileName;
    File file = new File(path);
    resp.reset();
    resp.setContentType("application/octet-stream");
    resp.setCharacterEncoding("utf-8");
    resp.setContentLength((int) file.length());
    resp.setHeader("Content-Disposition", "attachment;filename=" + downloadName + ".pdf");
    byte[] buff = new byte[1024];
    BufferedInputStream bis = null;
    OutputStream os = null;
    try {
      os = resp.getOutputStream();
      bis = new BufferedInputStream(new FileInputStream(file));
      int i = 0;
      while ((i = bis.read(buff)) != -1) {
        os.write(buff, 0, i);
        os.flush();
      }
    } catch (IOException ex) {
      LOG.error("Unable to download Pdf.{}", ex);
    } finally {
      try {
        if (bis != null) {
          bis.close();
        }
        // Delete local pdf files after completion
        file.delete();
      } catch (IOException ex) {
        LOG.error("Unable to download Pdf.{}", ex);
      }
    }

  }
}
