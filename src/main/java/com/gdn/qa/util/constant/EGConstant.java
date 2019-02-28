package com.gdn.qa.util.constant;

import com.gdn.qa.util.service.ArgumentMismatchException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tri.abror on 2/28/2019.
 */

public class EGConstant {
  public static String JSON;
  public static String SETTING;
  public static String TESTLINK_URL = "";
  public static String DEV_KEY = "";
  public static String PROJECT_NAME = "";
  public static String TESTPLAN_NAME = "";
  public static String BUILD_NAME = "";

  public static void readFromArgs(Map<String, String> args) throws ArgumentMismatchException {
    if(args.containsKey("help")){
      printHelp();

      System.exit(0);
    }

    if(args.containsKey("jsonfile")){
      JSON = args.get("jsonfile");
    }else{
      throw new ArgumentMismatchException("Need --jsonFile args");
    }

    if(args.containsKey("setting")){
      SETTING = args.get("setting");
    }else{
      throw new ArgumentMismatchException("Need --setting args");
    }
  }

  public static void printTitle() {
    System.out.println("\n"
        + "  ____            _       _      _____       _                       _   _             \n"
        + " |  _ \\          | |     | |    |_   _|     | |                     | | (_)            \n"
        + " | |_) | __ _  __| | __ _| | __   | |  _ __ | |_ ___  __ _ _ __ __ _| |_ _  ___  _ __  \n"
        + " |  _ < / _` |/ _` |/ _` | |/ /   | | | '_ \\| __/ _ \\/ _` | '__/ _` | __| |/ _ \\| '_ \\ \n"
        + " | |_) | (_| | (_| | (_| |   <   _| |_| | | | ||  __/ (_| | | | (_| | |_| | (_) | | | |\n"
        + " |____/ \\__,_|\\__,_|\\__,_|_|\\_\\ |_____|_| |_|\\__\\___|\\__, |_|  \\__,_|\\__|_|\\___/|_| |_|\n"
        + "                                                      __/ |                            \n"
        + "                                                     |___/                             \n");

  }

  public static void printHelp() {
    System.out.println("Usage:  badak-integration [ARGUMENT]=[VALUE]");
    System.out.println("");
    System.out.println("Arguments:");
    System.out.println("--jsonFile=[String]     : Specify json/report file location (mandatory)\n");
    System.out.println("--setting=[String]      : Specify testlink configuration file location (mandatory)");
    System.out.println("--help                  : Displays help");
    System.out.println("");
  }

  public static void initializeTestlinkSetting(){
    String path = SETTING;
    HashMap<String, String> setting = new HashMap<>();

    BufferedReader reader;
    try{
      reader = new BufferedReader(new FileReader(path));
      String line = reader.readLine();
      System.out.println("================================================");
      System.out.println("Testlink Configuration : ");
      System.out.println("================================================");
      while(line != null){
        System.out.println(line);
        setting.put(line.split("=")[0], line.split("=")[1]);
        line = reader.readLine();
      }
      System.out.println("================================================");
      TESTLINK_URL = setting.get("testlinkURL");
      DEV_KEY = setting.get("devKey");
      PROJECT_NAME = setting.get("projectName");
      TESTPLAN_NAME = setting.get("testPlanName");
      BUILD_NAME = setting.get("buildName");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
