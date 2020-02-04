package com.blibli.oss.qa.util.model.earlgrey;

/**
 * Created by tri.abror on 2/27/2019.
 */

public class TestResult {
  private String testName;
  private String testStatus;

  public String getTestName() {
    return testName;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public String getTestStatus() {
    return testStatus;
  }

  public void setTestStatus(String testStatus) {
    this.testStatus = testStatus;
  }
}
