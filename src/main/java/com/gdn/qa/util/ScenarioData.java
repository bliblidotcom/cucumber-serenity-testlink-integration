package com.gdn.qa.util;

public class ScenarioData {
    private String name;
    private TestlinkTags testlinkTags;
    private Boolean passed;
    private String reasonFail;
    private Double duration;

    public ScenarioData() {
        this.passed = true;
        this.reasonFail = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public String getReasonFail() {
        return reasonFail;
    }

    public void setReasonFail(String reasonFail) {
        this.reasonFail = reasonFail;
    }

    public TestlinkTags getTestlinkTags() {
        return testlinkTags;
    }

    public void setTestlinkTags(TestlinkTags testlinkTags) {
        this.testlinkTags = testlinkTags;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }
}
