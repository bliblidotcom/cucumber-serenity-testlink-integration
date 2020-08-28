package com.gdn.qa.util.model.earlgrey;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tri.abror on 2/26/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"Duration", "Subtests", "TestIdentifier", "TestName", "TestObjectClass"})
public class Test {

  @JsonProperty("Duration")
  private Double duration;
  @JsonProperty("Subtests")
  private List<Subtest1> subtest1s;
  @JsonProperty("TestIdentifier")
  private String testIdentifier;
  @JsonProperty("TestName")
  private String testName;
  @JsonProperty("TestObjectClass")
  private String testObjectClass;
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

  @JsonProperty("Subtests")
  public List<Subtest1> getSubtest1s() {
    return subtest1s;
  }

  @JsonProperty("Subtests")
  public void setSubtest1s(List<Subtest1> subtest1s) {
    this.subtest1s = subtest1s;
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

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }
}
