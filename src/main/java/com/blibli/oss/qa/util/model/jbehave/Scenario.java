package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"scenario", "storyPath", "allowed", "normalPerformableScenario",
    "examplePerformableScenarios", "status"})
public class Scenario implements Serializable {

  private final static long serialVersionUID = -8935593674038975543L;
  @JsonProperty("scenario")
  private ScenarioInfo scenario;
  @JsonProperty("storyPath")
  private String storyPath;
  @JsonProperty("allowed")
  private Boolean allowed;
  @JsonProperty("normalPerformableScenario")
  private NormalPerformableScenario normalPerformableScenario;
  @JsonProperty("examplePerformableScenarios")
  private List<Object> examplePerformableScenarios = new ArrayList<Object>();
  @JsonProperty("status")
  private String status;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("scenario")
  public ScenarioInfo getScenario() {
    return scenario;
  }

  @JsonProperty("scenario")
  public void setScenario(ScenarioInfo scenario) {
    this.scenario = scenario;
  }

  @JsonProperty("storyPath")
  public String getStoryPath() {
    return storyPath;
  }

  @JsonProperty("storyPath")
  public void setStoryPath(String storyPath) {
    this.storyPath = storyPath;
  }

  @JsonProperty("allowed")
  public Boolean getAllowed() {
    return allowed;
  }

  @JsonProperty("allowed")
  public void setAllowed(Boolean allowed) {
    this.allowed = allowed;
  }

  @JsonProperty("normalPerformableScenario")
  public NormalPerformableScenario getNormalPerformableScenario() {
    return normalPerformableScenario;
  }

  @JsonProperty("normalPerformableScenario")
  public void setNormalPerformableScenario(NormalPerformableScenario normalPerformableScenario) {
    this.normalPerformableScenario = normalPerformableScenario;
  }

  @JsonProperty("examplePerformableScenarios")
  public List<Object> getExamplePerformableScenarios() {
    return examplePerformableScenarios;
  }

  @JsonProperty("examplePerformableScenarios")
  public void setExamplePerformableScenarios(List<Object> examplePerformableScenarios) {
    this.examplePerformableScenarios = examplePerformableScenarios;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
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
