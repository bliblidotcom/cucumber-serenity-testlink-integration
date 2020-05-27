package com.gdn.qa.util.reader;

import com.gdn.qa.util.model.ScenarioData;
import com.gdn.qa.util.model.TagsReader;
import com.gdn.qa.util.model.TestLinkData;
import com.gdn.qa.util.model.cucumber.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author yunaz.ramadhan on 3/1/2020
 */
public class CucumberTestResultReader extends BaseTestResultReader<List<CucumberModel>>
    implements TagsReader<List<Tags>> {
  public CucumberTestResultReader(TestLinkData testLinkData, String reportFolder) throws Exception {
    super(testLinkData, reportFolder);
  }

  @Override
  Map<Integer, Map<String, ScenarioData>> groupingScenariosByTestSuiteId(List<CucumberModel> reports)
      throws Exception {
    Map<Integer, Map<String, ScenarioData>> groupedFeature = new HashMap<>();

    System.out.println("Populating scenarios from feature :");
    for (CucumberModel feature : reports) {
      System.out.println(feature.getName());
      String summary =
          String.format("Feature : %s, %s", feature.getName(), feature.getDescription());
      List<String[]> steps = new ArrayList<>();
      for (Elements element : feature.getElements()) {
        ScenarioData scenarioData = new ScenarioData();
        scenarioData.setTreeNode(getTreeNode(feature.getUri()));
        if (element.getType().equalsIgnoreCase("background")) {
          steps.addAll(0, readSteps(element.getSteps()));
        } else if (element.getType().equalsIgnoreCase("scenario")) {
          steps.addAll(readSteps(element.getSteps()));
          Integer testSuiteId =
              getIdNumberFromString(getValueFromTags("testsuiteid", element.getTags()));
          String testLinkId = null;
          try {
            String[] externalIdWithTag =
                getValueFromTags("testlinkid", element.getTags()).split("=");
            testLinkId = externalIdWithTag.length < 2 ? null : externalIdWithTag[1];
          } catch (Exception ignored) {
          }
          testSuiteId = testSuiteId == null ? -1 : testSuiteId;
          String reason = "";
          Integer indexFail = null;
          boolean status = true;
          for (int j = 0; j < steps.size(); j++) {
            if (!steps.get(j)[3].trim().isEmpty()) {
              reason = steps.get(j)[3];
              indexFail = j;
              status = false;
              break;
            }
          }
          Double duration = element.getSteps()
              .stream()
              .mapToDouble(e -> (int) (
                              (e.getResult().getDuration() == null)? 0 :
                                      e.getResult().getDuration().getTime() / 1000000000.0))
              .sum();

          scenarioData.setReasonFail(reason);
          scenarioData.setIndexFail(indexFail);
          scenarioData.setPassed(status);
          scenarioData.setName(element.getName());
          scenarioData.setSummary(summary);
          scenarioData.setTestCase(constructTestCase(testSuiteId,
              testLinkId,
              element.getName(),
              summary,
              status,
              steps));
          scenarioData.setDuration(duration);
          scenarioData.setFeatureName(feature.getName());
          scenarioData.setDetailToPrint(printDetail(null,
              feature.getName(),
              element.getName(),
              status,
              scenarioData.getReasonFail(),
              testSuiteId < 0 ? "" : String.valueOf(testSuiteId),
              testLinkId == null ? "" : testLinkId));

          Map<String, ScenarioData> scenarios =
              groupedFeature.getOrDefault(testSuiteId, new HashMap<>());
          scenarios.put(element.getName(), scenarioData);
          groupedFeature.put(testSuiteId, scenarios);

          steps.clear();
        }
      }
    }

    return groupedFeature;
  }

  @Override
  List<CucumberModel> filterReportFiles(List<File> allFiles) throws Exception {
    List<CucumberModel> result = new ArrayList<>();
    if (allFiles != null && !allFiles.isEmpty()) {
      for (File file : allFiles) {
        try {
          List<CucumberModel> features =
              Arrays.asList(readFileToObject(file, CucumberModel[].class));
          if (!features.isEmpty()) {
            System.out.println("Opening report files from " + file.getPath());
            result = features;
            break;
          }
        } catch (Exception ignored) {

        }
      }
    }
    return result;
  }

  private String[] constructStep(Step step, int counter) {
    String additionalInfo = step.getRows() == null ?
        step.getDocString() == null ? "" : "\n" + step.getDocString().getValue() + "\n" :
        getRowStep(step.getRows());
    String stepDefinition =
        String.format("%s%s %s", step.getKeyword(), step.getName(), additionalInfo);
    String status = String.valueOf(step.getResult().getStatus().equalsIgnoreCase("passed"));
    String errorMessage =
        step.getResult().getErrorMessage() == null ? "" : step.getResult().getErrorMessage();
    String notes = "";
    if (!Boolean.parseBoolean(status)) {
      notes =
          String.format("This Test Case Failed on step %s, Log:\n%s", counter + 1, errorMessage);
    }

    return new String[] {stepDefinition, status, errorMessage, notes};
  }

  private List<String[]> readSteps(List<Step> steps) {
    return IntStream.range(0, steps.size())
        .parallel()
        .mapToObj(counter -> constructStep(steps.get(counter), counter))
        .collect(Collectors.toList());
  }

  private String constructTableHeader(String[] headers) {
    StringBuilder headerRow = new StringBuilder();
    if (headers != null) {
      headerRow.append("<tr>");
      for (String header : headers) {
        headerRow.append(String.format("<th>%s</th>", StringEscapeUtils.escapeHtml4(header)));
      }
      headerRow.append("</tr>");
    }

    return headerRow.toString();
  }

  private String constructTableRow(String[] rows, int columnLength) {
    StringBuilder headerRow = new StringBuilder();
    if (rows != null) {
      headerRow.append("<tr>");
      columnLength = columnLength <= 0 ? rows.length : columnLength;
      for (int col = 0; col < columnLength; col++) {
        if (col < rows.length) {
          headerRow.append(String.format("<td>%s</td>", StringEscapeUtils.escapeHtml4(rows[col])));
        } else {
          headerRow.append("<td></td>");
        }
      }
      headerRow.append("</tr>");
    }

    return headerRow.toString();
  }

  private String getRowStep(Rows[] rows) {
    StringBuilder addedRow = new StringBuilder();
    try {
      if (rows != null) {
        int columnLength = rows[0].getCells().length;
        addedRow.append("<br><table>");
        for (int index = 0; index < rows.length; index++) {
          if (index == 0) {
            addedRow.append(constructTableHeader(rows[index].getCells()));
          } else {
            Rows row = rows[index];
            addedRow.append(constructTableRow(row.getCells(), columnLength));
          }
        }
        addedRow.append("</table><br>");
      }
    } catch (Exception ignore) {

    }
    return addedRow.toString();
  }

  @Override
  public String getValueFromTags(String tagName, List<Tags> tags) {
    try {
      AtomicReference<String> result = new AtomicReference<>();
      if (tags != null && !tags.isEmpty()) {
        List<Tags> filteredTags = tags.parallelStream()
            .filter(e -> e.getName().toLowerCase().contains(tagName.toLowerCase()))
            .collect(Collectors.toList());
        if (!filteredTags.isEmpty()) {
          filteredTags.forEach(tag -> {
            result.set(tag.getName().trim());
          });
        }
      }
      return result.get();
    } catch (Exception e) {
      return null;
    }
  }
}
