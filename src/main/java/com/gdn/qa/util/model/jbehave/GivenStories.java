package com.gdn.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"givenStories", "givenStoriesAsString", "examplesTable"})
public class GivenStories implements Serializable {

  private final static long serialVersionUID = -6464844406120779302L;
  @JsonProperty("givenStories")
  private List<Object> givenStories = new ArrayList<Object>();
  @JsonProperty("givenStoriesAsString")
  private String givenStoriesAsString;
  @JsonProperty("examplesTable")
  private ExamplesTable examplesTable;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("givenStories")
  public List<Object> getGivenStories() {
    return givenStories;
  }

  @JsonProperty("givenStories")
  public void setGivenStories(List<Object> givenStories) {
    this.givenStories = givenStories;
  }

  @JsonProperty("givenStoriesAsString")
  public String getGivenStoriesAsString() {
    return givenStoriesAsString;
  }

  @JsonProperty("givenStoriesAsString")
  public void setGivenStoriesAsString(String givenStoriesAsString) {
    this.givenStoriesAsString = givenStoriesAsString;
  }

  @JsonProperty("examplesTable")
  public ExamplesTable getExamplesTable() {
    return examplesTable;
  }

  @JsonProperty("examplesTable")
  public void setExamplesTable(ExamplesTable examplesTable) {
    this.examplesTable = examplesTable;
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
