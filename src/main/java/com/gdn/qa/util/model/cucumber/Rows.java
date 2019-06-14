package com.gdn.qa.util.model.cucumber;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cells"
})
public class Rows {
    @JsonProperty("cells")
    private String[] cells;

    @JsonProperty("cells")
    public String[] getCells() {
        return cells;
    }

    @JsonProperty("cells")
    public void setCells(String[] cells) {
        this.cells = cells;
    }

    @Override
    public String toString() {
        return "Rows{" +
                "cells=" + Arrays.toString(cells) +
                '}';
    }
}
