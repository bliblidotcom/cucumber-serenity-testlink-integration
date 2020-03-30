package com.gdn.qa.util.model;

/**
 * @author yunaz.ramadhan on 3/27/2020
 */
public interface TagsReader<T> {
  String getValueFromTags(String tagName, T tags);
}
