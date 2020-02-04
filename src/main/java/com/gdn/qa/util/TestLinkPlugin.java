package com.gdn.qa.util;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.*;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import com.gdn.qa.util.model.CheckDuplicateTestStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.gdn.qa.util.constant.TestlinkConstant.MAX_LENGTH_TESTLINK;

/**
 * Created by argo.triwidodo on 06/12/2016.
 * Updated by yunaz.ramadhan on 1/30/2020
 */
public class TestLinkPlugin {
  private TestLinkAPI connection;
  private String testProject;
  private String testPlan;
  private Integer buildId;
  private String buildName;
  private String platFormName;
  private Integer tcExternalID;
  private Integer tcInternalID;
  private TestCase testCases;
  private TestPlan tpID;
  private Integer projectID;
  private Integer testSuiteID;
  private String title;
  private String preCondition;
  private String summary;
  private String reasonFail;
  private CheckDuplicateTestStatus duplicateTestStatus;

  public TestLinkPlugin(TestLinkAPI connection,
      String testProject,
      String testPlan,
      String buildName,
      String platFormName) throws Exception {
    this.connection = connection;
    this.testProject = testProject;
    this.testPlan = testPlan;
    this.buildName = buildName;
    this.platFormName = platFormName;

    if (this.connection == null) {
      throw new Exception("No connection to testlink");
    }
    tpID = this.connection.getTestPlanByName(testPlan, testProject);
    projectID = this.connection.getTestProjectByName(testProject).getId();
    Build[] builds = this.connection.getBuildsForTestPlan(tpID.getId());
    if (builds == null || builds.length == 0) {
      throw new Exception("Can't find Build on " + testProject + " / testplan " + tpID.getName()
          + " Please Create build first. ");
    }
    boolean buildFound = false;
    for (Build build : builds) {
      if (build.getName().equalsIgnoreCase(buildName)) {
        this.buildId = build.getId();
        buildFound = true;
        break;
      }
    }

    if (!buildFound) {
      throw new Exception(
          "Can't find Build " + buildName + " on " + testProject + " / testplan " + tpID.getName()
              + " . Builds avaiable " + Arrays.toString(builds));
    }
  }

  public String getTestPlan() {
    return testPlan;
  }

  public String getTitle() {
    return title;
  }

  public TestLinkPlugin setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getPreCondition() {
    return preCondition;
  }

  public TestLinkPlugin setPreCondition(String preCondition) {
    this.preCondition = preCondition;
    return this;
  }

  public String getSummary() {
    return summary;
  }

  public TestLinkPlugin setSummary(String summary) {
    this.summary = summary;
    return this;
  }

  public Integer getTestSuiteID() {
    return testSuiteID;
  }

  public TestLinkPlugin setTestSuiteID(int testSuiteID) {
    this.testSuiteID = testSuiteID;
    return this;
  }

  public void linkTestCases(String testLinkID,
      List<String[]> steps,
      boolean status,
      String notes,
      Integer duration,
      String detailToPrint) throws Exception {
    if(testLinkID != null || testLinkID.trim().equals("")) {
      testCases = this.connection.getTestCaseByExternalId(testLinkID, null);
    }else {
      tryToUpdateTestCase(steps, status);
    }
    tcExternalID = testCases.getId();
    tcInternalID = testCases.getInternalId();

    this.connection.addTestCaseToTestPlan(projectID,
        tpID.getId(),
        testCases.getId(),
        testCases.getVersion(),
        null,
        testCases.getOrder(),
        1);
    String finalNotes;
    if (status) {
      finalNotes = notes.trim().isEmpty() ? "This test case was executed using automation" : notes;
    } else {
      finalNotes = notes.trim().isEmpty() ? this.reasonFail : notes;
    }
    List<TestCaseStepResult> stepResults = parseTestCaseResult(steps);
    ReportTCResultResponse reportTCResponse = status ?
        updateStatus(ExecutionStatus.PASSED, stepResults, finalNotes, duration) :
        updateStatus(ExecutionStatus.FAILED, stepResults, finalNotes, duration);
    System.out.print(detailToPrint);
    switch (duplicateTestStatus) {
      case EXISTING_TEST_CASE:
        System.out.println("Test case not changed, updating execution result.....");
        break;
      case STEPS_ADDED:
        System.out.println("Test case's steps added, updating to new version.....");
        break;
      case STEPS_CHANGED:
        System.out.println("Test case's steps changed, updating to new version.....");
        break;
      default:
        System.out.println("Test case not found, creating new test case.....");
        break;
    }
    System.out.println(reportTCResponse.getMessage().equalsIgnoreCase("Success!") ?
        "Done!" :
        "Failed!");
  }

  private ReportTCResultResponse updateStatus(ExecutionStatus status,
      List<TestCaseStepResult> result,
      String notes,
      Integer duration) {
    return this.connection.reportTCResult(tcExternalID,
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
        platFormName,
        null,
        true,
        "automation-test",
        null);
  }

