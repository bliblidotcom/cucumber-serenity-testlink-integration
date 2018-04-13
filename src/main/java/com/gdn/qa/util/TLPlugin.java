package com.gdn.qa.util;

/**
 * Created by argo.triwidodo on 06/12/2016.
 */

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.*;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TLPlugin {
    //Enter your project API key here.
    private String DEVKEY = "1cc94be3bb2e1c3e069fa67f7e9a24e1";

    //Enter your Test Link URL here
    private String urlTestlink = "http://172.17.21.92/testlink/lib/api/xmlrpc/v1/xmlrpc.php";

    //Enter your Test Project Name here
    String testProject = "GOSEND";

    //Enter your Test Plan here
    String testPlan = "GOSEND TestPlan";

    //Enter your Test build here
    String build = "Gosend - UAT Build";

    //Enter Your test Platform Name
    String platFormName = "UAT";
    String testLinkID = null;
    TestLinkAPI api = null;
    //URL testLinkURL = null;

    Integer tcExternalID = null;
    Integer tcInternalID = null;
    TestCase testCases = null;
    TestPlan tpID = null;
    Integer projectID;
    Integer testSuiteID;
    String title;
    String preCondition = "";
    String summary = "";
    String pTitle = "";

    int buildId;
    int indexFail = 0;
    ArrayList<String[]> pSteps;

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPreCondition() {
        return preCondition;
    }

    public void setPreCondition(String preCondition) {
        this.preCondition = preCondition;
    }

    public void TLPluginInitialize(String tLinkID, ArrayList<String[]> steps, Integer testLinkSuiteID, String judul, String testLinkUrl,
                                   String testLinkDevKey, String testLinkProjectName, String testLinkPlanName, String testLinkBuildName,
                                   String testLinkPlatformName) {
        /// Initialize Parameter
        urlTestlink = testLinkUrl;
        DEVKEY = testLinkDevKey;
        testProject = testLinkProjectName;
        testPlan = testLinkPlanName;
        build = testLinkBuildName;
        platFormName = testLinkPlatformName;

        pSteps = steps;
        try {
            api = new TestLinkAPI(new URL(
                    urlTestlink),
                    DEVKEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("Estabilishing connection to Testlink..."+api.ping());
        tpID = api.getTestPlanByName(testPlan, testProject);
        /// Get project ID and TestSuite ID
        TestProject project = api.getTestProjectByName(testProject);
        projectID = project.getId();

        // Get BUild ID By Name
        Build[] builds = api.getBuildsForTestPlan(tpID.getId());
        for (Build build : builds)
            if (build.getName().equalsIgnoreCase(testLinkBuildName))
                buildId = build.getId();

        if (testLinkSuiteID != null) {
            testSuiteID = testLinkSuiteID;
        } else {
            testSuiteID = 414;
        }

        title = judul;
        if (!tLinkID.isEmpty()) {
            testCases = api.getTestCaseByExternalId(tLinkID, null);
        } else {
            createTestCase(steps);
        }
        tcExternalID = testCases.getId();
        tcInternalID = testCases.getInternalId();

    }

    public boolean updateTestcasePassed() {
        //  Integer testCaseID = api.getTestCaseIDByName("Pickup Order Gosend via MTA",testSuiteName,testProject,"");
        // api.reportTCResult(testCaseID);
        try {
            Execution lastExecution = api.getLastExecutionResult(tpID.getId(), testCases.getId(), null);
            if (lastExecution != null) {
                api.deleteExecution(lastExecution.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            api.addTestCaseToTestPlan(projectID, tpID.getId(), testCases.getId(), testCases.getVersion(), null, testCases.getOrder(), null);
            ReportTCResultResponse reportTCResponse = api.reportTCResult(tcExternalID, tcInternalID, tpID.getId(),
                    ExecutionStatus.PASSED, null, build, "Test Case ini dieksekusi Otomatis", null, null, null, platFormName, null, true);
            System.out.println(reportTCResponse.getMessage());
        }
        return true;
    }

    public boolean updateTestcaseFail(String notes) {
        //  Integer testCaseID = api.getTestCaseIDByName("Pickup Order Gosend via MTA",testSuiteName,testProject,"");
        // api.reportTCResult(testCaseID);
        System.out.println(tcExternalID);

        if (notes == "") {
            notes = "Fail When Execute Test in step " + (indexFail + 1) + " : " + pSteps.get(indexFail)[2];
        }
        try {
            Execution lastExecution = api.getLastExecutionResult(tpID.getId(), testCases.getId(), null);
            if (lastExecution != null) {
                api.deleteExecution(lastExecution.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            api.addTestCaseToTestPlan(projectID, tpID.getId(), testCases.getId(), testCases.getVersion(), null, null, null);
            ReportTCResultResponse reportTCResponse = api.reportTCResult(tcExternalID, tcInternalID, tpID.getId(),
                    ExecutionStatus.FAILED, null, build, notes, null, null, null, platFormName, null, true);
            System.out.println(reportTCResponse.getMessage());
        }
        return true;
    }

    public void createTestCase(ArrayList<String[]> steps) {

        //// Create Test Steps
        indexFail = 0;
        List<TestCaseStep> testCaseSteps = new ArrayList<TestCaseStep>();
        for (int i = 0; i < steps.size(); i++) {
            TestCaseStep testCaseStep = new TestCaseStep();
/*            System.out.println(steps.get(i)[0]);
            System.out.println(steps.get(i)[1]);*/
            testCaseStep.setActions(steps.get(i)[0]); /// Set Actions
            testCaseStep.setExecutionType(ExecutionType.AUTOMATED); /// Set Type Action
            if (!Boolean.parseBoolean(steps.get(i)[1])) {
//                testCaseStep.setExpectedResults("Last Result Run : " + steps.get(i)[2]);
                indexFail = i;
            }
            testCaseStep.setActive(true);
            testCaseStep.setId(i + 1);
            testCaseStep.setNumber(i + 1);
            testCaseSteps.add(i, testCaseStep); // Add To Steps
        }

        if (summary == "") {
            summary = title;
        }
        if (pTitle != "") {
            title = pTitle;
        }
        List<Integer> a = new ArrayList<>();
        a.add(testSuiteID);
        TestSuite[] testSuiteTemp = api.getTestSuiteByID(a);
        // Checking duplucate
        testCases = searchTestStepDuplicate(title, testProject, testSuiteTemp[0].getName(), testCaseSteps);
        if (testCases == null) {
            try {
                testCases = api.createTestCase(title, testSuiteID, projectID, "automation-test", summary,
                        testCaseSteps, preCondition, TestCaseStatus.FINAL, TestImportance.MEDIUM, ExecutionType.AUTOMATED, 1,
                        1, true, ActionOnDuplicate.CREATE_NEW_VERSION);
                api.addTestCaseToTestPlan(projectID, tpID.getId(), testCases.getId(), testCases.getVersion(), null, testCases.getOrder(), null);
            } catch (TestLinkAPIException e) {
                e.printStackTrace();
            }
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
    public TestCase searchTestStepDuplicate(String title, String testProjectName, String testSuiteName, List<TestCaseStep> tsInputted) {
        // 1 Get data from server
        Integer idtcServer;
        try {
            idtcServer = api.getTestCaseIDByName(title, testSuiteName, testProjectName, null);
        } catch (Exception e) {
            System.out.println("Test Case tidak ditemukan , akan membuat test case baru.....");
            return null;
        }

        TestCase tcServer = api.getTestCase(idtcServer, null, null);
        // 2 Compare data
        List<TestCaseStep> tsServers = tcServer.getSteps();
        //// Compare Title
        if (!title.equals(tcServer.getName())) { // Jika tidak sama langsung return false
            System.out.println("Test Case tidak ditemukan , akan membuat test case baru.....");
            return null;
        } else { // Jika sama lanjutkan pengecekan
            if (tsServers.size() != tsInputted.size()) { // jika ukuran tidak sama langsung lewati
                System.out.println("Test Step Ditambahin.");
                return null;
            } else { // jika Ukuran sama lanjutkan pengecekan
                for (int i = 0; i < tsInputted.size(); i++) {
                    if (!tsServers.get(i).getActions().equals(tsInputted.get(i).getActions())) {
                        System.out.println("Test Step berubah create new.");
                        return null;
                    }
                }
            }
        }
        System.out.println("Test Step Tidak berubah , Mengembalikan Existing....");
        return tcServer;
    }
}

