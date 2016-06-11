package com.springguru.springdemo.adapter.batch;

import org.springframework.batch.item.ItemProcessor;

import com.springguru.springdemo.domain.Employee;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

  @Override
  public Employee process(Employee employee) throws Exception {
    return employee;
  }

}
