package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"matches", "results"})
public class BeforeSteps implements Serializable {

  private final static long serialVersionUID = 7546756789390698687L;
  @JsonProperty("matches")
  private List<Object> matches = new ArrayList<Object>();
  @JsonProperty("results")
  private List<Result> results = new ArrayList<Result>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("matches")
  public List<Object> getMatches() {
    return matches;
  }

  @JsonProperty("matches")
  public void setMatches(List<Object> matches) {
    this.matches = matches;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @JsonProperty("results")
  public List<Result> getResults() {
    return results;
  }

  @JsonProperty("results")
  public void setResults(List<Result> results) {
    this.results = results;
  }
}
