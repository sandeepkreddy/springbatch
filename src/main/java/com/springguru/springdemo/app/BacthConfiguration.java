package com.springguru.springdemo.app;

import javax.batch.api.chunk.ItemReader;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.file.transform.RangeArrayPropertyEditor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.springguru.springdemo.adapter.batch.AccountFieldSetMapper;
import com.springguru.springdemo.adapter.batch.EmployeeProcessor;
import com.springguru.springdemo.domain.Employee;

@Configuration
@EnableBatchProcessing
public class BacthConfiguration {

  /**
   * Job configuration
   * 
   * @return
   * @throws Exception
   */
  @Bean
  public Job job() throws Exception {
    return jobBuilderFactory().get("loadJob").start(step1()).build();
  }

  @Bean
  public JobBuilderFactory jobBuilderFactory() throws Exception {
    return new JobBuilderFactory(jobRepository());
  }

  @Bean
  public JobRepository jobRepository() throws Exception {
    JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
    jobRepositoryFactoryBean.setDataSource(dataSource());
    jobRepositoryFactoryBean.setTransactionManager(batchTransactionManager());
    jobRepositoryFactoryBean.setIsolationLevelForCreate("ISOLATION_DEFAULT");
    jobRepositoryFactoryBean.afterPropertiesSet();
    return jobRepositoryFactoryBean.getObject();
  }

  @ConfigurationProperties(prefix = "")
  @Bean(name = "batchDataSource")
  @Primary
  public DataSource dataSource() {
    return DataSourceBuilder.create().type(DriverManagerDataSource.class).build();
  }

  @Bean
  public PlatformTransactionManager batchTransactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }

  /**
   * Step Configuration
   * 
   * @throws Exception
   */
  @Bean
  public Step step1() throws Exception {
    return stepBuilderFactory().get("loadToDB").<Employee, Employee>chunk(10)
        .reader(flatFileItemReaderBean()).processor(employeeProcessor()).writer(itemWriter())
        .faultTolerant().skip(FlatFileParseException.class).skipLimit(10).build();
  }

  @Bean
  public StepBuilderFactory stepBuilderFactory() throws Exception {
    StepBuilderFactory stepBuilderFactory =
        new StepBuilderFactory(jobRepository(), batchTransactionManager());
    return stepBuilderFactory;
  }

  @Bean
  public FlatFileItemReader flatFileItemReaderBean() {
    FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<>();
    flatFileItemReader.setLineMapper(lineMapper());
    flatFileItemReader.setResource(new FileSystemResource(""));
    return flatFileItemReader;
  }

  @Bean
  public LineMapper<Employee> lineMapper() {
    FixedLengthTokenizer fixedLengthTokenizer = new FixedLengthTokenizer();
    fixedLengthTokenizer.setStrict(false);

    RangeArrayPropertyEditor rangeArrayPropertyEditor = new RangeArrayPropertyEditor();
    rangeArrayPropertyEditor.setAsText(configProperties().getFieldRanges());// fieldRanges
    fixedLengthTokenizer.setNames(configProperties().getFieldNames().split(","));
    // get the string and split according to the separator xample ","
    fixedLengthTokenizer.setColumns((Range[]) rangeArrayPropertyEditor.getValue());

    DefaultLineMapper<Employee> defaultLineMapper = new DefaultLineMapper<>();
    defaultLineMapper.setLineTokenizer(fixedLengthTokenizer);
    defaultLineMapper.setFieldSetMapper(accountFieldSetMapper());
    return defaultLineMapper;
  }

  @Bean
  public ItemProcessor<Employee, Employee> employeeProcessor() {
    return new EmployeeProcessor();
  }

  @Bean
  public ItemWriter<Employee> itemWriter() {
    JdbcBatchItemWriter<Employee> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
    jdbcBatchItemWriter
        .setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
    jdbcBatchItemWriter.setSql("");
    jdbcBatchItemWriter.setDataSource(dataSource());
    return jdbcBatchItemWriter;
  }

  @Bean
  public AccountFieldSetMapper accountFieldSetMapper() {
    return new AccountFieldSetMapper();
  }

  @Bean
  public ConfigProperties configProperties() {
    return new ConfigProperties();
  }

}
