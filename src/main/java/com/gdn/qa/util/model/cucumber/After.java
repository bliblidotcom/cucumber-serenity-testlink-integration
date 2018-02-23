
package com.gdn.qa.util.model.cucumber;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result",
    "match"
})
public class After {

    @JsonProperty("result")
    private Result_ result;
    @JsonProperty("match")
    private Match_ match;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("result")
    public Result_ getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(Result_ result) {
        this.result = result;
    }

    @JsonProperty("match")
    public Match_ getMatch() {
        return match;
    }

    @JsonProperty("match")
    public void setMatch(Match_ match) {
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
