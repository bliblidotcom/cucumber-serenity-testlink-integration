package com.gdn.qa.util;

import com.gdn.qa.util.constant.EGConstant;
import com.gdn.qa.util.service.Utils;

/**
 * Created by tri.abror on 2/28/2019.
 */
public class EarlGreyIntegration {
  public static void main(String[] args) throws InterruptedException{
    Utils utils = new Utils();

    try{
      EGConstant.readFromArgs(utils.getAllArgs(args));
      EGConstant.initializeTestlinkSetting();
    }catch (Exception e){
      e.printStackTrace();
      System.exit(1);
    }

    TestResultReader testResultReader = new TestResultReader();
    testResultReader.initialize(EGConstant.TESTLINK_URL,
        EGConstant.DEV_KEY,
        EGConstant.PROJECT_NAME,
        EGConstant.TESTPLAN_NAME,
        EGConstant.BUILD_NAME,
        "");

    try {
      testResultReader.readWithEarlGrey(EGConstant.JSON);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
