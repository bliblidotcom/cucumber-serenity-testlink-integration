package com.gdn.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"step", "type", "parametrisedStep", "durationInMillis", "start", "end","throwable"})
public class Result implements Serializable {

  private final static long serialVersionUID = -7812970926304140859L;
  @JsonProperty("step")
  private String step;
  @JsonProperty("type")
  private String type;
  @JsonProperty("parametrisedStep")
  private String parametrisedStep;
  @JsonProperty("durationInMillis")
  private Integer durationInMillis;
  @JsonProperty("start")
  private Integer start;
  @JsonProperty("end")
  private Integer end;
  @JsonProperty("throwable")
  private JbehaveThrowable throwable;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("step")
  public String getStep() {
    return step;
  }

  @JsonProperty("step")
  public void setStep(String step) {
    this.step = step;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("parametrisedStep")
  public String getParametrisedStep() {
    return parametrisedStep;
  }

  @JsonProperty("parametrisedStep")
  public void setParametrisedStep(String parametrisedStep) {
    this.parametrisedStep = parametrisedStep;
  }

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

  @JsonProperty("throwable")
  public JbehaveThrowable getThrowable() {
    return throwable;
  }

  @JsonProperty("throwable")
  public void setThrowable(JbehaveThrowable throwable) {
    this.throwable = throwable;
  }
}
