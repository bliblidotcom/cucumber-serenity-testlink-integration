package com.blibli.oss.qa.util;

/**
 * Created by argo.triwidodo on 06/12/2016.
 */

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.*;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import com.blibli.oss.qa.util.service.CustomTestlinkService;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.blibli.oss.qa.util.constant.TestlinkConstant.MAX_LENGTH_TESTLINK;


public final class TLPlugin {
  private static TLPlugin INSTANCE;
  //Enter your project API key here.
  private String DEVKEY = null;

  //Enter your Test Link URL here
  private String urlTestlink = null;

  //Enter your Test Project Name here
  private String testProject = null;

  //Enter your Test Plan here
  private String testPlan = null;

  //Enter your Test build here
  private String buildName = null;

  //Enter Your test Platform Name
  private String platFormName = null;
  private String testLinkID = null;
  private TestLinkAPI api = null;
  private CustomTestlinkService customTestlinkService = null;
  //URL testLinkURL = null;

  private Integer tcExternalID = null;
  private Integer tcInternalID = null;
  private TestCase testCases = null;
  private TestPlan tpID = null;
  private Integer projectID;
  private Integer testSuiteID;
  private String title;
  private String preCondition = "";
  private String summary = "";
  private String pTitle = "";

  private Integer buildId;
  private Integer indexFail = 0;
  private ArrayList<String[]> pSteps;

  private TLPlugin() {

  }

