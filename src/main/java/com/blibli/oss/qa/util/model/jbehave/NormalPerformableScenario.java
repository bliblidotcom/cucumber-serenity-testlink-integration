package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"parameters", "givenStories", "beforeSteps", "steps", "afterSteps",
    "storyAndScenarioMeta", "scenario"})
public class NormalPerformableScenario implements Serializable {

  private final static long serialVersionUID = 8383831397037397834L;
  @JsonProperty("parameters")
  private List<Object> parameters = new ArrayList<Object>();
  @JsonProperty("givenStories")
  private List<Object> givenStories = new ArrayList<Object>();
  @JsonProperty("beforeSteps")
  private BeforeSteps beforeSteps;
  @JsonProperty("steps")
  private Steps steps;
  @JsonProperty("afterSteps")
  private AfterSteps afterSteps;
  @JsonProperty("storyAndScenarioMeta")
  private StoryAndScenarioMeta storyAndScenarioMeta;
  @JsonProperty("scenario")
  private ScenarioInfo scenario;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("parameters")
  public List<Object> getParameters() {
    return parameters;
  }

  @JsonProperty("parameters")
  public void setParameters(List<Object> parameters) {
    this.parameters = parameters;
  }

  @JsonProperty("givenStories")
  public List<Object> getGivenStories() {
    return givenStories;
  }

  @JsonProperty("givenStories")
  public void setGivenStories(List<Object> givenStories) {
    this.givenStories = givenStories;
  }

  @JsonProperty("beforeSteps")
  public BeforeSteps getBeforeSteps() {
    return beforeSteps;
  }

  @JsonProperty("beforeSteps")
  public void setBeforeSteps(BeforeSteps beforeSteps) {
    this.beforeSteps = beforeSteps;
  }

  @JsonProperty("steps")
  public Steps getSteps() {
    return steps;
  }

  @JsonProperty("steps")
  public void setSteps(Steps steps) {
    this.steps = steps;
  }

  @JsonProperty("afterSteps")
  public AfterSteps getAfterSteps() {
    return afterSteps;
  }

  @JsonProperty("afterSteps")
  public void setAfterSteps(AfterSteps afterSteps) {
    this.afterSteps = afterSteps;
  }

  @JsonProperty("storyAndScenarioMeta")
  public StoryAndScenarioMeta getStoryAndScenarioMeta() {
    return storyAndScenarioMeta;
  }

  @JsonProperty("storyAndScenarioMeta")
  public void setStoryAndScenarioMeta(StoryAndScenarioMeta storyAndScenarioMeta) {
    this.storyAndScenarioMeta = storyAndScenarioMeta;
  }

  @JsonProperty("scenario")
  public ScenarioInfo getScenario() {
    return scenario;
  }

  @JsonProperty("scenario")
  public void setScenario(ScenarioInfo scenario) {
    this.scenario = scenario;
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
