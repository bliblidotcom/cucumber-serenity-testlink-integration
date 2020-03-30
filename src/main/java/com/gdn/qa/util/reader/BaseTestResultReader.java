package com.gdn.qa.util.reader;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.*;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gdn.qa.util.model.ScenarioData;
import com.gdn.qa.util.model.TestLinkData;
import com.gdn.qa.util.service.TestLinkConnectionController;
import com.gdn.qa.util.service.TestLinkPlugin;

import java.io.File;
import java.util.*;

import static com.gdn.qa.util.BadakReporter.printConfiguration;

/**
 * @author yunaz.ramadhan on 3/1/2020
 */
public abstract class BaseTestResultReader<T> {
  String testProject;
  String testPlan;
  String build;
  String platFormName;
  String DEVKEY;
  String urlTestlink;
  TestLinkAPI connection;
  String testReportFolder;
  Long totalPass;
  Long totalFail;
  Long linked;
  private ObjectMapper mapper;

  public BaseTestResultReader(TestLinkData testLinkData, String reportFolder) throws Exception {
    urlTestlink = testLinkData.getUrlTestlink();
    DEVKEY = testLinkData.getDEVKEY();
    testProject = testLinkData.getTestProject();
    testPlan = testLinkData.getTestPlan();
    build = testLinkData.getBuild();
    platFormName = testLinkData.getPlatFormName();
    testReportFolder = reportFolder;
    printConfiguration(testLinkData, reportFolder);
    connection = connectToTestlink();
    totalPass = 0L;
    totalFail = 0L;
    linked = 0L;
  }

  private TestLinkAPI connectToTestlink() throws Exception {
    return TestLinkConnectionController.getInstance().
        setUrlTestlink(urlTestlink).setDevKey(DEVKEY).connect();
  }

  TestCase constructTestCase(Integer testSuiteId,
      String fullExternalId,
      String name,
      String summary,
      Boolean status,
      List<String[]> steps) {
    TestCase testCase = new TestCase();
    testCase.setName(name);
    testCase.setSummary(summary);
    testCase.setPreconditions(summary);
    testCase.setSteps(constructTestCaseSteps(steps));
    testCase.setAuthorLogin("automation-test");
    testCase.setTestCaseStatus(TestCaseStatus.FINAL);
    testCase.setTestImportance(TestImportance.MEDIUM);
    testCase.setActionOnDuplicatedName(ActionOnDuplicate.CREATE_NEW_VERSION);
    testCase.setExecutionType(ExecutionType.AUTOMATED);
    testCase.setCheckDuplicatedName(true);
    testCase.setExecutionOrder(0);
    testCase.setTestSuiteId(testSuiteId);
    testCase.setInternalId(null);
    testCase.setExternalId(getIdNumberFromString(fullExternalId));
    testCase.setFullExternalId(fullExternalId);
    testCase.setExecutionStatus(status ? ExecutionStatus.PASSED : ExecutionStatus.FAILED);
    return testCase;
  }

  private List<TestCaseStep> constructTestCaseSteps(List<String[]> steps) {
    List<TestCaseStep> testCaseSteps = new ArrayList<>();
    for (int i = 0; i < steps.size(); i++) {
      TestCaseStep caseStep = new TestCaseStep();
      caseStep.setActions(steps.get(i)[0]);
      caseStep.setNumber(i + 1);
      caseStep.setActive(true);
      caseStep.setExecutionType(ExecutionType.AUTOMATED);
      caseStep.setId(i + 1);
      caseStep.setTestCaseVersionId(null);
      testCaseSteps.add(caseStep);
    }
    return testCaseSteps;
  }

