
package com.blibli.oss.qa.util.model.cucumber;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "before",
        "line",
        "name",
        "description",
        "id",
        "after",
        "type",
        "keyword",
        "steps",
        "tags"
})
public class Elements {

    @JsonProperty("before")
    private List<Before> before = null;
    @JsonProperty("line")
    private Integer line;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("id")
    private String id;
    @JsonProperty("after")
    private List<After> after = null;
    @JsonProperty("type")
    private String type;
    @JsonProperty("keyword")
    private String keyword;
    @JsonProperty("steps")
    private List<Step> steps = null;
    @JsonProperty("tags")
    private List<Tags> tags = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("before")
    public List<Before> getBefore() {
        return before;
    }

    @JsonProperty("before")
    public void setBefore(List<Before> before) {
        this.before = before;
    }

    @JsonProperty("line")
    public Integer getLine() {
        return line;
    }

    @JsonProperty("line")
    public void setLine(Integer line) {
        this.line = line;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("after")
    public List<After> getAfter() {
        return after;
    }

    @JsonProperty("after")
    public void setAfter(List<After> after) {
        this.after = after;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("keyword")
    public String getKeyword() {
        return keyword;
    }

    @JsonProperty("keyword")
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @JsonProperty("steps")
    public List<Step> getSteps() {
        return steps;
    }

    @JsonProperty("steps")
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonProperty("tags")
    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    @JsonProperty("tags")
    public List<Tags> getTags() {
        return tags;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
