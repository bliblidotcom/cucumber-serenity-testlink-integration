package com.gdn.qa.util.model.earlgrey;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tri.abror on 2/27/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"Duration", "TestIdentifier", "TestName", "TestObjectClass",
    "TestStatus", "TestSummaryGUID"})
public class Subtest3 {
  @JsonProperty("Duration")
  private Double duration;
  @JsonProperty("TestIdentifier")
  private String testIdentifier;
  @JsonProperty("TestName")
  private String testName;
  @JsonProperty("TestObjectClass")
  private String testObjectClass;
  @JsonProperty("TestStatus")
  private String testStatus;
  @JsonProperty("TestSummaryGUID")
  private String testSummaryGUID;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("Duration")
  public Double getDuration() {
    return duration;
  }

  @JsonProperty("Duration")
  public void setDuration(Double duration) {
    this.duration = duration;
  }

  @JsonProperty("TestIdentifier")
  public String getTestIdentifier() {
    return testIdentifier;
  }

  @JsonProperty("TestIdentifier")
  public void setTestIdentifier(String testIdentifier) {
    this.testIdentifier = testIdentifier;
  }

  @JsonProperty("TestName")
  public String getTestName() {
    return testName;
  }

  @JsonProperty("TestName")
  public void setTestName(String testName) {
    this.testName = testName;
  }

  @JsonProperty("TestObjectClass")
  public String getTestObjectClass() {
    return testObjectClass;
  }

  @JsonProperty("TestObjectClass")
  public void setTestObjectClass(String testObjectClass) {
    this.testObjectClass = testObjectClass;
  }

  @JsonProperty("TestStatus")
  public String getTestStatus() {
    return testStatus;
  }

  @JsonProperty("TestStatus")
  public void setTestStatus(String testStatus) {
    this.testStatus = testStatus;
  }

  @JsonProperty("TestSummaryGUID")
  public String getTestSummaryGUID() {
    return testSummaryGUID;
  }

  @JsonProperty("TestSummaryGUID")
  public void setTestSummaryGUID(String testSummaryGUID) {
    this.testSummaryGUID = testSummaryGUID;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }
}
