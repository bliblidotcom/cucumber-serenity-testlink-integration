package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"story", "localizedNarrative", "allowed", "status", "givenStories", "scenarios",
    "beforeSteps", "afterSteps", "timing", "givenStory"})
public class Story implements Serializable {

  private final static long serialVersionUID = -687539236934932108L;
  @JsonProperty("story")
  private StoryInfo story;
  @JsonProperty("localizedNarrative")
  private String localizedNarrative;
  @JsonProperty("allowed")
  private Boolean allowed;
  @JsonProperty("status")
  private String status;
  @JsonProperty("givenStories")
  private List<Object> givenStories = new ArrayList<Object>();
  @JsonProperty("scenarios")
  private List<Scenario> scenarios = new ArrayList<>();
  @JsonProperty("beforeSteps")
  private BeforeSteps beforeSteps;
  @JsonProperty("afterSteps")
  private AfterSteps afterSteps;
  @JsonProperty("timing")
  private Timing timing;
  @JsonProperty("givenStory")
  private Boolean givenStory;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("story")
  public StoryInfo getStory() {
    return story;
  }

  @JsonProperty("story")
  public void setStory(StoryInfo story) {
    this.story = story;
  }

  @JsonProperty("localizedNarrative")
  public String getLocalizedNarrative() {
    return localizedNarrative;
  }

  @JsonProperty("localizedNarrative")
  public void setLocalizedNarrative(String localizedNarrative) {
    this.localizedNarrative = localizedNarrative;
  }

  @JsonProperty("allowed")
  public Boolean getAllowed() {
    return allowed;
  }

  @JsonProperty("allowed")
  public void setAllowed(Boolean allowed) {
    this.allowed = allowed;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty("givenStories")
  public List<Object> getGivenStories() {
    return givenStories;
  }

  @JsonProperty("givenStories")
  public void setGivenStories(List<Object> givenStories) {
    this.givenStories = givenStories;
  }

  @JsonProperty("scenarios")
  public List<Scenario> getScenarios() {
    return scenarios;
  }

  @JsonProperty("scenarios")
  public void setScenarios(List<Scenario> scenarios) {
    this.scenarios = scenarios;
  }

  @JsonProperty("beforeSteps")
  public BeforeSteps getBeforeSteps() {
    return beforeSteps;
  }

  @JsonProperty("beforeSteps")
  public void setBeforeSteps(BeforeSteps beforeSteps) {
    this.beforeSteps = beforeSteps;
  }

  @JsonProperty("afterSteps")
  public AfterSteps getAfterSteps() {
    return afterSteps;
  }

  @JsonProperty("afterSteps")
  public void setAfterSteps(AfterSteps afterSteps) {
    this.afterSteps = afterSteps;
  }

  @JsonProperty("timing")
  public Timing getTiming() {
    return timing;
  }

  @JsonProperty("timing")
  public void setTiming(Timing timing) {
    this.timing = timing;
  }

  @JsonProperty("givenStory")
  public Boolean getGivenStory() {
    return givenStory;
  }

  @JsonProperty("givenStory")
  public void setGivenStory(Boolean givenStory) {
    this.givenStory = givenStory;
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