  public static TLPlugin getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TLPlugin();
    }

    return INSTANCE;
  }

  public String getpTitle() {
    return pTitle;
  }

  public void setpTitle(String pTitle) {
    getInstance().pTitle = pTitle;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    getInstance().summary = summary;
  }

  public String getPreCondition() {
    return preCondition;
  }

  public void setPreCondition(String preCondition) {
    getInstance().preCondition = preCondition;
  }

  public void initializeTLPlugin(String teslinkUrl,
      String testlinkDevKey,
      String testProjectName,
      String testPlanName,
      String testBuildName,
      String platform) throws Exception {
    urlTestlink = teslinkUrl;
    DEVKEY = testlinkDevKey;
    testProject = testProjectName;
    testPlan = testPlanName;
    buildName = testBuildName;
    platFormName = platform;
    api = new TestLinkAPI(new URL(urlTestlink), DEVKEY);
    customTestlinkService = new CustomTestlinkService(api);
    System.out.print("Establishing connection to Testlink...");
    if (api.ping().equalsIgnoreCase("Hello!")) {
      System.out.println("Done!");
    }
    tpID = api.getTestPlanByName(testPlan, testProject);
    /// Get project ID and TestSuite ID
    TestProject project = api.getTestProjectByName(testProject);
    projectID = project.getId();

    // Get BUild ID By Name
    Build[] builds = api.getBuildsForTestPlan(tpID.getId());
    if (builds.length == 0) {
      throw new Exception("Can't find Build on " + testProject + " / testplan " + tpID.getName()
          + " Please Create build first. ");
    }
    boolean buildFound = false;
    for (Build build : builds) {
      if (build.getName().equalsIgnoreCase(buildName)) {
        buildId = build.getId();
        buildFound = true;
      }
    }

    if (!buildFound) {
      throw new Exception(
          "Can't find Build " + buildName + " on " + testProject + " / testplan " + tpID.getName()
              + " . Builds avaiable " + Arrays.toString(builds));
    }
  }

  public void setJudul(String judul) {
    title = judul;
  }

  public void setTestSuiteID(int testSuiteID) {
    if (testSuiteID < 0) {
      getInstance().testSuiteID = 414;
    } else {
      getInstance().testSuiteID = testSuiteID;
    }
  }

  public TestCase linkTestCases(String testLinkID, ArrayList<String[]> steps) throws Exception {
    if (!testLinkID.isEmpty()) {
      testCases = api.getTestCaseByExternalId(testLinkID, null);
    } else {
      createTestCase(steps);
    }
    tcExternalID = testCases.getId();
    tcInternalID = testCases.getInternalId();
    return testCases;
  }

  public boolean updateTestcasePassed() {
    //  Integer testCaseID = api.getTestCaseIDByName("Pickup Order Gosend via MTA",testSuiteName,testProject,"");
    // api.reportTCResult(testCaseID);
    try {
      //            Execution lastExecution = api.getLastExecutionResult(tpID.getId(), testCases.getId(), null);
      Execution lastExecution = customTestlinkService.getLastExecutionResultByBuild(tpID.getId(),
          testCases.getId(),
          null,
          buildId);
      if (lastExecution != null) {
        System.out.println("Found Last Execution Status");
        if (lastExecution.getBuildId().equals(buildId)) {
          api.deleteExecution(lastExecution.getId());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      api.addTestCaseToTestPlan(projectID,
          tpID.getId(),
          testCases.getId(),
          testCases.getVersion(),
          null,
          testCases.getOrder(),
          null);
      ReportTCResultResponse reportTCResponse =
          updateStatus(ExecutionStatus.PASSED, "This test case was executed using automation");

      System.out.println(reportTCResponse.getMessage().equalsIgnoreCase("Success!") ?
          "Done!" :
          "Failed!");
    }
    return true;
  }

  public boolean updateTestcaseFail(String notes) {
    //  Integer testCaseID = api.getTestCaseIDByName("Pickup Order Gosend via MTA",testSuiteName,testProject,"");
    // api.reportTCResult(testCaseID);
    //        System.out.println(tcExternalID);

    if (notes.trim().equals("")) {
      notes =
          "Fail When Execute Test in step " + (indexFail + 1) + " : " + pSteps.get(indexFail)[2];
    }
    try {
      //            Execution lastExecution = api.getLastExecutionResult(tpID.getId(), testCases.getId(), null);
      Execution lastExecution = customTestlinkService.getLastExecutionResultByBuild(tpID.getId(),
          testCases.getId(),
          null,
          buildId);
      if (lastExecution != null) {
        System.out.println("Found Last Execution Status");
        System.out.println("Build ID : \t" + lastExecution.getBuildId());
        if (lastExecution.getBuildId().equals(buildId)) {
          api.deleteExecution(lastExecution.getId());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      api.addTestCaseToTestPlan(projectID,
          tpID.getId(),
          testCases.getId(),
          testCases.getVersion(),
          null,
          null,
          null);
      ReportTCResultResponse reportTCResponse = updateStatus(ExecutionStatus.FAILED, notes);

      System.out.println(reportTCResponse.getMessage().equalsIgnoreCase("Success!") ?
          "Done!" :
          "Failed!");
    }
    return true;
  }

  private ReportTCResultResponse updateStatus(ExecutionStatus status, String notes) {
    return api.reportTCResult(tcExternalID,
        tcInternalID,
        tpID.getId(),
        status,
        null,
        null,
        buildName,
        notes,
        null,
        null,
        null,
        null,
        platFormName,
        null,
        true,
        null,
        null);
  }

  public void createTestCase(ArrayList<String[]> steps) throws Exception {

    //// Create Test Steps
    indexFail = 0;
    List<TestCaseStep> testCaseSteps = new ArrayList<>();
    for (int i = 0; i < steps.size(); i++) {
      TestCaseStep testCaseStep = new TestCaseStep();
      testCaseStep.setActions(steps.get(i)[0]); /// Set Actions
      testCaseStep.setExecutionType(ExecutionType.AUTOMATED); /// Set Type Action
      if (!Boolean.parseBoolean(steps.get(i)[1])) {
        indexFail = i;
      }
      testCaseStep.setActive(true);
      testCaseStep.setId(i + 1);
      testCaseStep.setNumber(i + 1);
      testCaseSteps.add(i, testCaseStep); // Add To Steps
    }

    if (summary.equals("")) {
      summary = title;
    }
    if (!pTitle.equals("")) {
      title = pTitle;
    }
    List<Integer> a = new ArrayList<>();
    a.add(testSuiteID);
    TestSuite[] testSuiteTemp = api.getTestSuiteByID(a);
    // Checking duplicate
    testCases =
        searchTestStepDuplicate(title, testProject, testSuiteTemp[0].getName(), testCaseSteps);
    if (testCases == null) {
      testCases = api.createTestCase(title,
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
      api.addTestCaseToTestPlan(projectID,
          tpID.getId(),
          testCases.getId(),
          testCases.getVersion(),
          null,
          testCases.getOrder(),
          null);
    }
  }

  public TestCase searchTestStepDuplicate(String title,
      String testProjectName,
      String testSuiteName,
      List<TestCaseStep> tsInputted) throws Exception {
    // 1 Get data from server
    Integer idtcServer;
    // check caracter length
    if (title.length() > MAX_LENGTH_TESTLINK)
      throw new Exception("Please Shorten your title , " + title + " it's should have maximum "
          + MAX_LENGTH_TESTLINK + " Character");
    try {
      idtcServer = api.getTestCaseIDByName(title, testSuiteName, testProjectName, "");

    } catch (Exception e) {
      System.out.println("Test Case tidak ditemukan , akan membuat test case baru.....");
      //      System.out.println(e.getMessage());
      // perlu buat pengecekan apabila errornya premature file
      if (e.getMessage().toLowerCase().contains("premature end of file")) {
        throw new Exception("Premature file , Testcase " + title + " will be skipped");
      }
      return null;
    }

    TestCase tcServer = api.getTestCase(idtcServer, null, null);
    // 2 Compare data
    List<TestCaseStep> tsServers = tcServer.getSteps();
    //// Compare Title
    //        if (!title.equals(tcServer.getName())) { // Jika tidak sama langsung return false
    if (!title.substring(0, (tcServer.getName().length()))
        .equals(tcServer.getName())) { // Jika tidak sama langsung return false
      System.out.println("Test case not found, creating new testcase.....");
      return null;
    } else { // Jika sama lanjutkan pengecekan
      if (tsServers.size() != tsInputted.size()) { // jika ukuran tidak sama langsung lewati
        System.out.println("Test step added");
        return null;
      } else { // jika Ukuran sama lanjutkan pengecekan
        List<String> actual =
            tsServers.stream().map(TestCaseStep::getActions).collect(Collectors.toList());
        List<String> expected =
            tsInputted.stream().map(TestCaseStep::getActions).collect(Collectors.toList());
        if (!actual.equals(expected)) {
          System.out.println("Test step changed. Create new version...");
          return null;
        }
      }
    }
    System.out.print("Test step not changed, Updating the result....");
    return tcServer;
  }
}

