package com.gdn.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"transformers"})
public class TableTransformers implements Serializable {

  private final static long serialVersionUID = 4683397395287258724L;
  @JsonProperty("transformers")
  private List<List<String>> transformers = new ArrayList<List<String>>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("transformers")
  public List<List<String>> getTransformers() {
    return transformers;
  }

  @JsonProperty("transformers")
  public void setTransformers(List<List<String>> transformers) {
    this.transformers = transformers;
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
