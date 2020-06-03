package com.gdn.qa.util.service;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.*;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import com.gdn.qa.util.constant.CheckDuplicateTestStatus;
import com.gdn.qa.util.constant.ReportGeneratorPolicy;
import com.gdn.qa.util.model.ScenarioData;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by argo.triwidodo on 06/12/2016.
 * Updated by yunaz.ramadhan on 1/30/2020
 */
public class TestLinkPlugin {
  private TestLinkAPI connection;
  private Integer buildId;
  private String buildName;
  private String platFormName;
  private TestPlan tpID;
  private Integer projectID;
  private Map<String, Integer> lastNodeChecked;

  public TestLinkPlugin(TestLinkAPI connection,
      String testProject,
      String testPlan,
      String buildName,
      String platFormName,
      ReportGeneratorPolicy policy) throws Exception {
    this.connection = connection;
    this.buildName = buildName;
    this.platFormName = platFormName;

    if (this.connection == null) {
      throw new Exception("No connection to testlink");
    }
    try {
      projectID = this.connection.getTestProjectByName(testProject).getId();
    } catch (Exception e) {
      throw new Exception("Cannot find projec : " + testProject, e);
    }
    if (projectID != null) {
      try {
        tpID = this.connection.getTestPlanByName(testPlan, testProject);
      } catch (Exception ignored) {

      }
      if ((policy.equals(ReportGeneratorPolicy.AUTO)
          || policy.equals(ReportGeneratorPolicy.MODERATE)) && tpID == null) {
        tpID = this.connection.createTestPlan(testPlan, testProject, null, true, true);
      } else if (tpID == null) {
        throw new Exception("Cannot find test plan " + testPlan);
      }

      boolean buildFound = false;
      Build[] builds = this.connection.getBuildsForTestPlan(tpID.getId());
      if (builds != null) {
        for (Build build : builds) {
          if (build.getName().equalsIgnoreCase(buildName)) {
            this.buildId = build.getId();
            buildFound = true;
            break;
          }
        }
      }

      if ((policy.equals(ReportGeneratorPolicy.AUTO)
          || policy.equals(ReportGeneratorPolicy.MODERATE)) && !buildFound) {
        Build build = this.connection.createBuild(tpID.getId(), buildName, null);
        buildId = build.getId();
      } else if (!buildFound) {
        throw new Exception(
            "Can't find Build " + buildName + " on " + testProject + " / testplan " + tpID.getName()
                + " . Builds avaiable " + Arrays.toString(builds));
      }
    }
  }

  private ReportTCResultResponse updateStatus(Integer tcExternalID,
      Integer tcInternalID,
      ExecutionStatus status,
      List<TestCaseStepResult> result,
      String notes,
      String user,
      Integer duration) {
    return this.connection.setTestCaseExecutionResult(tcExternalID,
        tcInternalID,
        tpID.getId(),
        status,
        result,
        buildId,
        buildName,
        notes,
        duration,
        true,
        null,
        null,
        null,
        null,
        true,
        user,
        null);
  }

  private boolean checkDuplicateState(TestCase previous, ScenarioData currentData) {
    boolean updated = false;
    if (previous != null && currentData != null) {
      if (previous.getSteps().size() == currentData.getTestCase().getSteps().size()) {
        //check test steps definition
        List<String> actual =
            previous.getSteps().stream().map(TestCaseStep::getActions).collect(Collectors.toList());
        List<String> expected = currentData.getTestCase()
            .getSteps()
            .stream()
            .map(TestCaseStep::getActions)
            .collect(Collectors.toList());
        if (!actual.equals(expected)) {
          //test step definition changed
          updated = updateTestCaseDataToTestLink(previous,
              currentData,
              CheckDuplicateTestStatus.STEPS_CHANGED);
        } else {
          //nothing changed update the result
          updated = updateTestCaseDataToTestLink(previous,
              currentData,
              CheckDuplicateTestStatus.EXISTING_TEST_CASE);
        }
      } else {
        //test step added
        updated = updateTestCaseDataToTestLink(previous,
            currentData,
            CheckDuplicateTestStatus.STEPS_ADDED);
      }
    }
    return updated;
  }

