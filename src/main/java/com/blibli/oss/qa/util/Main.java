package com.blibli.oss.qa.util;

import com.blibli.oss.qa.util.constant.ReportGeneratorPolicy;
import com.blibli.oss.qa.util.constant.SupportedReports;
import com.blibli.oss.qa.util.model.TestLinkData;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Integrating with serenity
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
    //    private String testlinkURL = "";

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

    /**
     * @parameter
     */
    @Parameter(defaultValue = "STRICT")
    private String reportPolicy;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("===Process Started===");
        ReportGeneratorPolicy policy = null;
        try {
            policy = BadakReporter.searchEnum(ReportGeneratorPolicy.class, reportPolicy);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (policy == null) {
            policy = ReportGeneratorPolicy.STRICT;
        }

        TestLinkData testLinkData = new TestLinkData().setUrlTestlink(testlinkURL)
                .setDEVKEY(devKey)
                .setTestProject(projectName)
                .setTestPlan(testPlanName)
                .setBuild(buildName)
                .setReportPolicy(policy)
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
