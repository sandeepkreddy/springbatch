package com.springguru.springdemo.adapter.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.springguru.springdemo.domain.Employee;

public class AccountFieldSetMapper implements FieldSetMapper<Employee> {

  @Override
  public Employee mapFieldSet(FieldSet arg0) throws BindException {
    return null;
  }

}
