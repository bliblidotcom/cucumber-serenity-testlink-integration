package com.gdn.qa.util;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Banner.loadBanner();
        System.out.println("===Config===");
        System.out.println("testlink URL : " + testlinkURL);
        System.out.println("DevKey : " + devKey);
        System.out.println("Project Name : " + projectName);
        System.out.println("Test Plan Name : " + testPlanName);
        System.out.println("Build Name : " + buildName);
        System.out.println("Platform Name : " + platformName);
        System.out.println("===End Of Config===");
        System.out.println("===Process Started===");

        // check if cucumebr json exist
        String cucumberPath = System.getProperty("user.dir") + "/target/destination/cucumber.json";
        System.out.println("Cucumber Path : " + cucumberPath);
        File cucumberFile = new File(cucumberPath);
        TestResultReader testResultReader = new TestResultReader();
        testResultReader.initialize(testlinkURL, devKey,
                projectName, testPlanName, buildName, platformName);

        if(!cucumberFile.exists()){
            System.out.println("=== Run with Jbehave ===");
            testResultReader.readResult();
        }else{
            System.out.println("=== Run with Cucumber ===");
            try {
                testResultReader.readWithCucumber(cucumberPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("===Process Ended===");
    }
}
