package com.springguru.springdemo.adapter.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.springguru.springdemo.domain.Employee;

public class AccountFieldSetMapper implements FieldSetMapper<Employee> {

  @Override
  public Employee mapFieldSet(FieldSet fieldSet) throws BindException {
    Employee employee = new Employee();
    employee.setId(fieldSet.readInt("id"));
    employee.setName(fieldSet.readString("name"));
    employee.setAge(fieldSet.readInt("age"));
    employee.setAddress(fieldSet.readString("address"));
    employee.setSalary(fieldSet.readBigDecimal("salary"));
    return employee;
  }

}
