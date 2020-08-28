package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"@name", "@value"})
public class Property implements Serializable {

  private final static long serialVersionUID = -3282129261538873676L;
  @JsonProperty("@name")
  private String name;
  @JsonProperty("@value")
  private String value;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("@name")
  public String getName() {
    return name;
  }

  @JsonProperty("@name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("@value")
  public String getValue() {
    return value;
  }

  @JsonProperty("@value")
  public void setValue(String value) {
    this.value = value;
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
