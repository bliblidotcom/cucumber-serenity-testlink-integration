package com.blibli.oss.qa.util.service;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.TestLinkMethods;
import br.eti.kinoshita.testlinkjavaapi.constants.TestLinkParams;
import br.eti.kinoshita.testlinkjavaapi.model.Execution;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import br.eti.kinoshita.testlinkjavaapi.util.Util;
import org.apache.xmlrpc.XmlRpcException;

import java.util.HashMap;
import java.util.Map;

public class CustomTestlinkService {
    private TestLinkAPI testLinkAPI;

    public TestLinkAPI getTestLinkAPI() {
        return testLinkAPI;
    }

    public CustomTestlinkService(TestLinkAPI testLinkAPI) {
        this.testLinkAPI = testLinkAPI;
    }

    public Execution getLastExecutionResultByBuild(Integer testPlanId, Integer testCaseId, Integer testCaseExternalId , Integer testBuildId) throws TestLinkAPIException {
        Execution execution = null;
        try {
            Map<String, Object> executionData = new HashMap();
            executionData.put(TestLinkParams.TEST_PLAN_ID.toString(), testPlanId);
            executionData.put(TestLinkParams.TEST_CASE_ID.toString(), testCaseId);
            executionData.put(TestLinkParams.TEST_CASE_EXTERNAL_ID.toString(), testCaseExternalId);
            executionData.put(TestLinkParams.BUILD_ID.toString(), testBuildId);
            Object response = testLinkAPI.executeXmlRpcCall(TestLinkMethods.GET_LAST_EXECUTION_RESULT.toString(), executionData);
            Object[] responseArray = Util.castToArray(response);
            Map<String, Object> responseMap = (Map)responseArray[0];
            if (responseMap instanceof Map && responseMap.size() > 0) {
                execution = Util.getExecution(responseMap);
            }

            return execution;
        } catch (XmlRpcException var9) {
            throw new TestLinkAPIException("Error retrieving last execution result: " + var9.getMessage(), var9);
        }
    }
}
