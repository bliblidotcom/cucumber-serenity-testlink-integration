package com.gdn.qa.util;

import org.junit.Test;

public class TestReadTags {

//    @Test
//    public void testCucumber4() {
//        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
//        String devKey = "54087f0b956c4ea5f131585eb66869c8";
//        String projectName = "Surabaya Sample Project Rest Assured";
//        String testPlanName = "Rest Assured UAT1";
//        String buildName = "BUILD UAT1";
//        String platformName = "";
//        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber4.json";
//        System.out.println(cucumberPath);
//
//        TestResultReader testResultReader = new TestResultReader();
//        testResultReader.initialize(testlinkURL, devKey,
//                projectName, testPlanName, buildName, platformName);
//        try {
//            testResultReader.readWithCucumber(cucumberPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testCucumber5() {
//        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
//        String devKey = "54087f0b956c4ea5f131585eb66869c8";
//        String projectName = "Surabaya Sample Project Rest Assured";
//        String testPlanName = "Rest Assured UAT1";
//        String buildName = "BUILD UAT1";
//        String platformName = "";
//        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber5.json";
//        System.out.println(cucumberPath);
//
//        TestResultReader testResultReader = new TestResultReader();
//        testResultReader.initialize(testlinkURL, devKey,
//                projectName, testPlanName, buildName, platformName);
//        try {
//            testResultReader.readWithCucumber(cucumberPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testCucumber6() {
//        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
//        String devKey = "54087f0b956c4ea5f131585eb66869c8";
//        String projectName = "Surabaya Sample Project Rest Assured";
//        String testPlanName = "Rest Assured UAT1";
//        String buildName = "BUILD UAT1";
//        String platformName = "";
//        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber6.json";
//        System.out.println(cucumberPath);
//
//        TestResultReader testResultReader = new TestResultReader();
//        testResultReader.initialize(testlinkURL, devKey,
//                projectName, testPlanName, buildName, platformName);
//        try {
//            testResultReader.readWithCucumber(cucumberPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testCucumber7() {
//        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
//        String devKey = "54087f0b956c4ea5f131585eb66869c8";
//        String projectName = "Surabaya Sample Project Rest Assured";
//        String testPlanName = "Rest Assured UAT1";
//        String buildName = "BUILD UAT1";
//        String platformName = "";
//        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber7.json";
//        System.out.println(cucumberPath);
//
//        TestResultReader testResultReader = new TestResultReader();
//        testResultReader.initialize(testlinkURL, devKey,
//                projectName, testPlanName, buildName, platformName);
//        try {
//            testResultReader.readWithCucumber(cucumberPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
    @Test
    public void testCucumber8() {
        String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "54087f0b956c4ea5f131585eb66869c8";
        String projectName = "Surabaya Sample Project Rest Assured";
        String testPlanName = "Rest Assured UAT1";
        String buildName = "BUILD UAT1";
//        String buildName = "Prod";
        String platformName = "";
        String cucumberPath = System.getProperty("user.dir") + "/src/test/resources/cucumber_fail.json";
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

}
