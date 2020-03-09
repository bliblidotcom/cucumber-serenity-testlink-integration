package com.gdn.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"steps"})
public class Before implements Serializable {

  private final static long serialVersionUID = 5184789225065168022L;
  @JsonProperty("steps")
  private List<List<Object>> steps = new ArrayList<List<Object>>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("steps")
  public List<List<Object>> getSteps() {
    return steps;
  }

  @JsonProperty("steps")
  public void setSteps(List<List<Object>> steps) {
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
