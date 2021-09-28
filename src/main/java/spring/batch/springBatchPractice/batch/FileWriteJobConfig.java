package cub.fxs.bch.reserve.batch.job;

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

import cub.fxs.bch.reserve.batch.listener.BCHDAWAR001JobListener;
import cub.fxs.bch.reserve.batch.listener.BCHDAWAR001ProcessListener;
import cub.fxs.bch.reserve.batch.listener.BCHDAWAR001ReaderListener;
import cub.fxs.bch.reserve.batch.listener.BCHDAWAR001StepListener;
import cub.fxs.bch.reserve.batch.listener.BCHDAWAR001WriterListener;
import cub.fxs.bch.reserve.entity.BsrResvMaster;
import cub.fxs.bch.reserve.repository.BsrResvMasterRepo;

public class FileWriteJobConfig {

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
    public Job genFileJob(@Qualifier("FileWriteStep1")
    Step step) {
        return jobBuilderFactory.get("FileWriteJob")
                .start(step)
                .listener(new BCHDAWAR001JobListener())
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
    @Bean(name = "FileWriteStep1")
    private Step genFileStep1(ItemReader<ItemInfo> itemReader, ItemWriter<ItemInfo> itemWriter,
            JpaTransactionManager jpaTransactionManager) {

        return stepBuilderFactory.get("FileWriteStep1")
                .transactionManager(jpaTransactionManager)
                .<ItemInfo, ItemInfo> chunk(FETCH_SIZE)
                .reader(itemReader).faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .writer(itemWriter)
                .listener(new BCHDAWAR001ReaderListener())
                .listener(new BCHDAWAR001ProcessListener())
                .listener(new BCHDAWAR001WriterListener())
                .listener(new BCHDAWAR001StepListener()).build();
    }

    /**
     * 註冊 ItemReader
     * @return
     * @throws IOException
     */
    @Bean
    @StepScope
    private RepositoryItemReader<BsrResvMaster> itemReader(@Value("#jobParameters[genFileType]")
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

        return new RepositoryItemReaderBuilder<BsrResvMaster>().name("itemReader")
                .pageSize(FETCH_SIZE)
                .repository(bsrResvMasterRepo)
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
    private FlatFileItemWriter<BsrResvMaster> customFlatFileWriter() {

        String fileName = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());

        BeanWrapperFieldExtractor<BsrResvMaster> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(MAPPER_FIELD);

        DelimitedLineAggregator<BsrResvMaster> lineAggreagor = new DelimitedLineAggregator<>();
        lineAggreagor.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<BsrResvMaster>().name("BCHDAWAR001FileWriter")
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
