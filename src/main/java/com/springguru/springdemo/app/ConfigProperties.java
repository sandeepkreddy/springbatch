package com.springguru.springdemo.app;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "")
public class ConfigProperties {


  private String fieldNames;
  private String fieldRanges;

  public String getFieldNames() {
    return fieldNames;
  }

  public void setFieldNames(String fieldNames) {
    this.fieldNames = fieldNames;
  }

  public String getFieldRanges() {
    return fieldRanges;
  }

  public void setFieldRanges(String fieldRanges) {
    this.fieldRanges = fieldRanges;
  }


}
