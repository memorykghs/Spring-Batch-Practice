package spring.batch.springBatchPractice.batch.job;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;

import spring.batch.springBatchPractice.batch.listener.BCHCURSOR001JobListener;
import spring.batch.springBatchPractice.batch.listener.BCHCURSOR001ReaderListener;
import spring.batch.springBatchPractice.batch.listener.BCHCURSOR001StepListener;
import spring.batch.springBatchPractice.batch.listener.BCHCURSOR001WriterListener;
import spring.batch.springBatchPractice.entity.Car;

public class BCHCURSOR001JobConfig {

    /** JobBuilderFactory */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** StepBuilderFactory */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /** FlatFileItemWriter */
    @Autowired
    private FlatFileItemWriterBuilder<Car> flatFileItemWriterBuilder;

    /** 分批件數 */
    private static final int FETCH_SIZE = 5;

    /** Mapping 欄位名稱 */
    private static final String[] MAPPER_FIELD = new String[] { "MANUFACTURER", "TYPE", "MIN_PRICE", "PRICE" };

    /**
     * 註冊 job
     * @param step
     * @return
     */
    public Job job(@Qualifier("BCHCURSOR001Job")
    Step step) {
        return jobBuilderFactory.get("BCHCURSOR001Job")
                .start(step)
                .listener(new BCHCURSOR001JobListener())
                .build();
    }

    /**
     * Step Transaction
     * @return
     */
    @Bean
    public JpaTransactionManager jpaTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        return transactionManager;
    }

    /**
     * 註冊 Step
     * @param itemReader
     * @param process
     * @param itemWriter
     * @param jpaTransactionManager
     * @return
     */
    @Bean(name = "BCHCURSOR001Step1")
    private Step step(ItemReader<Car> itemReader, ItemWriter<Car> itemWriter,
            JpaTransactionManager jpaTransactionManager) {
        return stepBuilderFactory.get("BCHCURSOR001Step1")
                .transactionManager(jpaTransactionManager)
                .<Car, Car> chunk(FETCH_SIZE)
                .reader(itemReader)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .writer(itemWriter)
                .listener(new BCHCURSOR001ReaderListener())
                .listener(new BCHCURSOR001WriterListener())
                .listener(new BCHCURSOR001StepListener())
                .build();
    }

    /**
     * 註冊 ItemReader
     * @return
     * @throws IOException
     */
    @Bean
    private JdbcCursorItemReader<Car> itemReader() {

        return new JdbcCursorItemReaderBuilder<Car>().name("BCHCURSOR001ItemReader")
                .fetchSize(FETCH_SIZE)
                .sql("select * from STUDNET.CARS order by MANUACTURER and TYPE asc")
                .rowMapper((rs, rowNum) -> {
                    Car car = new Car();
                    car.setManufacturer(rs.getString("MANUFACTURER"));
                    car.setType(rs.getString("TYPE"));
                    car.setMinPrice(new BigDecimal(rs.getString("MIN_PRICE")));
                    car.setPrice(new BigDecimal(rs.getString("PRICE")));

                    return car;
                }).build();
    }

    /**
     * 建立 FileWriter
     * @return
     */
    @Bean
    private ItemWriter<Car> itemWriter() {

        // 1. 設定欄位對應規則
        BeanWrapperFieldExtractor<Car> beanWrapperFileExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFileExtractor.setNames(MAPPER_FIELD);

        // 2. 設定
        DelimitedLineAggregator<Car> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setFieldExtractor(beanWrapperFileExtractor);

        return flatFileItemWriterBuilder.name("BCHAGGRE001ItemWriter")
                .resource(new FileSystemResource("excel/car.csv"))
                .append(true)
                .lineAggregator(lineAggregator)
                .build();
    }

}
