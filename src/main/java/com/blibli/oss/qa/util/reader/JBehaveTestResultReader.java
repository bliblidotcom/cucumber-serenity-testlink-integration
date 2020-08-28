package com.blibli.oss.qa.util.reader;

import com.blibli.oss.qa.util.model.ScenarioData;
import com.blibli.oss.qa.util.model.TagsReader;
import com.blibli.oss.qa.util.model.TestLinkData;
import com.blibli.oss.qa.util.model.jbehave.JbehaveReport;
import com.blibli.oss.qa.util.model.jbehave.Property;
import com.blibli.oss.qa.util.model.jbehave.Result;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author yunaz.ramadhan on 3/7/2020
 */
public class JBehaveTestResultReader extends BaseTestResultReader<JbehaveReport>
    implements TagsReader<List<Property>> {
  public JBehaveTestResultReader(TestLinkData testLinkData, String reportFolder) throws Exception {
    super(testLinkData, reportFolder);
  }

  @Override
  Map<Integer, Map<String, ScenarioData>> groupingScenariosByTestSuiteId(JbehaveReport reports)
      throws Exception {
    Map<Integer, Map<String, ScenarioData>> groupedFeature = new ConcurrentHashMap<>();

    if (reports != null) {
      System.out.println("Populating scenarios from story :");
      reports.getStories().parallelStream().forEach(story -> {
        System.out.println(story.getStory().getName());
        String summary = story.getLocalizedNarrative();

        story.getScenarios().parallelStream().forEach(scenario -> {
          ScenarioData scenarioData = new ScenarioData();
          scenarioData.setTreeNode(getTreeNode(scenario.getStoryPath()));
          Integer testSuiteId = getIdNumberFromString(getValueFromTags("testsuiteid",
              scenario.getNormalPerformableScenario().getStoryAndScenarioMeta().getProperties()));
          String testLinkId = getValueFromTags("testlinkid",
              scenario.getNormalPerformableScenario().getStoryAndScenarioMeta().getProperties());
          testSuiteId = testSuiteId == null ? -1 : testSuiteId;

          List<String[]> steps = new ArrayList<>();
          if (!scenario.getNormalPerformableScenario().getBeforeSteps().getResults().isEmpty()) {
            steps.addAll(0,
                readSteps(scenario.getNormalPerformableScenario().getBeforeSteps().getResults()));
          }

          if (!scenario.getNormalPerformableScenario().getAfterSteps().getResults().isEmpty()) {
            steps.addAll(readSteps(scenario.getNormalPerformableScenario()
                .getAfterSteps()
                .getResults()));
          }
          String reason = "";
          Integer indexFail = null;
          boolean status = true;
          for (int j = 0; j < steps.size(); j++) {
            if (!steps.get(j)[3].trim().isEmpty()) {
              reason = steps.get(j)[3];
              indexFail = j;
              status = false;
              break;
            }
          }
          Double duration = (double) (story.getTiming().getDurationInMillis() / 60000);
          scenarioData.setReasonFail(reason);
          scenarioData.setIndexFail(indexFail);
          scenarioData.setPassed(status);
          scenarioData.setName(scenario.getScenario().getTitle());
          scenarioData.setSummary(summary);
          scenarioData.setTestCase(constructTestCase(testSuiteId,
              testLinkId,
              scenario.getScenario().getTitle(),
              summary,
              status,
              steps));
          scenarioData.setDuration(duration);
          scenarioData.setFeatureName(story.getStory().getName());
          scenarioData.setDetailToPrint(printDetail(null,
              story.getStory().getName(),
              scenario.getScenario().getTitle(),
              status,
              scenarioData.getReasonFail(),
              testSuiteId.toString(),
              testLinkId));
          if (groupedFeature.containsKey(testSuiteId)) {
            Map<String, ScenarioData> scenarios = groupedFeature.get(testSuiteId);
            scenarios.put(scenario.getScenario().getTitle(), scenarioData);
            groupedFeature.put(testSuiteId, scenarios);
          } else {
            Map<String, ScenarioData> scenarios = new HashMap<>();
            scenarios.put(scenario.getScenario().getTitle(), scenarioData);
            groupedFeature.put(testSuiteId, scenarios);
          }
        });
      });
    }
    return groupedFeature;
  }

  private List<String[]> readSteps(List<Result> steps) {
    return IntStream.range(0, steps.size())
        .parallel()
        .mapToObj(counter -> constructStep(steps.get(counter), counter))
        .collect(Collectors.toList());
  }

  private String[] constructStep(Result step, int counter) {
    String additionalInfo = step.getParametrisedStep();
    String stepDefinition = String.format("%s %s", step.getStep(), additionalInfo);
    String status = String.valueOf(step.getType().equalsIgnoreCase("successful"));
    String errorMessage = step.getThrowable() == null ? "" : step.getThrowable().getDetailMessage();
    String notes = "";
    if (!Boolean.parseBoolean(status)) {
      notes =
          String.format("This Test Case Failed on step %s, Log:\n%s", counter + 1, errorMessage);
    }

    return new String[] {stepDefinition, status, errorMessage, notes};
  }

  @Override
  JbehaveReport filterReportFiles(List<File> allFiles) throws Exception {
    JbehaveReport result = null;
    if (allFiles != null && !allFiles.isEmpty()) {
      for (File file : allFiles) {
        try {
          JbehaveReport dataList = readFileToObject(file, JbehaveReport.class);
          if (dataList != null) {
            result = dataList;
            break;
          }
        } catch (Exception ignored) {

        }
      }
    }
    return result;
  }

  @Override
  public String getValueFromTags(String tagName, List<Property> tags) {
    String result = null;
    try {
      Optional<Property> selected = tags.parallelStream()
          .filter(e -> e.getName().equalsIgnoreCase(tagName.toLowerCase()))
          .findFirst();
      if (selected.isPresent()) {
        result = selected.get().getValue().trim();
      }
    } catch (Exception ignored) {
      return null;
    }
    return result;
  }
}
