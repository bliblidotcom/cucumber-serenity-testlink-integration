Cucumber Serenity Testlink Integration
--------------------------
[![Build Status](https://travis-ci.org/bliblidotcom/cucumber-serenity-testlink-integration.svg?branch=master)](https://travis-ci.org/bliblidotcom/cucumber-serenity-testlink-integration)
This is a Plugin for integrating your cucumber test to Testlink via API 

It will Create, Update , and Report your test Result in testlink based on your cucumber Steps

#### Requirement
- Testlink With Version 1.9.15 or higher 
- Java 8 

#### Usage
 
Add this in your ```pom.xml```

```xml
                <plugins>
                   ...........Your Other Plugin.......
                <plugin>
                <groupId>com.blibli.oss.qa.util</groupId>
                <artifactId>cucumber-serenity-testlink-integration</artifactId>
                <version>4.0.0</version>
                <executions>
                    	<execution>
                        	<phase>post-integration-test</phase>
                        		<goals>
                            			<goal>testlink-serenity</goal>
                        		</goals>
                        	<configuration>
                            		<testlinkURL>[Your Testlink URL]/lib/api/xmlrpc/v1/xmlrpc.php</testlinkURL>
                            		<devKey>Your Testlink Dev Key</devKey>
                            		<projectName>Your Project Name in testlink (Ex Finance , Android Apps)</projectName>
               	            		<testPlanName>Your Test Plan Name</testPlanName>
              		        	<buildName>Your Build Name in Your Test Plan Name</buildName>
                 	        	<platformName>Your Platform Name(Optional)</platformName>
              		 	</configuration>
                	</execution>
             	   </executions>
            	</plugin>
                ...........Your Other Plugin.......
              </plugins>
``` 
Don't Forget to add our blibli.com Open Source Plugin Repository 

```xml
<pluginRepositories>
................... Your other plugin Repositories .............
<pluginRepository>
    	<snapshots>
        	<enabled>false</enabled>
    	</snapshots>
	<id>jitpack.io</id>
    	<url>https://jitpack.io</url>
</pluginRepository>
```

Add new user with name and username **automation-test** in your testlink account and makesure it's able to read , write , update and execute any testcase , this user is used for updating the testcase and also for execution the testcase.

And Add plugin to generate file called **cucumber.json** in your folder **target/destination/cucumber.json** this in your Cucumber Options
```java
@CucumberOptions(features = "src/test/resources/features/"
        , plugin = {"json:target/destination/cucumber.json"},
        tags = {""})
public class CucumberRunner {
}
```
And Finaly you can define your testsuite in each features files via tags
```gherkin
@LookUpFeature 
Feature: LookUp A Definition

  @Positive @Apple @TestSuiteID=12345
  Scenario: Looking up the definition of 'apple'
    Given the user is on the Wikionary home page
    When the user looks up the definition of the word 'apple'
    Then they should see the definition 'A common, round fruit produced by the tree Malus domestica, cultivated in temperate climates.'
```
For that it will searching testcase with title _Looking up the definition of 'apple'_ in testsuite 12345 , 
- if it's desn't exist , it will create new
- if it's exist and the steps is same it will just update the status execution (Passed Or Not)
- if it's exist and the steps is not same , it will create new version for that testcase and update the result as well
- You can put the tag in the feature tag , so it will inheritence in all scenario inside that feature

```gherkin
@LookUpFeature @TestSuiteID=12345
Feature: LookUp A Definition

  @Positive @Apple 
  Scenario: Looking up the definition of 'apple'
    Given the user is on the Wikionary home page
    When the user looks up the definition of the word 'apple'
    Then they should see the definition 'A common, round fruit produced by the tree Malus domestica, cultivated in temperate climates.'
  @Positive @Pear 
  Scenario: Looking up the definition of 'pear'
    Given the user is on the Wikionary home page
    When the user looks up the definition of the word 'pear'
    Then they should see the definition 'pear is ....'
``` 

Or If you just want to update the execution result without update your scenario you can put tag `@TestlinkID`
```gherkin
@LookUpFeature 
Feature: LookUp A Definition

  @Positive @Apple @TestlinkID=BLIBLI-123
  Scenario: Looking up the definition of 'apple'
    Given the user is on the Wikionary home page
    When the user looks up the definition of the word 'apple'
    Then they should see the definition 'A common, round fruit produced by the tree Malus domestica, cultivated in temperate climates.'
```

It will Update Execution ***result only*** without modify your testcase in your testcase with ID **BLIBLI-123**

#### Running Test And Updating Result

Use `mvn clean verify` to update the result into testlink , since it's using maven plugin it will not update result if you run it via junit runner or right click and run test
