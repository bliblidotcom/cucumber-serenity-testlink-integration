package com.gdn.qa.util;

import com.gdn.qa.util.constant.SupportedReports;
import com.gdn.qa.util.model.TestLinkData;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Created by hendri.antomy on 8/2/2017.
 *
 * @Goal testlink-serenity
 */
@Mojo(name = "testlink-serenity")
public class Main extends AbstractMojo {

  /**
   * @parameter
   */
  @Parameter
  private String sourceDir;

  /**
   * @parameter
   */
  @Parameter
  private String testlinkURL;
  //    private String testlinkURL = "https://testlink.gdn-app.com/lib/api/xmlrpc/v1/xmlrpc.php";

  /**
   * @parameter
   */
  @Parameter
  private String devKey;
  //    private String devKey="b5c1d6045227609983587bc8a4e499cf";

  /**
   * @parameter
   */
  @Parameter
  private String projectName;
  //    private String projectName="Surabaya Sample Project Rest Assured";

  /**
   * @parameter
   */
  @Parameter
  private String testPlanName;
  //    private String testPlanName="Rest Assured UAT1";

  /**
   * @parameter
   */
  @Parameter
  private String buildName;
  //    private String buildName="BUILD UAT1";

  /**
   * @parameter
   */
  @Parameter
  private String platformName;

  /**
   * @parameter
   */
  @Parameter(defaultValue = "CUCUMBER")
  private String reportsFrom;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    System.out.println("===Process Started===");
    TestLinkData testLinkData = new TestLinkData().setUrlTestlink(testlinkURL)
        .setDEVKEY(devKey)
        .setTestProject(projectName)
        .setTestPlan(testPlanName)
        .setBuild(buildName)
        .setPlatFormName(platformName);
    try {
      SupportedReports type = BadakReporter.searchEnum(SupportedReports.class, reportsFrom);
      if (type == null) {
        type = SupportedReports.CUCUMBER;
      }
      if (sourceDir == null || sourceDir.trim().isEmpty()) {
        BadakReporter.getReader(type, testLinkData).writeToTestLink();
      } else {
        BadakReporter.getReader(type, testLinkData, sourceDir).writeToTestLink();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("===Process Ended===");
  }
}
