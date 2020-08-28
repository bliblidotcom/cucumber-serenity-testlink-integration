package com.gdn.qa.util.model.earlgrey;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tri.abror on 2/26/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"DiagnosticsDirectory", "ProjectPath", "TargetName", "TestKind", "TestName",
    "TestObjectClass", "Tests"})
public class TestableSummary {
  @JsonProperty("DiagnosticsDirectory")
  private String diagnosticsDirectory;
  @JsonProperty("ProjectPath")
  private String projectPath;
  @JsonProperty("TargetName")
  private String targetName;
  @JsonProperty("TestKind")
  private String testKind;
  @JsonProperty("TestName")
  private String testName;
  @JsonProperty("TestObjectClass")
  private String testObjectClass;
  @JsonProperty("Tests")
  private List<Test> tests = null;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("DiagnosticsDirectory")
  public String getDiagnosticsDirectory() {
    return diagnosticsDirectory;
  }

  @JsonProperty("DiagnosticsDirectory")
  public void setDiagnosticsDirectory(String diagnosticsDirectory) {
    this.diagnosticsDirectory = diagnosticsDirectory;
  }

  @JsonProperty("ProjectPath")
  public String getProjectPath() {
    return projectPath;
  }

  @JsonProperty("ProjectPath")
  public void setProjectPath(String projectPath) {
    this.projectPath = projectPath;
  }

  @JsonProperty("TargetName")
  public String getTargetName() {
    return targetName;
  }

  @JsonProperty("TargetName")
  public void setTargetName(String targetName) {
    this.targetName = targetName;
  }

  @JsonProperty("TestKind")
  public String getTestKind() {
    return testKind;
  }

  @JsonProperty("TestKind")
  public void setTestKind(String testKind) {
    this.testKind = testKind;
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

  @JsonProperty("Tests")
  public List<Test> getTests() {
    return tests;
  }

  @JsonProperty("Tests")
  public void setTests(List<Test> tests) {
    this.tests = tests;
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
