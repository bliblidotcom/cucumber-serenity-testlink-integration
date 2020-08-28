package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"inOrderTo", "asA", "iWantTo", "soThat"})
public class Narrative implements Serializable {

  private final static long serialVersionUID = 8613600129603546368L;
  @JsonProperty("inOrderTo")
  private String inOrderTo;
  @JsonProperty("asA")
  private String asA;
  @JsonProperty("iWantTo")
  private String iWantTo;
  @JsonProperty("soThat")
  private String soThat;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("inOrderTo")
  public String getInOrderTo() {
    return inOrderTo;
  }

  @JsonProperty("inOrderTo")
  public void setInOrderTo(String inOrderTo) {
    this.inOrderTo = inOrderTo;
  }

  @JsonProperty("asA")
  public String getAsA() {
    return asA;
  }

  @JsonProperty("asA")
  public void setAsA(String asA) {
    this.asA = asA;
  }

  @JsonProperty("iWantTo")
  public String getIWantTo() {
    return iWantTo;
  }

  @JsonProperty("iWantTo")
  public void setIWantTo(String iWantTo) {
    this.iWantTo = iWantTo;
  }

  @JsonProperty("soThat")
  public String getSoThat() {
    return soThat;
  }

  @JsonProperty("soThat")
  public void setSoThat(String soThat) {
    this.soThat = soThat;
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
