package com.blibli.oss.qa.util.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tri.abror on 2/28/2019.
 */
public class Utils {

  public Map<String, String> getAllArgs(String[] args) {
    List<String> param = Arrays.asList(args);
    Map<String, String> mapParam = param.stream().collect(Collectors.toMap(key -> {
      String[] result = key.split("=");
      return result[0].replaceAll("--", "").toLowerCase();
    }, val -> {
      String[] result = val.split("=");
      if (result.length == 1) {
        return "";
      } else {
        return result[1];
      }
    }));

    return mapParam;
  }
}