  private List<TestCaseStepResult> parseTestCaseResult(List<String[]> steps) {
    List<TestCaseStepResult> results = new ArrayList<>();
    boolean fail = false;
    for (int i = 0; i < steps.size(); i++) {
      TestCaseStepResult result = new TestCaseStepResult();
      result.setNumber(i + 1);
      if (!Boolean.parseBoolean(steps.get(i)[1])) {
        result.setResult(ExecutionStatus.FAILED);
        result.setNotes("Fail When Execute Test in step " + (i + 1) + " : " + steps.get(i)[2]);
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

  private void tryToUpdateTestCase(List<String[]> steps, boolean status) throws Exception {
    //// Create Test Steps
    List<TestCaseStep> testCaseSteps = new ArrayList<>();
    for (int i = 0; i < steps.size(); i++) {
      if (!Boolean.parseBoolean(steps.get(i)[1])) {
        this.reasonFail = "Fail When Execute Test in step " + (i + 1) + " : " + steps.get(i)[2];
      }
      TestCaseStep testCaseStep = new TestCaseStep();
      testCaseStep.setActions(steps.get(i)[0]);
      testCaseStep.setExecutionType(ExecutionType.AUTOMATED);
      testCaseStep.setActive(true);
      testCaseStep.setId(i + 1);
      testCaseStep.setNumber(i + 1);
      testCaseStep.setExpectedResults("Passed");
      testCaseSteps.add(i, testCaseStep); // Add To Steps
    }

    if (summary == null || summary.equals("")) {
      summary = title;
    }

    List<Integer> a = new ArrayList<>();
    a.add(testSuiteID);
    TestSuite[] testSuiteTemp = this.connection.getTestSuiteByID(a);
    // Checking duplucate
    testCases = getTestCaseChanges(title, testProject, testSuiteTemp[0].getName(), testCaseSteps);
    if (duplicateTestStatus.equals(CheckDuplicateTestStatus.NEW_TEST_CASE)) {
      testCases = this.connection.createTestCase(title,
          testSuiteID,
          projectID,
          "automation-test",
          summary,
          testCaseSteps,
          preCondition,
          TestCaseStatus.FINAL,
          TestImportance.MEDIUM,
          ExecutionType.AUTOMATED,
          1,
          1,
          true,
          ActionOnDuplicate.CREATE_NEW_VERSION);
    } else if (duplicateTestStatus.equals(CheckDuplicateTestStatus.STEPS_ADDED)
        || duplicateTestStatus.equals(CheckDuplicateTestStatus.STEPS_CHANGED)
        || testCases.getExecutionStatus().equals(ExecutionStatus.NOT_RUN)) {
      testCases.setSteps(testCaseSteps);
      testCases.setExecutionType(ExecutionType.AUTOMATED);
      testCases.setTestImportance(TestImportance.MEDIUM);
      testCases.setPreconditions(preCondition);
      testCases.setSummary(summary);
      testCases.setTestCaseStatus(TestCaseStatus.FINAL);
      testCases.setExecutionStatus(status ? ExecutionStatus.PASSED : ExecutionStatus.FAILED);
      this.connection.updateTestCase(testCases);
    }
  }

  /**
   * Digunakan untuk mengecek apakah ada testCase yang duplicate
   *
   * @param title           title dari test casenya
   * @param testProjectName project name yang digunakan
   * @param testSuiteName   test suite name yang digunakan
   * @param tsInputted      test step yang diinputkan oleh usser
   * @return bila duplicate akan return object testCase , bila belum ada akan mengembalikan null
   */
  public TestCase getTestCaseChanges(String title,
      String testProjectName,
      String testSuiteName,
      List<TestCaseStep> tsInputted) throws Exception {
    // 1 Get data from server
    Integer idtcServer = null;
    // check caracter length
    if (title.length() > MAX_LENGTH_TESTLINK)
      throw new Exception("Please Shorten your title , " + title + " it's should have maximum "
          + MAX_LENGTH_TESTLINK + " Character");
    try {
      idtcServer = this.connection.getTestCaseIDByName(title, testSuiteName, testProjectName, "");
    } catch (Exception e) {
      // perlu buat pengecekan apabila errornya premature file
      if (e.getMessage().toLowerCase().contains("premature end of file")) {
        throw new Exception("Premature file , Testcase " + title + " will be skipped");
      } else {
        duplicateTestStatus = CheckDuplicateTestStatus.NEW_TEST_CASE;
        return null;
      }
    }

    if (idtcServer != null) {
      TestCase tcServer = this.connection.getTestCase(idtcServer, null, null);
      // Compare data
      List<TestCaseStep> tsServers = tcServer.getSteps();
      //// Compare Title
      if (!title.substring(0, (tcServer.getName().length()))
          .equals(tcServer.getName())) { // Jika tidak sama langsung return false
        duplicateTestStatus = CheckDuplicateTestStatus.NEW_TEST_CASE;
        return null;
      } else { // Jika sama lanjutkan pengecekan
        if (tsServers.size() != tsInputted.size()) { // jika ukuran tidak sama langsung lewati
          duplicateTestStatus = CheckDuplicateTestStatus.STEPS_ADDED;
          return tcServer;
        } else { // jika Ukuran sama lanjutkan pengecekan
          List<String> actual =
              tsServers.stream().map(TestCaseStep::getActions).collect(Collectors.toList());
          List<String> expected =
              tsInputted.stream().map(TestCaseStep::getActions).collect(Collectors.toList());
          if (!actual.equals(expected)) {
            duplicateTestStatus = CheckDuplicateTestStatus.STEPS_CHANGED;
            return tcServer;
          }
        }
      }
      duplicateTestStatus = CheckDuplicateTestStatus.EXISTING_TEST_CASE;
      return tcServer;
    } else {
      throw new Exception("Cannot check test case, Testcase " + title + " will be skipped");
    }
  }
}
