package com.gdn.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"path", "description", "narrative", "meta", "givenStories", "lifecycle",
    "scenarios", "name"})
public class StoryInfo implements Serializable {

  private final static long serialVersionUID = -2333613887246005341L;
  @JsonProperty("path")
  private String path;
  @JsonProperty("description")
  private Description description;
  @JsonProperty("narrative")
  private Narrative narrative;
  @JsonProperty("meta")
  private Meta meta;
  @JsonProperty("givenStories")
  private GivenStories givenStories;
  @JsonProperty("lifecycle")
  private Lifecycle lifecycle;
  @JsonProperty("scenarios")
  private List<ScenarioInfo> scenarios = new ArrayList<ScenarioInfo>();
  @JsonProperty("name")
  private String name;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("path")
  public String getPath() {
    return path;
  }

  @JsonProperty("path")
  public void setPath(String path) {
    this.path = path;
  }

  @JsonProperty("description")
  public Description getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(Description description) {
    this.description = description;
  }

  @JsonProperty("narrative")
  public Narrative getNarrative() {
    return narrative;
  }

  @JsonProperty("narrative")
  public void setNarrative(Narrative narrative) {
    this.narrative = narrative;
  }

  @JsonProperty("meta")
  public Meta getMeta() {
    return meta;
  }

  @JsonProperty("meta")
  public void setMeta(Meta meta) {
    this.meta = meta;
  }

  @JsonProperty("givenStories")
  public GivenStories getGivenStories() {
    return givenStories;
  }

  @JsonProperty("givenStories")
  public void setGivenStories(GivenStories givenStories) {
    this.givenStories = givenStories;
  }

  @JsonProperty("lifecycle")
  public Lifecycle getLifecycle() {
    return lifecycle;
  }

  @JsonProperty("lifecycle")
  public void setLifecycle(Lifecycle lifecycle) {
    this.lifecycle = lifecycle;
  }

  @JsonProperty("scenarios")
  public List<ScenarioInfo> getScenarios() {
    return scenarios;
  }

  @JsonProperty("scenarios")
  public void setScenarios(List<ScenarioInfo> scenarios) {
    this.scenarios = scenarios;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
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
