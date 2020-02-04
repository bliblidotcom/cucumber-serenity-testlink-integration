
package com.blibli.oss.qa.util.model.cucumber;

import com.fasterxml.jackson.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "duration",
    "status"
})
public class Result {

    @JsonProperty("duration")
    private Timestamp duration;
    @JsonProperty("status")
    private String status;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("duration")
    public Timestamp getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Timestamp duration) {
        this.duration = duration;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("error_message")
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("error_message")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
