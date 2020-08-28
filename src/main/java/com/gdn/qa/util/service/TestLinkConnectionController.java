package com.gdn.qa.util.service;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;

import java.net.URL;

/**
 * @author yunaz.ramadhan on 1/30/2020
 */
public class TestLinkConnectionController {
  private static TestLinkConnectionController INSTANCE;

  private String DEVKEY;
  private String urlTestlink;
  private TestLinkAPI connection;

  private TestLinkConnectionController(){

  }

  public String getDEVKEY() {
    return DEVKEY;
  }

  public TestLinkConnectionController setDevKey(String DEVKEY) {
    this.DEVKEY = DEVKEY;
    return this;
  }

  public String getUrlTestlink() {
    return urlTestlink;
  }

  public TestLinkConnectionController setUrlTestlink(String urlTestlink) {
    this.urlTestlink = urlTestlink;
    return this;
  }

  public TestLinkAPI connect() throws Exception {
    if(connection == null) {
      if(getUrlTestlink().trim().isEmpty() ||getDEVKEY().trim().isEmpty()){
        throw new Exception("Please specify testlink url and devkey to connect!");
      }
      connection = new TestLinkAPI(new URL(urlTestlink), DEVKEY);
      System.out.println("Establishing connection to Testlink...");
      if (connection.ping().equalsIgnoreCase("Hello!")) {
        System.out.println("Done!");
      }
    }else {
      System.out.println("Use previous connection to testlink");
    }
    return connection;
  }

  public static TestLinkConnectionController getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TestLinkConnectionController();
    }

    return INSTANCE;
  }
}
