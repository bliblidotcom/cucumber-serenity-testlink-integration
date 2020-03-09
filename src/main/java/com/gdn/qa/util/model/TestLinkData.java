package com.gdn.qa.util.model;

/**
 * @author yunaz.ramadhan on 3/2/2020
 */
public class TestLinkData {
  private String testProject;
  private String testPlan;
  private String build;
  private String platFormName;
  private String DEVKEY;

  public String getTestProject() {
    return testProject;
  }

  public TestLinkData setTestProject(String testProject) {
    this.testProject = testProject;
    return this;
  }

  public String getTestPlan() {
    return testPlan;
  }

  public TestLinkData setTestPlan(String testPlan) {
    this.testPlan = testPlan;
    return this;
  }

  public String getBuild() {
    return build;
  }

  public TestLinkData setBuild(String build) {
    this.build = build;
    return this;
  }

  public String getPlatFormName() {
    return platFormName;
  }

  public TestLinkData setPlatFormName(String platFormName) {
    this.platFormName = platFormName;
    return this;
  }

  public String getDEVKEY() {
    return DEVKEY;
  }

  public TestLinkData setDEVKEY(String DEVKEY) {
    this.DEVKEY = DEVKEY;
    return this;
  }

  private String urlTestlink;

  public String getUrlTestlink() {
    return urlTestlink;
  }

  public TestLinkData setUrlTestlink(String urlTestlink) {
    this.urlTestlink = urlTestlink;
    return this;
  }

  public TestLinkData build(){
    return this;
  }
}
