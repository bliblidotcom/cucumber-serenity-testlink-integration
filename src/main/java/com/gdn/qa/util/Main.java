package com.gdn.qa.util;

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
    private String testlinkURL;

    /**
     * @parameter
     */
    @Parameter
    private String devKey;

    /**
     * @parameter
     */
    @Parameter
    private String projectName;

    /**
     * @parameter
     */
    @Parameter
    private String testPlanName;

    /**
     * @parameter
     */
    @Parameter
    private String buildName;

    /**
     * @parameter
     */
    @Parameter
    private String platformName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        loadBanner();
        System.out.println("===Config===");
        System.out.println("testlink URL : " + testlinkURL);
        System.out.println("DevKey : " + devKey);
        System.out.println("Project Name : " + projectName);
        System.out.println("Test Plan Name : " + testPlanName);
        System.out.println("Build Name : " + buildName);
        System.out.println("Platform Name : " + platformName);
        System.out.println("===End Of Config===");
        System.out.println("===Process Started===");
        TestResultReader testResultReader = new TestResultReader();
        testResultReader.initialize(testlinkURL, devKey,
                projectName, testPlanName, buildName, platformName);
        testResultReader.readResult();
        System.out.println("===Process Ended===");
    }

    private void loadBanner() {
        System.out.println(Banner.textBanner);
    }
}
