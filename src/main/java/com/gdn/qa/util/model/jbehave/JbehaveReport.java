package com.gdn.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"stories", "scenarios"})
public class JbehaveReport implements Serializable {

  private final static long serialVersionUID = 8792989437194143942L;
  @JsonProperty("stories")
  private List<Story> stories = new ArrayList<Story>();
  @JsonProperty("scenarios")
  private List<Scenario> scenarios = new ArrayList<>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("stories")
  public List<Story> getStories() {
    return stories;
  }

  @JsonProperty("stories")
  public void setStories(List<Story> stories) {
    this.stories = stories;
  }

  @JsonProperty("scenarios")
  public List<Scenario> getScenarios() {
    return scenarios;
  }

  @JsonProperty("scenarios")
  public void setScenarios(List<Scenario> scenarios) {
    this.scenarios = scenarios;
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
