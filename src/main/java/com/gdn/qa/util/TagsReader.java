package com.gdn.qa.util;

import com.gdn.qa.util.model.cucumber.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TagsReader {

  public static Integer getTestSuiteIdFromTags(List<Tags> tags) {
    try {
      AtomicReference<Integer> result = new AtomicReference<>();
      if (tags != null && !tags.isEmpty()) {
        List<Tags> filteredTags = tags.parallelStream()
            .filter(e -> e.getName().toLowerCase().contains("testsuiteid"))
            .collect(Collectors.toList());
        if (!filteredTags.isEmpty()) {
          filteredTags.forEach(tag -> {
            String testSuiteIdInString = tag.getName().replaceAll("[^\\d.]", "");
            testSuiteIdInString = testSuiteIdInString.trim();
            if (!testSuiteIdInString.isEmpty()) {
              result.set(Integer.valueOf(testSuiteIdInString));
            } else {
              result.set(null);
            }
          });
        }
      }
      return result.get();
    } catch (Exception e) {
      return null;
    }
  }

  public static List<TestlinkTags> readTags(List<Tags> tags) {
    List<TestlinkTags> data = new ArrayList<>();
    tags.stream()
        .filter(tag -> (tag.getName().toLowerCase().contains("testlinkid") || tag.getName()
            .toLowerCase()
            .contains("testsuiteid")))
        .forEach(filterData -> {
          TestlinkTags tag = new TestlinkTags();
          boolean isFound = false;
          if (filterData.getName().split("=").length == 1) {
            System.out.println(
                "=== Test Link ID not found , please use @TESTLINKID=TESTLINKID (Ex. @TestlinkId=123) - Without Space on : "
                    + filterData.getName() + " ===");
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
