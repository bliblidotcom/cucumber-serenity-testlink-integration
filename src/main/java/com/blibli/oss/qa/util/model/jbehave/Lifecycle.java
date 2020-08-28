package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"before", "after"})
public class Lifecycle implements Serializable {

  private final static long serialVersionUID = 2608202599847769806L;
  @JsonProperty("before")
  private Before before;
  @JsonProperty("after")
  private List<Object> after = new ArrayList<Object>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("before")
  public Before getBefore() {
    return before;
  }

  @JsonProperty("before")
  public void setBefore(Before before) {
    this.before = before;
  }

  @JsonProperty("after")
  public List<Object> getAfter() {
    return after;
  }

  @JsonProperty("after")
  public void setAfter(List<Object> after) {
    this.after = after;
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
