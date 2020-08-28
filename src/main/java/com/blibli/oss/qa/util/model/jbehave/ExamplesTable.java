package com.blibli.oss.qa.util.model.jbehave;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"tableAsString", "tableTransformers", "headers", "data", "properties",
    "propertiesAsString", "namedParameters"})
public class ExamplesTable implements Serializable {

  private final static long serialVersionUID = 4587995778599550795L;
  @JsonProperty("tableAsString")
  private String tableAsString;
  @JsonProperty("tableTransformers")
  private TableTransformers tableTransformers;
  @JsonProperty("headers")
  private List<Object> headers = new ArrayList<Object>();
  @JsonProperty("data")
  private List<Object> data = new ArrayList<Object>();
  @JsonProperty("properties")
  private Properties properties;
  @JsonProperty("propertiesAsString")
  private String propertiesAsString;
  @JsonProperty("namedParameters")
  private List<Object> namedParameters = new ArrayList<Object>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("tableAsString")
  public String getTableAsString() {
    return tableAsString;
  }

  @JsonProperty("tableAsString")
  public void setTableAsString(String tableAsString) {
    this.tableAsString = tableAsString;
  }

  @JsonProperty("tableTransformers")
  public TableTransformers getTableTransformers() {
    return tableTransformers;
  }

  @JsonProperty("tableTransformers")
  public void setTableTransformers(TableTransformers tableTransformers) {
    this.tableTransformers = tableTransformers;
  }

  @JsonProperty("headers")
  public List<Object> getHeaders() {
    return headers;
  }

  @JsonProperty("headers")
  public void setHeaders(List<Object> headers) {
    this.headers = headers;
  }

  @JsonProperty("data")
  public List<Object> getData() {
    return data;
  }

  @JsonProperty("data")
  public void setData(List<Object> data) {
    this.data = data;
  }

  @JsonProperty("properties")
  public Properties getProperties() {
    return properties;
  }

  @JsonProperty("properties")
  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  @JsonProperty("propertiesAsString")
  public String getPropertiesAsString() {
    return propertiesAsString;
  }

  @JsonProperty("propertiesAsString")
  public void setPropertiesAsString(String propertiesAsString) {
    this.propertiesAsString = propertiesAsString;
  }

  @JsonProperty("namedParameters")
  public List<Object> getNamedParameters() {
    return namedParameters;
  }

  @JsonProperty("namedParameters")
  public void setNamedParameters(List<Object> namedParameters) {
    this.namedParameters = namedParameters;
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
