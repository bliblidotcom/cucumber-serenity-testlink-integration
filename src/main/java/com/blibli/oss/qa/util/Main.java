package com.blibli.oss.qa.util;

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

    @Parameter
    private String sourceDir;

    @Parameter
    private String testlinkURL;

    @Parameter
    private String devKey;

    @Parameter
    private String projectName;

    @Parameter
    private String testPlanName;

    @Parameter
    private String buildName;

    @Parameter
    private String platformName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Banner.loadBanner();
        System.out.println("===Config===");
        System.out.println("Source Directory: " + sourceDir);
        System.out.println("testlink URL : " + testlinkURL);
        System.out.println("DevKey : " + devKey);
        System.out.println("Project Name : " + projectName);
        System.out.println("Test Plan Name : " + testPlanName);
        System.out.println("Build Name : " + buildName);
        System.out.println("Platform Name : " + platformName);
        System.out.println("===End Of Config===");
        System.out.println("===Process Started===");

        // Scanning File under destination folder
        String cucumberFolder;
        if (sourceDir == null || sourceDir.isEmpty()) {
            cucumberFolder =
                    System.getProperty("user.dir") + File.separator + "target/destination/";
        } else {
            cucumberFolder = sourceDir;
        }

        try (Stream<Path> walk = Files.walk(Paths.get(cucumberFolder))) {
            List<String> cucumberPaths = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).filter(s -> s.toLowerCase().endsWith(".json")).collect(Collectors.toList());
            // check if cucumebr json exist
            System.out.print(" File Scanned : ");
            cucumberPaths.forEach(System.out::println);
            System.out.println("=================================================");
            cucumberPaths.parallelStream().forEach(cucumberPath -> {
                try {
                    System.out.println("Cucumber Path : " + cucumberPath);
                    File cucumberFile = new File(cucumberPath);
                    TestResultReader testResultReader = new TestResultReader();
                    testResultReader.initialize(testlinkURL,
                            devKey,
                            projectName,
                            testPlanName,
                            buildName,
                            platformName);

                    if (!cucumberFile.exists()) {
                        System.out.println("=== Run with Jbehave ===");
                        testResultReader.readResult();
                    } else {
                        System.out.println("=== Run with Cucumber ===");
                        testResultReader.readWithCucumber(cucumberPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("===Process Ended===");
    }
}
