package com.blibli.oss.qa.util;

public class TestlinkTags {
    private String testLinkId;
    private String testSuiteId;

    public TestlinkTags() {
        this.testLinkId = "";
        this.testSuiteId = "0";
    }

    public String getTestLinkId() {
        return testLinkId;
    }

    public void setTestLinkId(String testLinkId) {
        this.testLinkId = testLinkId;
    }

    public String getTestSuiteId() {
        return testSuiteId;
    }

    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }
}
