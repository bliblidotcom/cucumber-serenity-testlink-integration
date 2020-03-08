package com.gdn.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"descriptionAsString"})
public class Description implements Serializable {

  private final static long serialVersionUID = -830889317713023502L;
  @JsonProperty("descriptionAsString")
  private String descriptionAsString;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("descriptionAsString")
  public String getDescriptionAsString() {
    return descriptionAsString;
  }

  @JsonProperty("descriptionAsString")
  public void setDescriptionAsString(String descriptionAsString) {
    this.descriptionAsString = descriptionAsString;
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
