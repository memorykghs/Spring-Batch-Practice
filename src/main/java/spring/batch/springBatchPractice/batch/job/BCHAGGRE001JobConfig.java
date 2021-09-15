package spring.batch.springBatchPractice.batch.job;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.sample.domain.multiline.AggregateItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaTransactionManager;

import spring.batch.springBatchPractice.batch.listener.BCHAGGRE001JobListener;
import spring.batch.springBatchPractice.batch.listener.BCHAGGRE001ReaderListener;
import spring.batch.springBatchPractice.batch.listener.BCHAGGRE001StepListener;
import spring.batch.springBatchPractice.batch.listener.BCHAGGRE001WriterListener;
import spring.batch.springBatchPractice.entity.Car;
import spring.batch.springBatchPractice.repository.CarRepo;

/**
 * BCHAGGRE001 Job Config
 * Aggregate Item Reader
 * @author memorykghs
 */
public class BCHAGGRE001JobConfig {

    /** JobBuilderFactory */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** StepBuilderFactory */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /** FlatFileItemWriter */
    @Autowired
    private FlatFileItemWriterBuilder<Car> flatFileItemWriterBuilder;

    /** CarRepo*/
    @Autowired
    private CarRepo carRepo;

    /** 每批件數 */
    private static final int FETCH_SIZE = 10;

    /** Mapping 欄位名稱 */
    private static final String[] MAPPER_FIELD = new String[] { "MANUFACTURER", "TYPE", "MIN_PRICE", "PRICE" };

    @Bean
    public Job fileReaderJob(@Qualifier("BCHAGGRE001Step")
    Step step) {
        return jobBuilderFactory.get("BCHAGGRE001Job").start(step).listener(new BCHAGGRE001JobListener()).build();
    }

    /**
     * 註冊 Step
     * @param itemReader
     * @param process
     * @param itemWriter
     * @param jpaTransactionManager
     * @return
     */
    @Bean
    @Qualifier("BCHAGGRE001Step")
    private Step fileReaderStep(ItemReader<Car> itemReader, ItemWriter<Car> itemWriter, JpaTransactionManager jpaTransactionManager) {
        return stepBuilderFactory.get("BCHAGGRE001Step")
                .transactionManager(jpaTransactionManager)
                .<Car, Car> chunk(FETCH_SIZE)
                .reader(itemReader)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .writer(itemWriter)
                .listener(new BCHAGGRE001StepListener())
                .listener(new BCHAGGRE001ReaderListener())
                .listener(new BCHAGGRE001WriterListener())
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
     * 註冊 ItemReader
     * @return
     * @throws IOException
     */
    @Bean
    private AggregateItemReader<Car> itemReader() throws IOException {

        AggregateItemReader<Car> agggrgateItemReader = new AggregateItemReader<>();

        Map<String, Direction> sortMap = new HashMap<>();
        sortMap.put("MANUFACTURER", Direction.ASC);
        sortMap.put("TYPE", Direction.ASC);

        return agggrgateItemReader;
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
