package com.gdn.qa.util;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;

public class ScenarioData {
  private String name;
  private TestlinkTags testlinkTags;
  private Boolean passed;
  private String reasonFail;
  private String summary;
  private Double duration;
  private Integer indexFail;
  private TestCase testCase;
  private String detailToPrint;
  private String featureName;

  public ScenarioData() {
    this.passed = true;
    this.reasonFail = "";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getPassed() {
    return passed;
  }

  public void setPassed(Boolean passed) {
    this.passed = passed;
  }

  public String getReasonFail() {
    return reasonFail;
  }

  public void setReasonFail(String reasonFail) {
    this.reasonFail = reasonFail;
  }

  public TestlinkTags getTestlinkTags() {
    return testlinkTags;
  }

  public void setTestlinkTags(TestlinkTags testlinkTags) {
    this.testlinkTags = testlinkTags;
  }

  public Double getDuration() {
    return duration;
  }

  public void setDuration(Double duration) {
    this.duration = duration;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Integer getIndexFail() {
    return indexFail;
  }

  public void setIndexFail(Integer indexFail) {
    this.indexFail = indexFail;
  }

  public TestCase getTestCase() {
    return testCase;
  }

  public void setTestCase(TestCase testCase) {
    this.testCase = testCase;
  }

  public String getDetailToPrint() {
    return detailToPrint;
  }

  public void setDetailToPrint(String detailToPrint) {
    this.detailToPrint = detailToPrint;
  }

  public String getFeatureName() {
    return featureName;
  }

  public void setFeatureName(String featureName) {
    this.featureName = featureName;
  }
}
