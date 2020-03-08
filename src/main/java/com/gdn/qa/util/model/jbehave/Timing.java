package com.gdn.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"durationInMillis", "start", "end"})
public class Timing implements Serializable {

  private final static long serialVersionUID = -6988702904613145632L;
  @JsonProperty("durationInMillis")
  private Integer durationInMillis;
  @JsonProperty("start")
  private Integer start;
  @JsonProperty("end")
  private Integer end;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("durationInMillis")
  public Integer getDurationInMillis() {
    return durationInMillis;
  }

  @JsonProperty("durationInMillis")
  public void setDurationInMillis(Integer durationInMillis) {
    this.durationInMillis = durationInMillis;
  }

  @JsonProperty("start")
  public Integer getStart() {
    return start;
  }

  @JsonProperty("start")
  public void setStart(Integer start) {
    this.start = start;
  }

  @JsonProperty("end")
  public Integer getEnd() {
    return end;
  }

  @JsonProperty("end")
  public void setEnd(Integer end) {
    this.end = end;
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
