package com.blibli.oss.qa.util;

import com.blibli.oss.qa.util.model.cucumber.Tags;

import java.util.ArrayList;
import java.util.List;

public class TagsReader {

    public static List<TestlinkTags> readTags(List<Tags> tags) {
        List<TestlinkTags> data = new ArrayList<>();
        tags.stream().filter(tag -> (tag.getName().toLowerCase().contains("testlinkid")
                || tag.getName().toLowerCase().contains("testsuiteid"))
        ).forEach(filterData -> {
            TestlinkTags tag = new TestlinkTags();
            boolean isFound = false;
            if (filterData.getName().split("=").length == 1) {
                System.out.println("=== Test Link ID not found , please use @TESTLINKID=TESTLINKID (Ex. @TestlinkId=123) - Without Space on : " + filterData.getName() + " ===");
            }
            if (filterData.getName().toLowerCase().contains("testlinkid")) {
                isFound = true;
                tag.setTestLinkId(filterData.getName().split("=")[1].trim());
            }
            if (filterData.getName().toLowerCase().contains("testsuiteid")) {
                isFound = true;
                tag.setTestSuiteId(filterData.getName().split("=")[1].trim());
            }
            if (isFound) {
                data.add(tag);
            }
        });

        return data;
    }
}
