package com.blibli.oss.qa.util;

import com.blibli.oss.qa.util.constant.EGConstant;
import com.blibli.oss.qa.util.service.Utils;

/**
 * Created by tri.abror on 2/28/2019.
 */
public class EarlGreyIntegration {
  public static void main(String[] args) throws InterruptedException {
    Utils utils = new Utils();
    EGConstant.printTitle();

    try {
      EGConstant.readFromArgs(utils.getAllArgs(args));
      EGConstant.initializeTestlinkSetting();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    TestResultReader testResultReader = new TestResultReader();
    try {
      testResultReader.initialize(EGConstant.TESTLINK_URL,
          EGConstant.DEV_KEY,
          EGConstant.PROJECT_NAME,
          EGConstant.TESTPLAN_NAME,
          EGConstant.BUILD_NAME,
          "");
      testResultReader.readWithEarlGrey(EGConstant.JSON);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
