package com.blibli.oss.qa.util.reader;

import com.blibli.oss.qa.util.model.ScenarioData;
import com.blibli.oss.qa.util.model.TestLinkData;
import com.blibli.oss.qa.util.model.earlgrey.EarlGreyModel;
import com.blibli.oss.qa.util.model.earlgrey.Subtest2;
import com.blibli.oss.qa.util.model.earlgrey.Subtest3;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author yunaz.ramadhan on 3/3/2020
 */
public class EarlGreyTestResultReader extends BaseTestResultReader<EarlGreyModel> {
  public EarlGreyTestResultReader(TestLinkData testLinkData, String reportFolder) throws Exception {
    super(testLinkData, reportFolder);
  }

  @Override
  public Map<Integer, Map<String, ScenarioData>> groupingScenariosByTestSuiteId(EarlGreyModel reports)
      throws Exception {
    Map<Integer, Map<String, ScenarioData>> groupedFeature = new ConcurrentHashMap<>();

    System.out.println("Populating scenarios from :");
    if (reports.getTestableSummaries() != null) {
      List<Subtest2> resultTest = reports.getTestableSummaries()
          .get(0)
          .getTests()
          .get(0)
          .getSubtest1s()
          .get(0)
          .getSubtest2s();

      String scenarioName =
          reports.getTestableSummaries().get(0).getTestName().split("_")[1].replace("()", "");
      resultTest.parallelStream().forEach(subTest -> {
        System.out.println("Test " + subTest.getTestName());
        Integer testSuiteId;
        try {
          testSuiteId = Integer.valueOf(subTest.getTestName().split("_")[2]);
        } catch (Exception ignored) {
          testSuiteId = -1;
        }
        String reason = "";
        Integer indexFail = null;
        Double duration = subTest.getDuration() / 60000;
        boolean status = true;
        for (int j = 0; j < subTest.getSubtest3s().size(); j++) {
          if (!subTest.getSubtest3s().get(j).getTestStatus().equalsIgnoreCase("success")) {
            reason = String.format("Fail at step %s, : %s",
                j + 1,
                subTest.getSubtest3s().get(j).getTestName());
            indexFail = j;
            status = false;
            break;
          }
        }
        String name = subTest.getTestName().split("_")[1].replace("()", "");
        boolean finalStatus = status;
        String finalReason = reason;
        Integer finalIndexFail = indexFail;
        List<String[]> steps = readSteps(subTest.getSubtest3s());
        ScenarioData scenarioData = new ScenarioData();
        scenarioData.setReasonFail(finalReason);
        scenarioData.setIndexFail(finalIndexFail);
        scenarioData.setPassed(finalStatus);
        scenarioData.setName(name);
        scenarioData.setSummary(name);
        scenarioData.setTestCase(constructTestCase(testSuiteId,
            null,
            name,
            name,
            finalStatus,
            steps));
        scenarioData.setDuration(duration);
        scenarioData.setFeatureName(resultTest.get(0).getTestName());
        scenarioData.setDetailToPrint(printDetail(null,
            name,
            scenarioName,
            status,
            scenarioData.getReasonFail(),
            String.valueOf(testSuiteId),
            ""));
        if (groupedFeature.containsKey(testSuiteId)) {
          Map<String, ScenarioData> scenarios = groupedFeature.get(testSuiteId);
          scenarios.put(name, scenarioData);
          groupedFeature.put(testSuiteId, scenarios);
        } else {
          Map<String, ScenarioData> scenarios = new HashMap<>();
          scenarios.put(name, scenarioData);
          groupedFeature.put(testSuiteId, scenarios);
        }
      });
    }

    return groupedFeature;
  }

  @Override
  public EarlGreyModel filterReportFiles(List<File> allFiles) throws Exception {
    EarlGreyModel result = null;
    if (allFiles != null && !allFiles.isEmpty()) {
      for (File file : allFiles) {
        try {
          EarlGreyModel dataList = readFileToObject(file, EarlGreyModel.class);
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

  private List<String[]> readSteps(List<Subtest3> steps) {
    return IntStream.range(0, steps.size())
        .parallel()
        .mapToObj(counter -> constructStep(steps.get(counter), counter))
        .collect(Collectors.toList());
  }

  private String[] constructStep(Subtest3 step, int counter) {
    String additionalInfo = step.getTestIdentifier() + " " + step.getTestSummaryGUID();
    String stepDefinition = String.format("%s%s", step.getTestName(), additionalInfo);
    String status = String.valueOf(step.getTestStatus().equalsIgnoreCase("success"));
    String errorMessage = String.format("Fail at step %s, %s", counter + 1, step.getTestName());
    String notes = "";
    if (!Boolean.parseBoolean(status)) {
      notes =
          String.format("This Test Case Failed on step %s, Log:\n%s", counter + 1, errorMessage);
    }

    return new String[] {stepDefinition, status, errorMessage, notes};
  }
}
