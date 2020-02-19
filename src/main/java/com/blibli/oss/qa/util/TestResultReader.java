package com.blibli.oss.qa.util;

import com.blibli.oss.qa.util.model.cucumber.CucumberModel;
import com.blibli.oss.qa.util.model.cucumber.Rows;
import com.blibli.oss.qa.util.model.cucumber.Step;
import com.blibli.oss.qa.util.model.earlgrey.EarlGreyModel;
import com.blibli.oss.qa.util.model.earlgrey.Subtest2;
import com.blibli.oss.qa.util.model.earlgrey.Subtest3;
import com.blibli.oss.qa.util.model.earlgrey.TestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestResultReader {

    //Enter your Test Project Name here
    String testProject = "";
    //Enter your Test Plan here
    String testPlan = "";
    //Enter your Test build here
    String build = "";
    //Enter Your test Platform Name
    String platFormName = "";
    ScenarioData data;
    Boolean tempResult;
    AtomicReference<Integer> passed = new AtomicReference<>(0);
    AtomicReference<Integer> failed = new AtomicReference<>(0);
    //Enter your project API key here.
    private String DEVKEY = "";
    //Enter your Test Link URL here
    private String urlTestlink = "";
    private TLPlugin plugin;

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
        startTLPlugin();
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
                                reasonFail = failure.getTextContent();
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
            if (result.get("PreConditions") != null) {
                plugin.setPreCondition(result.get("PreConditions"));
            }
            if (result.get("Title") != null) {
                plugin.setpTitle(result.get("Title"));
            }

            if (result.get("Summary") != null) {
                plugin.setSummary(result.get("Summary"));
            } else {
                plugin.setSummary(result.get("Title"));
            }

            plugin.setJudul(title);
            plugin.setTestSuiteID(Integer.parseInt(result.get("TestSuiteID")));
            plugin.linkTestCases(result.get("testLinkID"), testCaseSteps);

            if (passed) {
                plugin.updateTestcasePassed();
            } else {
                plugin.updateTestcaseFail("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

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
        CucumberModel[] features = objectMapper.readValue(cucumberFile, CucumberModel[].class);

        // Read Read Each Test Feature
        for (CucumberModel feature : features) {
            System.out.println("Checking features " + feature.getName());
            //get default testsuiteid or test case id
            List<TestlinkTags> defaultTags = TagsReader.readTags(feature.getTags());

            //read background steps
            data = new ScenarioData();
            ArrayList<String[]> background = new ArrayList<>();
            feature.getElements()
                    .stream()
                    .filter(ft -> ft.getKeyword().equalsIgnoreCase("Background"))
                    .findFirst()
                    .ifPresent(b -> {
                        background.addAll(readSteps(b.getSteps()));
                    });
            this.tempResult = data.getPassed();
            plugin.setSummary(String.format("Feature : %s\n\t%s",
                    feature.getName(),
                    feature.getDescription()));
            readCucumberSteps("scenario", defaultTags, feature, background);
            readCucumberSteps("Scenario Outline", defaultTags, feature, background);
            System.out.println(String.format("\nSummary: %d passed and %d failed",
                    passed.get(),
                    failed.get()));
        }
    }

    private void readCucumberSteps(String keyword,
                                   List<TestlinkTags> defaultTags,
                                   CucumberModel feature,
                                   ArrayList<String[]> background) {
        HashMap<String, ArrayList<String[]>> scenarioOutlineSteps = new HashMap<>();
        HashMap<String, ScenarioData> scenarioOutlineData = new HashMap<>();
        ArrayList<String[]> dw = new ArrayList<>();
        //loop per scenario outline
        feature.getElements().stream()
                //filter to get element with keyword scenario only
                .filter(ft -> ft.getKeyword().equalsIgnoreCase(keyword) && (defaultTags.size() != 0
                        || TagsReader.readTags(ft.getTags()).size() != 0)).forEach(scenario -> {
            data.setPassed(tempResult);
            if (!scenarioOutlineSteps.containsKey(scenario.getName())) {
                ArrayList<String[]> steps = new ArrayList<>();
                steps.addAll(0, background);
                ScenarioData data = new ScenarioData();
                data.setName(scenario.getName());
                scenarioOutlineSteps.put(scenario.getName(), steps);
                scenarioOutlineData.put(scenario.getName(), data);
            }
            ScenarioData tempData = scenarioOutlineData.get(scenario.getName());
            ArrayList<String[]> tempSteps = scenarioOutlineSteps.get(scenario.getName());
            List<TestlinkTags> scenarioTags = TagsReader.readTags(scenario.getTags());
            if (defaultTags.size() != 0 || scenarioTags.size() != 0) {
                List<String[]> steps = readSteps(scenario.getSteps());
                tempSteps.addAll(steps);
                TestlinkTags tagsToUse = chooseTag(defaultTags, scenarioTags);
                //kalo di scenario gaada maka pake yg default
                tempData.setTestlinkTags(tagsToUse);
            }
            tempData.setReasonFail(data.getReasonFail());
            if (tempData.getPassed())
                tempData.setPassed(data.getPassed());
            scenarioOutlineData.put(scenario.getName(), tempData);
            scenarioOutlineSteps.put(scenario.getName(), tempSteps);
        });

        scenarioOutlineData.forEach((key, value) -> {
            if (value.getPassed()) {
                passed.getAndSet(passed.get() + 1);
            } else {
                failed.getAndSet(failed.get() + 1);
            }
            data.setPassed(value.getPassed());
            data.setReasonFail(value.getReasonFail());
            data.setName(value.getName());
            data.setTestlinkTags(value.getTestlinkTags());
            updateTestlink(scenarioOutlineSteps.get(key));
        });
    }

    private TestlinkTags chooseTag(List<TestlinkTags> feature, List<TestlinkTags> scenario) {

        TestlinkTags tagsToUse;
        //kalo di scenario gaada maka pake yg default
        if (scenario.size() == 0) {
            tagsToUse = feature.get(0);
        } else if (feature.size() != 0) {
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

    private void updateTestlink(ArrayList<String[]> testCaseSteps) {
        printDetail(testCaseSteps);
        try {
            plugin.setJudul(data.getName());
            plugin.setTestSuiteID(Integer.parseInt(data.getTestlinkTags().getTestSuiteId()));
            plugin.linkTestCases(data.getTestlinkTags().getTestLinkId(), testCaseSteps);
            if (data.getPassed()) {
                plugin.updateTestcasePassed();
            } else {
                plugin.updateTestcaseFail(data.getReasonFail());
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
        if (!step.getResult().getStatus().equalsIgnoreCase("passed") && data.getPassed()) {
            data.setPassed(false);
            data.setReasonFail(String.format("This Test Case Failed on step %s, Log:\n%s",
                    counter + 1,
                    step.getResult().getErrorMessage()));
        } else if (data.getPassed()) {
            data.setReasonFail("");
        }
        return new String[]{stepDefinition, status, errorMessage};
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

    private void printDetail(ArrayList<String[]> steps) {
        System.out.println("================================================");
        System.out.println("Title\t\t: " + data.getName());
        System.out.println("Passed\t\t: " + data.getPassed());
        System.out.println("Reason Fail\t: " + data.getReasonFail());
        System.out.println("TestSuiteId\t: " + data.getTestlinkTags().getTestSuiteId());
        System.out.println("TestLinkId\t: " + data.getTestlinkTags().getTestLinkId());

        System.out.println("Steps:");
        steps.forEach(step -> {
            System.out.println(step[0]);
        });
        System.out.println("================================================");
    }
    
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
        String pass = testResult.getTestStatus().equalsIgnoreCase("success") ? "true" : "false";
        String[] step = {stepName, pass, ""};
        steps.add(step);

        //set data
        data = new ScenarioData();
        data.setPassed(Boolean.parseBoolean(pass));
        data.setReasonFail(Boolean.parseBoolean(pass) ? "" : "Failing on step " + stepName);
        data.setName(stepName);
        TestlinkTags tag = new TestlinkTags();
        tag.setTestSuiteId(testSuiteId);
        data.setTestlinkTags(tag);

        printDetail(steps);
        try {
            plugin.setTestSuiteID(Integer.parseInt(testSuiteId));
            plugin.setJudul(testResult.getTestName());
            plugin.linkTestCases(tag.getTestLinkId(), steps);

            if (Boolean.parseBoolean(pass)) {
                plugin.updateTestcasePassed();
            } else {
                plugin.updateTestcaseFail("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TLPlugin startTLPlugin() throws Exception {
        plugin = TLPlugin.getInstance();
        plugin.initializeTLPlugin(urlTestlink, DEVKEY, testProject, testPlan, build, platFormName);
        return plugin;
    }
}
