package com.gdn.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yunaz.ramadhan on 3/8/2020
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"detailMessage","stackTrace","suppressedExceptions","uuid"})
public class JbehaveThrowable implements Serializable {
  private final static long serialVersionUID = -7812970926304140899L;
  @JsonProperty("detailMessage")
  private String detailMessage;
  @JsonProperty("stackTrace")
  private List<String> stackTrace;
  @JsonProperty("suppressedExceptions")
  private List<String[]> suppressedExceptions;
  @JsonProperty("uuid")
  private String uuid;
  @JsonProperty("cause")
  private JbehaveErrorCause cause;
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

  @JsonProperty("cause")
  public JbehaveErrorCause getCause() {
    return cause;
  }

  @JsonProperty("cause")
  public void setCause(JbehaveErrorCause cause) {
    this.cause = cause;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  @JsonProperty("uuid")
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