  public void writeToTestLink() throws Exception {
    Map<Integer, Map<String, ScenarioData>> groupedFeature =
        groupingScenariosByTestSuiteId(filterReportFiles(getTestReports()));

    // Process grouped feature
    if (!groupedFeature.isEmpty()) {
      for (Integer testSuiteId : groupedFeature.keySet()) {
        try {
          countTestCaseResult(groupedFeature.get(testSuiteId));
          TestLinkPlugin plugin =
              new TestLinkPlugin(connection, testProject, testPlan, build, platFormName);
          if (testSuiteId != null && testSuiteId > 0) {
            linked += plugin.linkTestCases(testSuiteId, groupedFeature.get(testSuiteId));
          } else {
            linked += plugin.linkTestCasesUsingTestLinkId(groupedFeature.get(testSuiteId));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    printResult();
  }

  private void printResult() {
    System.out.format("%n|---------------- Test Cases Summary ----------------|%n");
    System.out.format("+----------------------+---------+---------+---------+%n");
    System.out.format("| Total Test Cases     |Passed   |Failed   |Linked   |%n");
    System.out.format("+----------------------+---------+---------+---------+%n");


    String total = String.format("| %-20s |", (totalPass + totalFail));
    System.out.format(total);
    System.out.print("\033[32m");
    String pass = String.format(" %-7s ", totalPass);
    System.out.format(pass);
    System.out.print("\033[30m");
    System.out.print("|");
    System.out.print("\033[31m");
    String fail = String.format(" %-7s ", totalFail);
    System.out.format(fail);
    System.out.print("\033[30m");
    System.out.print("|");
    System.out.print("\033[35m");
    String skip = String.format(" %-7s ", linked);
    System.out.format(skip);
    System.out.print("\033[30m");
    System.out.println("|");
    System.out.format("+----------------------+---------+---------+---------+%n");
  }

  private void countTestCaseResult(Map<String, ScenarioData> data) {
    long temp = data.values().stream().filter(ScenarioData::getPassed).count();
    totalPass += temp;
    totalFail += data.size() - temp;
  }

  Integer getIdNumberFromString(String fullExternalId) {
    if (fullExternalId != null && !fullExternalId.trim().isEmpty()) {
      try {
        return Integer.valueOf(fullExternalId.replaceAll("[^\\d.]", ""));
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

  String printDetail(List<String[]> steps,
      String featureName,
      String title,
      boolean status,
      String notes,
      String testSuiteId,
      String testlinkId) {
    StringBuilder builder = new StringBuilder();
    builder.append("\n================================================\n");
    if (featureName != null && !featureName.trim().isEmpty()) {
      builder.append("Feature\t\t: ").append(featureName).append("\n");
    }
    builder.append("Title\t\t: ")
        .append(title)
        .append("\n")
        .append("Passed\t\t: ")
        .append(status)
        .append("\n")
        .append("Reason Fail\t: ")
        .append(notes)
        .append("\n")
        .append("TestSuiteId\t: ")
        .append(testSuiteId)
        .append("\n")
        .append("TestLinkId\t: ")
        .append(testlinkId)
        .append("\n");
    if (steps != null && !steps.isEmpty()) {
      builder.append("Steps\t\t:\n");
      steps.forEach(step -> {
        builder.append(String.format("%s\n", step[0]));
      });
    }
    builder.append("================================================\n");
    return builder.toString();
  }

  /**
   * @return List of file ordered by created date,
   * only filter files in the specified directory with accepted file type
   * Accepted file type for reports are .json and .xml
   * @throws Exception when there are failure during execution
   */
  private List<File> getTestReports() throws Exception {
    if (testReportFolder.trim().isEmpty()) {
      throw new Exception("No test report folder specified");
    }
    File folder = new File(testReportFolder);
    if (folder.isDirectory()) {
      File[] files =
          folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml") || name.toLowerCase()
              .endsWith(".json"));
      if (files == null || files.length == 0) {
        throw new Exception("No test report found in folder : " + testReportFolder);
      }
      Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
      return Arrays.asList(files);
    } else {
      if (folder.getName().toLowerCase().endsWith(".xml") || folder.getName()
          .toLowerCase()
          .endsWith(".json")) {
        return Collections.singletonList(folder);
      } else {
        throw new Exception("Unknown file format for " + testReportFolder);
      }
    }
  }

  <U> U readFileToObject(File file, Class<U> tClass) throws Exception {
    if (file.getName().toLowerCase().endsWith(".xml")) {
      this.mapper = new XmlMapper();
    } else if (file.getName().toLowerCase().endsWith(".json")) {
      this.mapper = new ObjectMapper();
    } else {
      throw new Exception("Unknown file type, ignore");
    }
    return this.mapper.readValue(file, tClass);
  }

  abstract Map<Integer, Map<String, ScenarioData>> groupingScenariosByTestSuiteId(T reports)
      throws Exception;

  abstract T filterReportFiles(List<File> allFiles) throws Exception;
}
