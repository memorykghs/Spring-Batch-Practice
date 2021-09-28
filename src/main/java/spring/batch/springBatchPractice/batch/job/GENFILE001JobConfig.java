package spring.batch.springBatchPractice.batch.job;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaTransactionManager;

import spring.batch.springBatchPractice.batch.listener.GENFILE001JobListener;
import spring.batch.springBatchPractice.batch.listener.GENFILE001ProcessListener;
import spring.batch.springBatchPractice.batch.listener.GENFILE001ReaderListener;
import spring.batch.springBatchPractice.batch.listener.GENFILE001StepListener;
import spring.batch.springBatchPractice.batch.listener.GENFILE001WriterListener;
import spring.batch.springBatchPractice.entity.ItemInfo;
import spring.batch.springBatchPractice.repository.ItemInfoRepo;

/**
 * 產檔批次
 * @author memorykghs
 */
public class GENFILE001JobConfig {

    /** JobBuilderFactory */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** StepBuilderFactory */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /** BsrResvResidualRepo */
    @Autowired
    private ItemInfoRepo itemInfoRepo;

    /** 分批件數 */
    private static final int FETCH_SIZE = 3;

    /** Mapper Field */
    private static final String[] MAPPER_FIELD = new String[] { "itemId", "itemName", "author", "type", "category", "description" };

    /** Header */
    private final String HEADER = new StringBuilder().append("作品編號").append(',').append("作品名稱").append(',').append("作者").append(',')
            .append("分類").append(',').append("作品類型").append(',').append("作品大綱").toString();

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
     * 註冊 Job
     * @param step2
     * @return
     */
    @Bean
    public Job genFileJob(@Qualifier("GENFILE001JobStep1")
    Step step) {
        return jobBuilderFactory.get("GENFILE001Job")
                .start(step)
                .listener(new GENFILE001JobListener())
                .build();
    }

    /**
     * 註冊 Step
     * @param itemReader
     * @param process
     * @param itemWriter
     * @param jpaTransactionManager
     * @return
     */
    @Bean(name = "GENFILE001Step1")
    private Step genFileStep1(ItemReader<ItemInfo> itemReader, ItemWriter<ItemInfo> itemWriter,
            JpaTransactionManager jpaTransactionManager) {

        return stepBuilderFactory.get("GENFILE001Step1")
                .transactionManager(jpaTransactionManager)
                .<ItemInfo, ItemInfo> chunk(FETCH_SIZE)
                .reader(itemReader).faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .writer(itemWriter)
                .listener(new GENFILE001ReaderListener())
                .listener(new GENFILE001ProcessListener())
                .listener(new GENFILE001WriterListener())
                .listener(new GENFILE001StepListener())
                .build();
    }

    /**
     * 註冊 ItemReader
     * @return
     * @throws IOException
     */
    @Bean
    @StepScope
    private RepositoryItemReader<ItemInfo> itemReader(@Value("#jobParameters[genFileType]")
    String genFileType) {

        List<Object> args = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        if ("month".equals(genFileType)) { // 月檔
            calendar.add(Calendar.MONTH, -1);
        }
        args.add(getStartDate(calendar));

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        args.add(new Timestamp(calendar.getTime().getTime()));

        Map<String, Direction> sortMap = new HashMap<>();
        sortMap.put("ITEM_ID", Direction.ASC);

        return new RepositoryItemReaderBuilder<ItemInfo>().name("itemReader")
                .pageSize(FETCH_SIZE)
                .repository(itemInfoRepo)
                .methodName("findAllByDateRange")
                .arguments(args)
                .sorts(sortMap)
                .build();
    }

    /**
     * 註冊 ItemWriter
     * @param entityManagerFactory
     * @return
     */
    @Bean
    private FlatFileItemWriter<ItemInfo> customFlatFileWriter() {

        String fileName = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());

        BeanWrapperFieldExtractor<ItemInfo> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(MAPPER_FIELD);

        DelimitedLineAggregator<ItemInfo> lineAggreagor = new DelimitedLineAggregator<>();
        lineAggreagor.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<ItemInfo>().name("GENFILE001FileWriter")
                .encoding("UTF-8")
                .resource(new FileSystemResource("D:/" + fileName + ".csv"))
                .append(true)
                .lineAggregator(lineAggreagor)
                .headerCallback(headerCallback -> headerCallback.write(HEADER))
                .build();
    }

    /**
     * 取得起始日期時間
     * @return
     */
    private Timestamp getStartDate(Calendar calendar) {

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Timestamp(calendar.getTime().getTime());
    }
}
