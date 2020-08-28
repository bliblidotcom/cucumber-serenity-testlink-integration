package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"title", "meta", "givenStories", "examplesTable", "steps"})
public class ScenarioInfo implements Serializable {

  private final static long serialVersionUID = 1662876507844370487L;
  @JsonProperty("title")
  private String title;
  @JsonProperty("meta")
  private Meta meta;
  @JsonProperty("givenStories")
  private GivenStories givenStories;
  @JsonProperty("examplesTable")
  private ExamplesTable examplesTable;
  @JsonProperty("steps")
  private List<String> steps = new ArrayList<String>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
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

  @JsonProperty("examplesTable")
  public ExamplesTable getExamplesTable() {
    return examplesTable;
  }

  @JsonProperty("examplesTable")
  public void setExamplesTable(ExamplesTable examplesTable) {
    this.examplesTable = examplesTable;
  }

  @JsonProperty("steps")
  public List<String> getSteps() {
    return steps;
  }

  @JsonProperty("steps")
  public void setSteps(List<String> steps) {
    this.steps = steps;
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
