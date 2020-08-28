package com.gdn.qa.util;

import com.gdn.qa.util.constant.SupportedReports;
import com.gdn.qa.util.model.TestLinkData;
import com.gdn.qa.util.reader.BaseTestResultReader;
import com.gdn.qa.util.reader.CucumberTestResultReader;
import com.gdn.qa.util.reader.EarlGreyTestResultReader;
import com.gdn.qa.util.reader.JBehaveTestResultReader;

import java.io.File;

/**
 * @author yunaz.ramadhan on 3/3/2020
 */
public class BadakReporter {
  private static final String DEFAULT_FOLDER = System.getProperty("user.dir");
  private static final String CUCUMBER_FOLDER =
      "target/destination/";
  private static final String EARLGREY_FOLDER =
       "target/destination/";
  private static final String JBEHAVE_FOLDER =
      "target/jbehave/view/";

  public static void printBanner() {
    System.out.println(
        "______           _       _          ______                      _            \n"
            + "| ___ \\         | |     | |         | ___ \\                    | |           \n"
            + "| |_/ / __ _  __| | __ _| | ________| |_/ /___ _ __   ___  _ __| |_ ___ _ __ \n"
            + "| ___ \\/ _` |/ _` |/ _` | |/ /______|    // _ \\ '_ \\ / _ \\| '__| __/ _ \\ '__|\n"
            + "| |_/ / (_| | (_| | (_| |   <       | |\\ \\  __/ |_) | (_) | |  | ||  __/ |   \n"
            + "\\____/ \\__,_|\\__,_|\\__,_|_|\\_\\      \\_| \\_\\___| .__/ \\___/|_|   \\__\\___|_|   \n"
            + "                                              | |                            \n"
            + "                                              |_|                            \n");
  }

  public static void printConfiguration(TestLinkData testLinkData, String directory) {
    System.out.format("%n|------------------------------------- Configuration Loaded : ----------------------------------|%n");
    System.out.format("+----------------------+------------------------------------------------------------------------+%n");
    String format = "| %-20s | %-70s |%n";
    System.out.printf(format, "Source directory", directory);
    System.out.printf(format, "Testlink URL", testLinkData.getUrlTestlink());
    System.out.printf(format, "Dev Key", testLinkData.getDEVKEY());
    System.out.printf(format, "Project Name", testLinkData.getTestProject());
    System.out.printf(format, "Test Plan Name", testLinkData.getTestPlan());
    System.out.printf(format, "Build Name", testLinkData.getBuild());
    System.out.printf(format, "Platform Name", testLinkData.getPlatFormName());
    System.out.format("+----------------------+------------------------------------------------------------------------+%n");
  }

  public static BaseTestResultReader<?> getReader(SupportedReports reportType,
      TestLinkData testLinkData) throws Exception {
    String reportPath;
    switch (reportType) {
      case JBEHAVE:
        reportPath = JBEHAVE_FOLDER;
        break;
      case EARLGREY:
        reportPath = EARLGREY_FOLDER;
        break;
      default:
        reportPath = CUCUMBER_FOLDER;
        break;
    }
    return getReader(reportType, testLinkData, reportPath);
  }

  public static BaseTestResultReader<?> getReader(SupportedReports reportType,
      TestLinkData testLinkData,
      String reportPath) throws Exception {
    printBanner();
    reportPath = String.format("%s%s%s", DEFAULT_FOLDER, File.separator, reportPath);
    try {
      System.out.println("Starting configuration for " + reportType.name());
      switch (reportType) {
        case JBEHAVE:
          return new JBehaveTestResultReader(testLinkData, reportPath);
        case EARLGREY:
          return new EarlGreyTestResultReader(testLinkData, reportPath);
        default:
          return new CucumberTestResultReader(testLinkData, reportPath);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return new CucumberTestResultReader(testLinkData, CUCUMBER_FOLDER);
    }
  }

  public static <T extends Enum<?>> T searchEnum(Class<T> enumeration, String search) {
    if(search == null || search.trim().isEmpty()){
      return null;
    }
    for (T each : enumeration.getEnumConstants()) {
      if (each.name().compareToIgnoreCase(search) == 0) {
        return each;
      }
    }
    return null;
  }
}
