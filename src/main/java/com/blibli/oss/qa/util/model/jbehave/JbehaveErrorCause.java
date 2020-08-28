package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yunaz.ramadhan on 3/8/2020
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"@class","detailMessage","stackTrace","suppressedExceptions"})
public class JbehaveErrorCause implements Serializable {
  private final static long serialVersionUID = -7852670026304140899L;
  @JsonProperty("detailMessage")
  private String detailMessage;
  @JsonProperty("stackTrace")
  private List<String> stackTrace;
  @JsonProperty("suppressedExceptions")
  private List<String[]> suppressedExceptions;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("detailMessage")
  public String getDetailMessage() {
    return detailMessage;
  }

  @JsonProperty("detailMessage")
  public void setDetailMessage(String detailMessage) {
    this.detailMessage = detailMessage;
  }

  @JsonProperty("stackTrace")
  public List<String> getStackTrace() {
    return stackTrace;
  }

  @JsonProperty("stackTrace")
  public void setStackTrace(List<String> stackTrace) {
    this.stackTrace = stackTrace;
  }

  @JsonProperty("suppressedExceptions")
  public List<String[]> getSuppressedExceptions() {
    return suppressedExceptions;
  }

  @JsonProperty("suppressedExceptions")
  public void setSuppressedExceptions(List<String[]> suppressedExceptions) {
    this.suppressedExceptions = suppressedExceptions;
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
