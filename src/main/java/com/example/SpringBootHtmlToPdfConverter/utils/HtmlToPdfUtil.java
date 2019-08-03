package com.example.SpringBootHtmlToPdfConverter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlToPdfUtil {
  private static Logger LOG = LoggerFactory.getLogger(HtmlToPdfUtil.class);

  /**
   * html To pdf Conversion
   *
   * @param srcPath html Route
   * @param pdfLocationPath pdf Save path
   * @return Conversion returns true successfully
   */
  public static boolean convert(String cmd, String srcPath, String pdfLocationPath,
      String systemOs) {
    String toPdfTool;
    if (systemOs.toLowerCase().startsWith("win")) {
      toPdfTool = cmd;
    } else {
      toPdfTool = cmd;
    }
    StringBuilder command = new StringBuilder();
    command.append(toPdfTool);
    command.append(" ");
    command.append(
        "-T 10 -B 10 -L 0 -R 0 -O Portrait --page-size A2 --viewport-size 1200x768 --enable-smart-shrinking toc ");
    command.append("  --background");
    command.append(" --debug-javascript");
    command.append("  --header-line");// Line under header
    command.append(" --header-spacing 10 ");// (Set the distance between header and content,Default
                                            // 0)

    command.append(srcPath);
    command.append(" ");
    command.append(pdfLocationPath);

    boolean result = true;
    try {
      Process proc = Runtime.getRuntime().exec(command.toString());
      HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
      HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
      error.start();
      output.start();
      proc.waitFor();
    } catch (Exception ex) {
      result = false;
      LOG.error("Exception occured in HTML to PDF Conversion {}", ex);
    }
    return result;
  }
}
