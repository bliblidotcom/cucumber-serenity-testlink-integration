package com.gdn.qa.util;

import com.gdn.qa.util.constant.ReportGeneratorPolicy;
import com.gdn.qa.util.constant.SupportedReports;
import com.gdn.qa.util.model.TestLinkData;
import org.junit.Test;

public class TestReadTags {
  @Test
  public void testCucumberFromFolder() throws Exception {
    long startTime = System.nanoTime();
    String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
    String devKey = "aa0eb2386227576295d65b753ab60a5f";
    String projectName = "Surabaya";
    String testPlanName = "TEST-PLAN";
    String buildName = "BUILD-TEST";
    String platformName = "API";
    String cucumberPath = "/src/test/resources/cucumber/folder/";
    TestLinkData testLinkData = new TestLinkData().setUrlTestlink(testlinkURL)
        .setDEVKEY(devKey)
        .setTestProject(projectName)
        .setTestPlan(testPlanName)
        .setBuild(buildName)
        .setPlatFormName(platformName);
    BadakReporter.getReader(SupportedReports.CUCUMBER, testLinkData, cucumberPath)
        .writeToTestLink();

    long endTime = System.nanoTime();
    System.out.println("Total execution time in second : " + (endTime - startTime) / 1000000000);
  }

  @Test
  public void testCucumberWithBackground() throws Exception {
    long startTime = System.nanoTime();
    String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
    String devKey = "aa0eb2386227576295d65b753ab60a5f";
    String projectName = "Surabaya";
    String testPlanName = "TEST-PLAN";
    String buildName = "BUILD-TEST";
    String platformName = "API";
    String cucumberPath = "/src/test/resources/cucumber/cucumber15.json";
    TestLinkData testLinkData = new TestLinkData().setUrlTestlink(testlinkURL)
        .setDEVKEY(devKey)
        .setReportPolicy(ReportGeneratorPolicy.AUTO)
        .setTestProject(projectName)
        .setTestPlan(testPlanName)
        .setBuild(buildName)
        .setPlatFormName(platformName);
    BadakReporter.getReader(SupportedReports.CUCUMBER, testLinkData, cucumberPath)
        .writeToTestLink();

    long endTime = System.nanoTime();
    System.out.println("Total execution time in second : " + (endTime - startTime) / 1000000000);
  }

  @Test
  public void testCucumberWithFailure() throws Exception {
    long startTime = System.nanoTime();
    String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
    String devKey = "aa0eb2386227576295d65b753ab60a5f";
    String projectName = "Surabaya";
    String testPlanName = "TEST-PLAN";
    String buildName = "BUILD-TEST";
    String platformName = "API";
    String cucumberPath = "/src/test/resources/cucumber/cucumber_fail.json";
    TestLinkData testLinkData = new TestLinkData().setUrlTestlink(testlinkURL)
        .setDEVKEY(devKey)
        .setTestProject(projectName)
        .setTestPlan(testPlanName)
        .setBuild(buildName)
        .setPlatFormName(platformName);
    BadakReporter.getReader(SupportedReports.CUCUMBER, testLinkData, cucumberPath)
        .writeToTestLink();

    long endTime = System.nanoTime();
    System.out.println("Total execution time in second : " + (endTime - startTime) / 1000000000);
  }

  @Test
  public void testCucumberHuge() throws Exception {
    long startTime = System.nanoTime();
    String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
    String devKey = "aa0eb2386227576295d65b753ab60a5f";
    String projectName = "Surabaya";
    String testPlanName = "Custom Yunaz";
    String buildName = "Neoloyalty";
    String platformName = "API";
    String cucumberPath = "/src/test/resources/cucumber/cucumber-huge.json";
    System.out.println(cucumberPath);
    TestLinkData testLinkData = new TestLinkData().setUrlTestlink(testlinkURL)
        .setDEVKEY(devKey)
        .setTestProject(projectName)
        .setTestPlan(testPlanName)
        .setReportPolicy(ReportGeneratorPolicy.AUTO)
        .setBuild(buildName)
        .setPlatFormName(platformName);
    BadakReporter.getReader(SupportedReports.CUCUMBER, testLinkData, cucumberPath)
        .writeToTestLink();

    long endTime = System.nanoTime();
    System.out.println("Total execution time in second : " + (endTime - startTime) / 1000000000);
  }

  @Test
  public void testIndexNumber() throws Exception {
    long startTime = System.nanoTime();
    String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
    String devKey = "aa0eb2386227576295d65b753ab60a5f";
    String projectName = "Surabaya";
    String testPlanName = "TEST-PLAN";
    String buildName = "BUILD-TEST";
    String platformName = "";
    String cucumberPath = "/src/test/resources/cucumber/cucumber2.json";
    System.out.println(cucumberPath);
    TestLinkData testLinkData = new TestLinkData().setUrlTestlink(testlinkURL)
        .setDEVKEY(devKey)
        .setTestProject(projectName)
        .setTestPlan(testPlanName)
        .setBuild(buildName)
        .setPlatFormName(platformName);
    BadakReporter.getReader(SupportedReports.CUCUMBER, testLinkData, cucumberPath)
        .writeToTestLink();

    long endTime = System.nanoTime();
    System.out.println("Total execution time in second : " + (endTime - startTime) / 1000000000);
  }
}
