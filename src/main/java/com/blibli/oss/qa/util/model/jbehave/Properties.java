package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"properties"})
public class Properties implements Serializable {

  private final static long serialVersionUID = -2564264395306644267L;
  @JsonProperty("properties")
  private List<Property> properties = new ArrayList<>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("properties")
  public List<Property> getProperties() {
    return properties;
  }

  @JsonProperty("properties")
  public void setProperties(List<Property> properties) {
    this.properties = properties;
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
