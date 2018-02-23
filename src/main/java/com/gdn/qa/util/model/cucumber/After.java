
package com.gdn.qa.util.model.cucumber;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "result",
        "match"
})
public class After {

    @JsonProperty("result")
    private Result result;
    @JsonProperty("match")
    private Match match;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("result")
    public Result getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(Result result) {
        this.result = result;
    }

    @JsonProperty("match")
    public Match getMatch() {
        return match;
    }

    @JsonProperty("match")
    public void setMatch(Match match) {
        this.match = match;
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
