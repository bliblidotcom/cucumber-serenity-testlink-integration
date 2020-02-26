package com.gdn.qa.util;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.*;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.qa.util.model.cucumber.CucumberModel;
import com.gdn.qa.util.model.cucumber.Rows;
import com.gdn.qa.util.model.cucumber.Step;
import com.gdn.qa.util.model.earlgrey.EarlGreyModel;
import com.gdn.qa.util.model.earlgrey.Subtest2;
import com.gdn.qa.util.model.earlgrey.Subtest3;
import com.gdn.qa.util.model.earlgrey.TestResult;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestResultReader {
  private static String DEFAULT_ID_TEST_LINK = "414";
  //Enter your Test Project Name here
  String testProject = "GOSEND";
  //Enter your Test Plan here
  String testPlan = "GOSEND TestPlan";
  //Enter your Test build here
  String build = "Gosend - UAT Build";
  //Enter Your test Platform Name
  String platFormName = "UAT";
  ScenarioData data;
  Boolean tempResult;
  AtomicReference<Integer> passed = new AtomicReference<>(0);
  AtomicReference<Integer> failed = new AtomicReference<>(0);
  //Enter your project API key here.
  private String DEVKEY = "1cc94be3bb2e1c3e069fa67f7e9a24e1";
  //Enter your Test Link URL here
  private String urlTestlink = "http://172.17.21.92/testlink/lib/api/xmlrpc/v1/xmlrpc.php";
  private TestLinkAPI connection;

  public void initialize(String testLinkURL,
      String testLinkDevKey,
      String testLinkProjectName,
      String testLinkPlanName,
      String testLinkBuildName,
      String testLinkPlatFormName) throws Exception {
    urlTestlink = testLinkURL;
    DEVKEY = testLinkDevKey;
    testProject = testLinkProjectName;
    testPlan = testLinkPlanName;
    build = testLinkBuildName;
    platFormName = testLinkPlatFormName;
    connection = connectToTestlink();
  }

  public void readResult() {
    //        System.out.println(System.getProperty("user.dir"));
    //        System.out.println(pathTourl.getFile());
    //File dirXml = new File(pathTourl.getFile());
    File dirXml = new File(System.getProperty("user.dir") + "/target/classes/");
    /// Get Parent Directory
    String parentDirectory = dirXml.getParent();
    File folder = new File(parentDirectory + "/jbehave");
    File[] listOfFiles = folder.listFiles();

    if (listOfFiles != null) {
      for (File listOfFile : listOfFiles) {
        if (listOfFile.isFile()) {
          String filePath = folder.getPath() + "/" + listOfFile.getName();
          System.out.println("File " + filePath);
          if (filePath.contains(".xml") && !filePath.contains("AfterStories") && !filePath.contains(
              "BeforeStories")) {
            readXML(filePath);
          }
        }
      }
    }
  }

  // Run With Jbehave
  public Map<String, String> readXML(String pathFile) {
    Map<String, String> result = new HashMap<>();

    try {

      File fXmlFile = new File(pathFile);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);
      ArrayList<String[]> testCaseSteps = new ArrayList<>();
      String title = "";
      Integer testSuiteID = null;

      //optional, but recommended
      //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
      doc.getDocumentElement().normalize();

      System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
      //// Read Meta Description
      NodeList metaList = doc.getElementsByTagName("meta");
      for (int cListProperty = 0; cListProperty < metaList.getLength(); cListProperty++) {

        System.out.println();
        Node nMetaList = metaList.item(cListProperty);

        /// Check If There are ELEMENT_NODE
        if (nMetaList.getNodeType() == Node.ELEMENT_NODE) {
          Element meta = (Element) nMetaList;
          NodeList property = meta.getElementsByTagName("property");
          for (int cProperty = 0; cProperty < property.getLength(); cProperty++) {
            ////// Get TestLink ID
            Element vProperty = (Element) property.item(cProperty);
            if (vProperty.getAttribute("name").equalsIgnoreCase("TestLinkId")) {
              result.put("testLinkID", vProperty.getAttribute("value"));
            }
            // Get TestSuite ID << Wajib
            if (vProperty.getAttribute("name").equalsIgnoreCase("TestSuiteID")) {
              result.put("TestSuiteID", vProperty.getAttribute("value"));
            }
            // Get preCondit <<
            if (vProperty.getAttribute("name").equalsIgnoreCase("PreConditions")) {
              result.put("PreConditions", vProperty.getAttribute("value"));
            }

            // Get getTitle if exist
            if (vProperty.getAttribute("name").equalsIgnoreCase("Title")) {
              result.put("Title", vProperty.getAttribute("value"));
            }

            // Get Summary if exist
            if (vProperty.getAttribute("name").equalsIgnoreCase("Summary")) {
              result.put("Summary", vProperty.getAttribute("value"));
            }

          }
        }
      }

      ///// Cek apabila tidak ada TestSuiteID
      if (result.get("TestSuiteID") == null) {
        System.out.println("Testlink Suite id is Null , No action Needed");
      }

      /// Get All List scenario
      NodeList nList = doc.getElementsByTagName("scenario");
      boolean passed = false;
      int totalSteps = 0;
      for (int temp = 0; temp < nList.getLength(); temp++) {

        Node nNode = nList.item(temp);

        System.out.println("\nCurrent Element :" + nNode.getNodeName());

        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

          Element eElement = (Element) nNode;
          //// Get All Steps
          NodeList steps = eElement.getElementsByTagName("step");
          ///// Get Title
          if (!eElement.getAttribute("title").isEmpty() && title.isEmpty()) {
            title = eElement.getAttribute("title");
          }
          ///check length Step
          System.out.println(steps.getLength());
          /// Get All Steps
          for (int cSteps = 0; cSteps < steps.getLength(); cSteps++) {


            ///// get steps
            Element vSteps = (Element) steps.item(cSteps);

            /// check if outcome is empty
            if (vSteps.getAttribute("outcome").isEmpty()) {
              continue;
            }

            //// Get Steps Definition
            String stepsDefinition = vSteps.getTextContent();
            String reasonFail = "";

            ///// Get Status Steps
            if (vSteps.getAttribute("outcome").equalsIgnoreCase("successful")) {
              //// Do Something when success
              passed = true;
            } else {
              //// Do Something When Fail
              /// Clean data when fail
              NodeList failures = vSteps.getElementsByTagName("failure");
              Element failure = null;
              if (failures.getLength() > 0) {
                failure = (Element) failures.item(0);
                reasonFail = String.format("This Test Case Failed on step %s, Log:\n%s",
                    cSteps + 1,
                    failure.getTextContent());
              }
              stepsDefinition = stepsDefinition.replace(reasonFail, "");
              passed = false;
            }
            String[] stepsResult = {stepsDefinition, Boolean.toString(passed), reasonFail};
            testCaseSteps.add(totalSteps++, stepsResult);
          }

          /// Digunakan Untuk Pengecekan di semua test Case Steps
          for (String[] testCaseStep : testCaseSteps) {
            if (testCaseStep[1].equalsIgnoreCase(Boolean.toString(false))) {
              passed = false;
              break;
            }
          }

        }
      }

      /// Add Test Suite ID
      TestLinkPlugin plugin =
          new TestLinkPlugin(connection, testProject, testPlan, build, platFormName);
      plugin.setSummary(result.getOrDefault("Summary", ""))
          .setPreCondition(result.getOrDefault("PreConditions", ""))
          .setTitle(result.get("Title"))
          .setTestSuiteID(Integer.parseInt(result.getOrDefault("TestSuiteID", "0")));

      plugin.linkTestCases(result.get("testLinkID"), testCaseSteps, passed, "", null, "");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Run With Cucumber
   *
   * @param cucumberJsonPath
   * @throws Exception
   */
  public void readWithCucumber(String cucumberJsonPath) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    if (cucumberJsonPath.equals("") || cucumberJsonPath.isEmpty()) {
      cucumberJsonPath = System.getProperty("user.dir") + "/target/destination/cucumber.json";
      //            cucumberJsonPath = System.getProperty("user.dir") + "/main/resource/cucumber3.json";
    }
    File cucumberFile = new File(cucumberJsonPath);
    if (!cucumberFile.exists()) {
      System.out.println("Cucumber Path : " + cucumberJsonPath);
      throw new Exception(
          "Cucumber Json is not exist can't Post To Testlink , is your setup is right ? ");
    }
    List<CucumberModel> features =
        Arrays.asList(objectMapper.readValue(cucumberFile, CucumberModel[].class));

    // Read Read Each Test Feature
    groupingScenarios(features);
  }

  private void groupingScenarios(List<CucumberModel> features) {
    //    group scenario per test suite id set in feature or scenario
    //    if no test suite id is set then the scenario will be ignored

    Map<Integer, Map<String, ScenarioData>> groupedFeature = new ConcurrentHashMap<>();
    AtomicReference<Integer> totalPassed = new AtomicReference<>(0);
    AtomicReference<Integer> totalFailed = new AtomicReference<>(0);
    AtomicReference<Integer> linked = new AtomicReference<>(0);

    features.parallelStream().forEach(feature -> {
      System.out.println("Populating scenarios from feature : " + feature.getName());
      String summary =
          String.format("Feature : %s, %s", feature.getName(), feature.getDescription());
      List<String[]> steps = new ArrayList<>();
      feature.getElements().parallelStream().forEach(element -> {
        ScenarioData scenarioData = new ScenarioData();
        if (element.getType().equalsIgnoreCase("background")) {
          steps.addAll(0, readSteps(element.getSteps()));
        } else if (element.getType().equalsIgnoreCase("scenario")) {
          steps.addAll(readSteps(element.getSteps()));
          Integer testSuiteId = TagsReader.getTestSuiteIdFromTags(element.getTags());
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
          if (status) {
            totalPassed.getAndSet(totalPassed.get() + 1);
          } else {
            totalFailed.getAndSet(totalFailed.get() + 1);
          }
          if (testSuiteId != null) {
            Double duration = element.getSteps()
                .stream()
                .mapToDouble(e -> (int) (e.getResult().getDuration().getTime() / 1000000000.0))
                .sum();
            scenarioData.setReasonFail(reason);
            scenarioData.setIndexFail(indexFail);
            scenarioData.setPassed(status);
            scenarioData.setName(element.getName());
            scenarioData.setSummary(summary);
            scenarioData.setTestCase(constructTestCase(testSuiteId,
                null,
                element.getName(),
                summary,
                status,
                steps));
            scenarioData.setDuration(duration);
            scenarioData.setFeatureName(feature.getName());
            scenarioData.setDetailToPrint(printDetail(null,
                feature.getName(),
                element.getName(),
                status,
                scenarioData.getReasonFail(),
                testSuiteId.toString(),
                ""));
            if (groupedFeature.containsKey(testSuiteId)) {
              Map<String, ScenarioData> scenarios = groupedFeature.get(testSuiteId);
              scenarios.put(element.getName(), scenarioData);
              groupedFeature.put(testSuiteId, scenarios);
            } else {
              Map<String, ScenarioData> scenarios = new HashMap<>();
              scenarios.put(element.getName(), scenarioData);
              groupedFeature.put(testSuiteId, scenarios);
            }
          } else {
            System.out.println(printDetail(null,
                feature.getName(),
                element.getName(),
                status,
                "",
                "",
                ""));
            System.out.println("Scenario will be skipped");
          }
          steps.clear();
        }
      });
    });

    // Process grouped feature
    if (!groupedFeature.isEmpty()) {
      groupedFeature.entrySet().parallelStream().forEach(feature -> {
        try {
          TestLinkPlugin plugin = new TestLinkPlugin(connection,
              testProject,
              testPlan,
              build,
              platFormName).setTestSuiteID(feature.getKey());
          plugin.linkTestCases(feature.getKey(), feature.getValue());
          linked.getAndSet(linked.get() + feature.getValue().size());
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
    int total = totalPassed.get() + totalFailed.get();
    System.out.println("\n================================================");
    System.out.println("\t\t\t\t\tSummary");
    System.out.println("================================================");
    System.out.println(String.format("\nTotal scenario\t\t\t\t: %d \nTotal linked to testlink\t: %d",
        total,
        linked.get()));
    System.out.println(String.format("\n%d scenarios passed\n%d scenarios failed",
        totalPassed.get(),
        totalFailed.get()));
    System.out.println("================================================");
  }

  private List<TestCaseStep> constructTestCaseSteps(List<String[]> steps) {
    List<TestCaseStep> testCaseSteps = new ArrayList<>();
    for (int i = 0; i < steps.size(); i++) {
      TestCaseStep caseStep = new TestCaseStep();
      caseStep.setActions(steps.get(i)[0]);
      caseStep.setNumber(i);
      caseStep.setExpectedResults("Passed");
      caseStep.setActive(true);
      caseStep.setExecutionType(ExecutionType.AUTOMATED);
      caseStep.setId(i);
      caseStep.setTestCaseVersionId(null);
      testCaseSteps.add(caseStep);
    }
    return testCaseSteps;
  }

  private TestCase constructTestCase(Integer testSuiteId,
      Integer testLinkId,
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
    testCase.setInternalId(testLinkId);
    testCase.setExternalId(testLinkId);
    testCase.setExecutionStatus(status ? ExecutionStatus.PASSED : ExecutionStatus.FAILED);
    return testCase;
  }

  private void readCucumberSteps(Map<String, ScenarioData> scenarios) {

    scenarios.entrySet()
        .parallelStream()
        .forEach(entry -> updateTestlink(entry.getValue().getName(),
            null,
            entry.getValue().getTestlinkTags().getTestSuiteId(),
            entry.getValue().getTestlinkTags().getTestLinkId(),
            entry.getValue().getPassed(),
            entry.getValue().getSummary(),
            entry.getValue().getDuration().intValue(),
            entry.getValue().getReasonFail()));
  }

  private TestlinkTags chooseTag(List<TestlinkTags> feature, List<TestlinkTags> scenario) {

    TestlinkTags tagsToUse;
    //kalo di scenario gaada maka pake yg default
    if (scenario.isEmpty() && !feature.isEmpty()) {
      tagsToUse = feature.get(0);
    } else if (!scenario.isEmpty() && !feature.isEmpty()) {
      //must filter because inherit tags
      List<TestlinkTags> tagsft = scenario.stream().filter(t -> {
        if (t.getTestLinkId().equalsIgnoreCase(feature.get(0).getTestLinkId()) && !t.getTestLinkId()
            .isEmpty())
          return false;
        return !t.getTestSuiteId().equalsIgnoreCase(feature.get(0).getTestSuiteId())
            || t.getTestSuiteId().equals("0");
      }).collect(Collectors.toList());
      if (tagsft.size() == 0) {
        tagsToUse = feature.get(0);
      } else {
        tagsToUse = tagsft.get(0);
      }
    } else {
      tagsToUse = scenario.get(0);
    }
    return tagsToUse;
  }

  private void updateTestlink(String title,
      List<String[]> testCaseSteps,
      String testSuiteId,
      String testLinkId,
      boolean status,
      String summary,
      Integer duration,
      String notes) {
    try {
      TestLinkPlugin plugin =
          new TestLinkPlugin(connection, testProject, testPlan, build, platFormName).setSummary(
              summary).setTitle(title).setTestSuiteID(Integer.parseInt(testSuiteId));
      plugin.linkTestCases(testLinkId,
          testCaseSteps,
          status,
          notes,
          duration,
          printDetail(null, null, title, status, notes, testSuiteId, testLinkId));
      if (status) {
        passed.getAndSet(passed.get() + 1);
      } else {
        failed.getAndSet(failed.get() + 1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String[] constructStep(Step step, int counter) {
    String additionalInfo = step.getRows() == null ?
        step.getDocString() == null ? "" : "\n" + step.getDocString().getValue() + "\n" :
        getRowStep(step.getRows());
    String stepDefinition =
        String.format("%s%s %s", step.getKeyword(), step.getName(), additionalInfo);
    String status = String.valueOf(step.getResult().getStatus().equalsIgnoreCase("passed"));
    String errorMessage =
        step.getResult().getErrorMessage() == null ? "" : step.getResult().getErrorMessage();
    String notes = "";
    if (!step.getResult().getStatus().equalsIgnoreCase("passed")) {
      notes =
          String.format("This Test Case Failed on step %s, Log:\n%s", counter + 1, errorMessage);
    }

    return new String[] {stepDefinition, status, errorMessage, notes};
  }

  private List<String[]> readSteps(List<Step> steps) {
    return IntStream.range(0, steps.size())
        .parallel()
        .mapToObj(counter -> constructStep(steps.get(counter), counter))
        .collect(Collectors.toList());
  }

  private String constructTableHeader(String[] headers) {
    StringBuilder headerRow = new StringBuilder();
    if (headers != null) {
      headerRow.append("<tr>");
      for (String header : headers) {
        headerRow.append(String.format("<th>%s</th>", StringEscapeUtils.escapeHtml4(header)));
      }
      headerRow.append("</tr>");
    }

    return headerRow.toString();
  }

  private String constructTableRow(String[] rows, int columnLength) {
    StringBuilder headerRow = new StringBuilder();
    if (rows != null) {
      headerRow.append("<tr>");
      columnLength = columnLength <= 0 ? rows.length : columnLength;
      for (int col = 0; col < columnLength; col++) {
        if (col < rows.length) {
          headerRow.append(String.format("<td>%s</td>", StringEscapeUtils.escapeHtml4(rows[col])));
        } else {
          headerRow.append("<td></td>");
        }
      }
      headerRow.append("</tr>");
    }

    return headerRow.toString();
  }

  private String getRowStep(Rows[] rows) {
    StringBuilder addedRow = new StringBuilder();
    try {
      if (rows != null) {
        int columnLength = rows[0].getCells().length;
        addedRow.append("<br><table>");
        for (int index = 0; index < rows.length; index++) {
          if (index == 0) {
            addedRow.append(constructTableHeader(rows[index].getCells()));
          } else {
            Rows row = rows[index];
            addedRow.append(constructTableRow(row.getCells(), columnLength));
          }
        }
        addedRow.append("</table><br>");
      }
    } catch (Exception ignore) {

    }
    return addedRow.toString();
  }

  private String printDetail(List<String[]> steps,
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
   * Digunakan untuk Overloading sajah ,
   *
   * @throws Exception
   */
  public void readWithCucumber() throws Exception {
    this.readWithCucumber("");
  }

  public void readWithEarlGrey(String earlGreyPath) {
    ObjectMapper objectMapper = new ObjectMapper();
    File file = new File(earlGreyPath);

    try {
      EarlGreyModel dataList = objectMapper.readValue(file, EarlGreyModel.class);
      if (dataList.getTestableSummaries() != null) {
        List<Subtest2> resultTest = dataList.getTestableSummaries()
            .get(0)
            .getTests()
            .get(0)
            .getSubtest1s()
            .get(0)
            .getSubtest2s();
        HashMap<String, List<TestResult>> test = new HashMap<>();
        for (Subtest2 test2 : resultTest) {
          List<TestResult> results = new ArrayList<>();
          for (Subtest3 test3 : test2.getSubtest3s()) {
            TestResult tr = new TestResult();
            String testName = test3.getTestName().split("_")[1].replace("()", "");
            tr.setTestName(testName);
            tr.setTestStatus(test3.getTestStatus());
            results.add(tr);
          }
          String testSuiteId = test2.getTestName().split("_")[2];
          if (test.get(testSuiteId) == null) {
            test.put(testSuiteId, results);
          } else {
            List<TestResult> combineResult = test.get(testSuiteId);
            combineResult.addAll(results);
            test.put(testSuiteId, combineResult);
          }
        }
        try {
          test.entrySet().forEach(v -> {
            v.getValue().forEach(t -> {
              System.out.println();
              System.out.println("Reading test step...");
              updateTestlinkEarlGrey(v.getKey(), t);
            });
          });
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void updateTestlinkEarlGrey(String testSuiteId, TestResult testResult) {
    ArrayList<String[]> steps = new ArrayList<>();
    String stepName = testResult.getTestName();

    //set step
    Boolean pass = testResult.getTestStatus().equalsIgnoreCase("success");
    String[] step = {stepName, pass.toString(), ""};
    steps.add(step);

    //set data
    data = new ScenarioData();
    data.setPassed(pass);
    data.setReasonFail(pass ? "" : "Failing on step " + stepName);
    data.setName(stepName);
    TestlinkTags tag = new TestlinkTags();
    tag.setTestSuiteId(testSuiteId);
    data.setTestlinkTags(tag);

    System.out.println(printDetail(steps,
        null,
        stepName,
        pass,
        data.getReasonFail(),
        testSuiteId,
        tag.getTestLinkId()));
    try {
      TestLinkPlugin plugin =
          new TestLinkPlugin(connection, testProject, testPlan, build, platFormName).setSummary(
              String.format("Feature : %s", data.getName()))
              .setTitle(data.getName())
              .setTestSuiteID(Integer.parseInt(testSuiteId));
      plugin.linkTestCases(tag.getTestLinkId(),
          steps,
          data.getPassed(),
          data.getReasonFail(),
          null,
          "");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private TestLinkAPI connectToTestlink() throws Exception {
    return TestLinkConnectionController.getInstance().
        setUrlTestlink(urlTestlink).setDevKey(DEVKEY).connect();
  }
}
