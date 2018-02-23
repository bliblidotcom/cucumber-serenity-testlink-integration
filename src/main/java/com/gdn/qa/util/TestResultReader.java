package com.gdn.qa.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.qa.util.model.cucumber.CucumberModel;
import com.gdn.qa.util.model.cucumber.Step;
import com.gdn.qa.util.model.cucumber.Tags;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestResultReader {
    private static String DEFAULT_ID_TEST_LINK = "414";
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

    public void initialize(String testLinkURL, String testLinkDevKey, String testLinkProjectName,
                           String testLinkPlanName, String testLinkBuildName, String testLinkPlatFormName) {
        urlTestlink = testLinkURL;
        DEVKEY = testLinkDevKey;
        testProject = testLinkProjectName;
        testPlan = testLinkPlanName;
        build = testLinkBuildName;
        platFormName = testLinkPlatFormName;
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

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filePath = folder.getPath() + "/" + listOfFiles[i].getName();
                System.out.println("File " + filePath);
                if (filePath.contains(".xml") && !filePath.contains("AfterStories") && !filePath.contains("BeforeStories")) {
                    readXML(filePath);
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
            TLPlugin testLinkPlugin = new TLPlugin();
            if (result.get("PreConditions") != null) {
                testLinkPlugin.setPreCondition(result.get("PreConditions"));
            }
            if (result.get("Title") != null) {
                testLinkPlugin.setpTitle(result.get("Title"));
            }

            if (result.get("Summary") != null) {
                testLinkPlugin.setSummary(result.get("Summary"));
            }

            testLinkPlugin.TLPluginInitialize(result.get("testLinkID"),
                    testCaseSteps, Integer.parseInt(result.get("TestSuiteID")), title, urlTestlink, DEVKEY, testProject, testPlan,
                    build, platFormName);

            if (passed) {
                testLinkPlugin.updateTestcasePassed();
            } else {
                testLinkPlugin.updateTestcaseFail("");
                System.out.println("Ini Gagal");
            }
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
        if (cucumberJsonPath == "" || cucumberJsonPath.isEmpty()) {
            cucumberJsonPath = System.getProperty("user.dir") + "/target/destination/cucumber.json";
        }
        File cucumberFile = new File(cucumberJsonPath);
        if (!cucumberFile.exists()) {
            System.out.println("Cucumber Path : " + cucumberJsonPath);
            throw new Exception("Cucumber Json is not exist can't Post To Testlink , is your setup is right ? ");
        }
        CucumberModel[] cucumberModels = objectMapper.readValue(cucumberFile, CucumberModel[].class);

        // Read Read Each Test Feature
        for (CucumberModel cucumberModel : cucumberModels) {
            ArrayList<String[]> testCaseSteps = new ArrayList<>();
            String testLinkId = "";
            String testSuiteId = null;
            Boolean passed = true;
            String reasonFail = "";
            // Get tag TestlinkID or TestSuiteId
            Tags tags = cucumberModel.getTags().stream().filter(x -> {
                if (x.getName().toLowerCase().contains("testlinkid") || x.getName().toLowerCase().contains("testSuiteId"))
                    return true;
                return false;
            }).findAny().orElse(null);
            // Lanjutkan Looping Jika Tidak ditemuin
            if (tags == null) {
                continue;
            }
            if (tags.getName().toLowerCase().contains("testlinkid")) {
                testLinkId = tags.getName().split("=")[1];
            } else {
                testSuiteId = tags.getName().split("=")[1];
            }
            // Read Each Steps
            for (Step step : cucumberModel.getElements().get(0).getSteps()) {
                String[] stepsResult = {step.getName(), Boolean.toString((step.getResult().getStatus().equalsIgnoreCase("passed")) ? true : false), (step.getResult().getErrorMessage() == null) ? "" : step.getResult().getErrorMessage()};
                // Check Sucess or Not
                if (!step.getResult().getStatus().equalsIgnoreCase("passed") && passed ) {
                    passed = false;
                    reasonFail = step.getResult().getErrorMessage();
                }
                testCaseSteps.add(stepsResult);
            }
            if (testSuiteId == null) {
                testSuiteId = "0";
            }
            TLPlugin testLinkPlugin = new TLPlugin();
            testLinkPlugin.TLPluginInitialize(testLinkId,
                    testCaseSteps, Integer.parseInt(testSuiteId), cucumberModel.getName(), urlTestlink, DEVKEY, testProject, testPlan,
                    build, platFormName);
            if (passed) {
                testLinkPlugin.updateTestcasePassed();
            }else{
                testLinkPlugin.updateTestcaseFail(reasonFail);
            }
        }

    }

    /**
     * Digunakan untuk Overloading sajah ,
     *
     * @throws Exception
     */
    public void readWithCucumber() throws Exception {
        this.readWithCucumber("");
    }

}
