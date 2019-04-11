package com.gdn.qa.util;

import org.junit.Test;

public class TestReadTags {

  @Test
  public void testEarlGrey1() {
    String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
    String devKey = "6ca063b6dfad21d0c4a9e9cafe6b9ff1";
    String projectName = "Surabaya";
    String testPlanName = "iOsAutomation";
    String buildName = "automation";
    String platformName = "";
    String earlGreyPath = System.getProperty("user.dir") + "/" + "TestSummaries.json";
    System.out.println(earlGreyPath);

    TestResultReader testResultReader = new TestResultReader();
    testResultReader.initialize(testlinkURL,
        devKey,
        projectName,
        testPlanName,
        buildName,
        platformName);
    try {
      testResultReader.readWithEarlGrey(earlGreyPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
  @Test
  public void testCucumber4() {
    String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
    String devKey = "6ca063b6dfad21d0c4a9e9cafe6b9ff1";
    String projectName = "Surabaya";
    String testPlanName = "iOsAutomation";
    String buildName = "automation";
    String platformName = "";
    String cucumberPath = System.getProperty("user.dir") + "/" + "cucumber.json";
    System.out.println(cucumberPath);

    TestResultReader testResultReader = new TestResultReader();
    testResultReader.initialize(testlinkURL,
        devKey,
        projectName,
        testPlanName,
        buildName,
        platformName);
    try {
      testResultReader.readWithCucumber(cucumberPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

    @Test
    public void testCucumber5() {
        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "54087f0b956c4ea5f131585eb66869c8";
        String projectName = "Surabaya Sample Project Rest Assured";
        String testPlanName = "Rest Assured UAT1";
        String buildName = "BUILD UAT1";
        String platformName = "";
        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber5.json";
        System.out.println(cucumberPath);

        TestResultReader testResultReader = new TestResultReader();
        testResultReader.initialize(testlinkURL, devKey,
                projectName, testPlanName, buildName, platformName);
        try {
            testResultReader.readWithCucumber(cucumberPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCucumber6() {
        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "54087f0b956c4ea5f131585eb66869c8";
        String projectName = "Surabaya Sample Project Rest Assured";
        String testPlanName = "Rest Assured UAT1";
        String buildName = "BUILD UAT1";
        String platformName = "";
        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber6.json";
        System.out.println(cucumberPath);

        TestResultReader testResultReader = new TestResultReader();
        testResultReader.initialize(testlinkURL, devKey,
                projectName, testPlanName, buildName, platformName);
        try {
            testResultReader.readWithCucumber(cucumberPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCucumber7() {
        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "54087f0b956c4ea5f131585eb66869c8";
        String projectName = "Surabaya Sample Project Rest Assured";
        String testPlanName = "Rest Assured UAT1";
        String buildName = "BUILD UAT1";
        String platformName = "";
        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber7.json";
        System.out.println(cucumberPath);

        TestResultReader testResultReader = new TestResultReader();
        testResultReader.initialize(testlinkURL, devKey,
                projectName, testPlanName, buildName, platformName);
        try {
            testResultReader.readWithCucumber(cucumberPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCucumber8() {
        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "54087f0b956c4ea5f131585eb66869c8";
        String projectName = "Surabaya Sample Project Rest Assured";
        String testPlanName = "Rest Assured UAT1";
        String buildName = "BUILD UAT1";
        String platformName = "";
        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber8.json";
        System.out.println(cucumberPath);

        TestResultReader testResultReader = new TestResultReader();
        testResultReader.initialize(testlinkURL, devKey,
                projectName, testPlanName, buildName, platformName);
        try {
            testResultReader.readWithCucumber(cucumberPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCucumber10() {
        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "54087f0b956c4ea5f131585eb66869c8";
        String projectName = "Surabaya Sample Project Rest Assured";
        String testPlanName = "Rest Assured UAT1";
        String buildName = "BUILD UAT1";
        String platformName = "";
        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber10.json";
        System.out.println(cucumberPath);

        TestResultReader testResultReader = new TestResultReader();
        testResultReader.initialize(testlinkURL, devKey,
            projectName, testPlanName, buildName, platformName);
        try {
            testResultReader.readWithCucumber(cucumberPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCucumber11() {
        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "54087f0b956c4ea5f131585eb66869c8";
        String projectName = "Surabaya Sample Project Rest Assured";
        String testPlanName = "Rest Assured UAT1";
        String buildName = "BUILD UAT1";
        String platformName = "";
        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber11.json";
        System.out.println(cucumberPath);

        TestResultReader testResultReader = new TestResultReader();
        testResultReader.initialize(testlinkURL, devKey,
                projectName, testPlanName, buildName, platformName);
        try {
            testResultReader.readWithCucumber(cucumberPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCucumber12() {
        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "54087f0b956c4ea5f131585eb66869c8";
        String projectName = "Surabaya Sample Project Rest Assured";
        String testPlanName = "Rest Assured UAT1";
        String buildName = "BUILD UAT1";
        String platformName = "";
        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber12.json";
        System.out.println(cucumberPath);

        TestResultReader testResultReader = new TestResultReader();
        testResultReader.initialize(testlinkURL, devKey,
                projectName, testPlanName, buildName, platformName);
        try {
            testResultReader.readWithCucumber(cucumberPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
