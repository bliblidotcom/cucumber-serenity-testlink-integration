package com.blibli.oss.qa.util.model.earlgrey;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tri.abror on 2/26/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"FormatVersion", "TestableSummaries"})
public class EarlGreyModel {

  @JsonProperty("FormatVersion")
  private String formatVersion;
  @JsonProperty("TestableSummaries")
  private List<TestableSummary> testableSummaries;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("FormatVersion")
  public String getFormatVersion() {
    return formatVersion;
  }

  @JsonProperty("FormatVersion")
  public void setFormatVersion(String formatVersion) {
    this.formatVersion = formatVersion;
  }

  @JsonProperty("TestableSummaries")
  public List<TestableSummary> getTestableSummaries() {
    return testableSummaries;
  }

  @JsonProperty("TestableSummaries")
  public void setTestableSummaries(List<TestableSummary> testableSummaries) {
    this.testableSummaries = testableSummaries;
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
