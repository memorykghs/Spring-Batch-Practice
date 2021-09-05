package spring.batch.springBatchPractice.job;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaTransactionManager;

import cub.fxs.bch.reserve.entity.BsrResvResidual;
import spring.batch.springBatchPractice.listener.BCH001JobListener;
import spring.batch.springBatchPractice.listener.BCH001StepListener;

public class BCH001JobConfig {

    /** JobBuilderFactory */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** StepBuilderFactory */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    /** 分批件數 */
    private static final int FETCH_SIZE = 7;

    /**
     * 註冊 job
     * @param step
     * @return
     */
    public Job bch001Job(@Qualifier("BCH001Job")
    Step step) {
        return jobBuilderFactory.get("BCH001Job").start(step).listener(new BCH001JobListener()).build();
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
    @Bean(name = "BCH001Step1")
    private Step BCH001Step1(ItemReader<Map<String, Object>> itemReader, BCH001Processor process, ItemWriter<BsrResvResidual> itemWriter,
            JpaTransactionManager jpaTransactionManager) {
        return stepBuilderFactory.get("BCH001Step1")
                .transactionManager(jpaTransactionManager)
                .<Map<String, Object>, BsrResvResidual> chunk(FETCH_SIZE)
                .reader(itemReader)
                .processor(process)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .writer(itemWriter)
                .listener(new BCH001ReaderListener())
                .listener(new BCH001ProcessListener())
                .listener(new BCH001WriterListener())
                .listener(new BCH001StepListener())
                .build();
    }
    
    /**
     * 註冊 ItemReader
     * @return
     * @throws IOException
     */
    @Bean
    private RepositoryItemReader<Map<String, Object>> itemReader() throws IOException {

        List<LocalDate> args = new ArrayList<>();
        args.add(getDate());

        Map<String, Direction> sortMap = new HashMap<>();
        sortMap.put("RESV_CONF_ID", Direction.ASC);

        return new RepositoryItemReaderBuilder<Map<String, Object>>().name("itemReader")
                .pageSize(FETCH_SIZE).repository(bsrResvConfigRepo)
                .methodName("getAllBranchConfig")
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
    public ItemWriter<BsrResvResidual> insertResidualWriter(EntityManagerFactory entityManagerFactory) {
        return items -> {
            items.stream().forEach(item -> {
                bsrResvResidualRepo.saveAndFlush(item);
            });
        };
    }
    
    /**
     * 取得查詢區間
     * @return
     */
    private LocalDate getDate() {
        return LocalDate.now();
    }

}