  public Integer linkTestCasesUsingTestLinkId(Map<String, ScenarioData> scenariosData) {
    AtomicReference<Integer> result = new AtomicReference<>(0);
    scenariosData.entrySet().parallelStream().forEach(entry -> {
      System.out.println(entry.getValue().getDetailToPrint());
      String fullExternalId = entry.getValue().getTestCase().getFullExternalId();
      if (fullExternalId != null && !fullExternalId.trim().isEmpty()) {
        try {
          ScenarioData currentData = entry.getValue();
          TestCase previous = this.connection.getTestCaseByExternalId(currentData.getTestCase()
              .getFullExternalId()
              .trim()
              .toUpperCase(), null);
          if (checkDuplicateState(previous, currentData)) {
            result.getAndSet(result.get() + 1);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("Scenario will be skipped");
      }
    });

    return result.get();
  }

  public Integer linkTestCaseWithAutomaticTestSuiteAssignment(Map<Integer, Map<String, ScenarioData>> grouped) {
    Integer result = 0;
    Map<Integer, Map<String, ScenarioData>> groupedFeature = new HashMap<>();
    List<String> lastNodeToCheck = new ArrayList<>();
    Integer currentId = null;
    this.lastNodeChecked = new HashMap<>();
    for (Integer dummy : grouped.keySet()) {
      Map<String, ScenarioData> scenariosData = grouped.get(dummy);
      for (String key : scenariosData.keySet()) {
        ScenarioData scenario = scenariosData.get(key);
        if (!lastNodeToCheck.equals(scenario.getTreeNode())) {
          currentId = getOrCreateTreeNodeSuiteId(scenario.getTreeNode());
        }
        if (currentId != null) {
          lastNodeToCheck = scenario.getTreeNode();
          Map<String, ScenarioData> scenarios =
              groupedFeature.getOrDefault(currentId, new HashMap<>());
          scenarios.put(key, scenario);
          groupedFeature.put(currentId, scenarios);
        }
      }
    }
    if (groupedFeature.isEmpty()) {
      groupedFeature.putAll(grouped);
    }
    for (Integer testSuiteId : groupedFeature.keySet()) {
      if (testSuiteId != null && testSuiteId > 0) {
        result += linkTestCases(testSuiteId, groupedFeature.get(testSuiteId));
      } else {
        result += linkTestCasesUsingTestLinkId(groupedFeature.get(testSuiteId));
      }
    }
    return result;
  }

  private Integer getOrCreateTreeNodeSuiteId(List<String> nodes) {
    Integer result = null;
    if (nodes != null && !nodes.isEmpty()) {
      if (this.lastNodeChecked.containsKey(nodes.toString())) {
        return this.lastNodeChecked.get(nodes.toString());
      }
      for (int i = 0; i < nodes.size(); i++) {
        String node = nodes.get(i);
        if (i == 0) {
          List<TestSuite> subNodes =
              Arrays.asList(this.connection.getFirstLevelTestSuitesForTestProject(projectID));
          Optional<TestSuite> suite =
              subNodes.stream().filter(e -> e.getName().equals(node)).findFirst();
          if (suite.isPresent()) {
            result = suite.get().getId();
          } else {
            System.out.println("Creating new node : " + node + " in " + nodes);
            try {
              TestSuite testSuite = this.connection.createTestSuite(projectID,
                  node,
                  null,
                  result,
                  null,
                  true,
                  ActionOnDuplicate.CREATE_NEW_VERSION);
              result = testSuite.getId();
            } catch (Exception ignored) {
              System.out.println("Fail to create node " + node + " from nodes " + nodes);
            }
          }
        } else {
          List<TestSuite> subNodes =
              Arrays.asList(this.connection.getTestSuitesForTestSuite(result));
          Optional<TestSuite> suite =
              subNodes.stream().filter(e -> e.getName().equals(node)).findFirst();
          if (suite.isPresent()) {
            result = suite.get().getId();
          } else {
            System.out.println("Creating new node : " + node + " in " + nodes);
            try {
              TestSuite testSuite = this.connection.createTestSuite(projectID,
                  node,
                  null,
                  result,
                  null,
                  true,
                  ActionOnDuplicate.CREATE_NEW_VERSION);
              result = testSuite.getId();
            } catch (Exception ignored) {
              System.out.println("Fail to create node " + node + " from nodes " + nodes);
            }
          }
        }
      }
      this.lastNodeChecked.put(nodes.toString(), result);
    }
    return result;
  }

  public Integer linkTestCases(Integer testSuiteID, Map<String, ScenarioData> scenariosData) {
    Integer result = 0;
    if (testSuiteID != null) {
      boolean isTestSuiteExists = false;
      try {
        List<Integer> ids = new ArrayList<>();
        ids.add(testSuiteID);
        isTestSuiteExists = this.connection.getTestSuiteByID(new ArrayList<>(ids)) != null;
      } catch (Exception ignored) {
        System.out.println("Cannot find the specified testSuite");
      }
      if (isTestSuiteExists) {
        List<TestCase> existingTestCases = new ArrayList<>();
        try {
          existingTestCases = Arrays.asList(this.connection.getTestCasesForTestSuite(testSuiteID,
              false,
              TestCaseDetails.FULL));
        } catch (Exception ignored) {
          System.out.println("Error when getting test cases for test suite " + testSuiteID);
        }
        if (existingTestCases.isEmpty()) {
          //No duplicate insert all test cases
          result += insertScenarioDataToTestLink(testSuiteID, scenariosData);
        } else {
          //May contain duplicate, check the test cases
          Set<String> existingTestCaseName =
              existingTestCases.stream().map(TestCase::getName).collect(Collectors.toSet());
          //insert non existing test case to test suite
          result += insertScenarioDataToTestLink(testSuiteID,
              scenariosData.entrySet()
                  .parallelStream()
                  .filter(e -> !existingTestCaseName.contains(e.getKey()))
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
          //compare the rest
          for (TestCase previous : existingTestCases) {
            ScenarioData currentData = scenariosData.getOrDefault(previous.getName(), null);
            if (currentData != null) {
              System.out.println(currentData.getDetailToPrint());
              if (checkDuplicateState(previous, currentData)) {
                result++;
              }
            }
          }
        }
      }
    } else {
      System.out.println("No testSuite ID defined, process will be terminated");
    }
    return result;
  }

  private Integer addTestCaseToTestPlan(TestCase testCase) {
    try {
      return this.connection.addTestCaseToTestPlan(projectID,
          tpID.getId(),
          testCase.getId(),
          testCase.getVersion(),
          null,
          null,
          null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private Boolean updateTestCaseResult(ScenarioData scenarioData, TestCase testCase) {
    Integer internal = testCase.getId();
    Integer external = testCase.getExternalId();
    System.out.println("Update result for " + scenarioData.getName());
    try {
      ReportTCResultResponse reportTCResponse = updateStatus(internal,
          external,
          scenarioData.getTestCase().getExecutionStatus(),
          parseTestCaseResult(scenarioData.getTestCase(), scenarioData.getIndexFail()),
          scenarioData.getReasonFail(),
          testCase.getAuthorLogin(),
          scenarioData.getDuration().intValue());
      System.out.println(reportTCResponse.getMessage());
      return reportTCResponse.getMessage().toLowerCase().contains("success");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private Boolean updateTestCaseDataToTestLink(TestCase previous,
      ScenarioData scenarioData,
      CheckDuplicateTestStatus duplicate) {
    switch (duplicate) {
      case EXISTING_TEST_CASE:
        System.out.println("Test case not changed, updating execution result.....");
        break;
      case STEPS_ADDED:
        System.out.println("Test case's steps added, updating to new version.....");
        previous.setVersion(previous.getVersion() + 1);
        break;
      case STEPS_CHANGED:
        System.out.println("Test case's steps changed, updating to new version.....");
        previous.setVersion(previous.getVersion() + 1);
        break;
      default:
        System.out.println("Test case not found, creating new test case.....");
        break;
    }

    try {
      Execution lastExecutionResult = this.connection.getLastExecutionResult(tpID.getId(),
          previous.getId(),
          previous.getFullExternalId(),
          null,
          null,
          null,
          null,
          null);
      if (lastExecutionResult != null) {
        this.connection.deleteExecution(lastExecutionResult.getId());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    TestCase testCase = this.connection.createTestCase(scenarioData.getTestCase().getName(),
        scenarioData.getTestCase().getTestSuiteId(),
        projectID,
        scenarioData.getTestCase().getAuthorLogin(),
        scenarioData.getTestCase().getSummary(),
        scenarioData.getTestCase().getSteps(),
        scenarioData.getTestCase().getPreconditions(),
        TestCaseStatus.FINAL,
        TestImportance.MEDIUM,
        ExecutionType.AUTOMATED,
        previous.getOrder(),
        previous.getInternalId(),
        true,
        ActionOnDuplicate.CREATE_NEW_VERSION);
    addTestCaseToTestPlan(testCase);

    return updateTestCaseResult(scenarioData, testCase);
  }

  private Integer insertScenarioDataToTestLink(Integer testSuiteID,
      Map<String, ScenarioData> scenariosData) {
    AtomicReference<Integer> result = new AtomicReference<>(0);
    if (scenariosData != null && !scenariosData.isEmpty()) {
      if (testSuiteID != null) {
        scenariosData.entrySet().parallelStream().forEach(entry -> {
          System.out.println(entry.getValue().getDetailToPrint());
          System.out.println("Test case not found, creating new test case.....");
          TestCase testCase = this.connection.createTestCase(entry.getValue().getName(),
              testSuiteID,
              projectID,
              entry.getValue().getTestCase().getAuthorLogin(),
              entry.getValue().getSummary(),
              entry.getValue().getTestCase().getSteps(),
              entry.getValue().getTestCase().getPreconditions(),
              TestCaseStatus.FINAL,
              TestImportance.MEDIUM,
              ExecutionType.AUTOMATED,
              1,
              null,
              true,
              ActionOnDuplicate.CREATE_NEW_VERSION);

          addTestCaseToTestPlan(testCase);
          if (updateTestCaseResult(entry.getValue(), testCase)) {
            result.getAndSet(result.get() + 1);
          }
        });
      }
    }
    return result.get();
  }

  private List<TestCaseStepResult> parseTestCaseResult(TestCase testCase, Integer indexFail) {
    List<TestCaseStep> steps = testCase.getSteps();
    List<TestCaseStepResult> results = new ArrayList<>();
    boolean fail = false;
    indexFail =
        indexFail == null ? 0 : indexFail >= steps.size() - 1 ? steps.size() - 1 : indexFail;
    for (int i = 0; i < steps.size(); i++) {
      TestCaseStepResult result = new TestCaseStepResult();
      result.setNumber(i + 1);
      if (testCase.getExecutionStatus().equals(ExecutionStatus.FAILED) && i == indexFail) {
        result.setResult(ExecutionStatus.FAILED);
        result.setNotes("Fail When Execute Test in step " + (i + 1) + " : " + testCase);
        fail = true;
      } else {
        result.setResult(ExecutionStatus.PASSED);
      }
      if (fail) {
        result.setResult(ExecutionStatus.BLOCKED);
      }
      results.add(result);
    }
    return results;
  }
}