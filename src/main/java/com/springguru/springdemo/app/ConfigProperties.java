package com.springguru.springdemo.app;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file.properties")
@Component
public class ConfigProperties implements Serializable{


  private static final long serialVersionUID = 1L;
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
  }

  @Override
  public boolean equals(Object object) {
    return EqualsBuilder.reflectionEquals(this, object);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

}
