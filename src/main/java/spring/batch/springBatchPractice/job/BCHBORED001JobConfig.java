package spring.batch.springBatchPractice.job;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;

import spring.batch.springBatchPractice.listener.BCH001StepListener;

public class BCHBORED001JobConfig {
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Bean
    public Job fileReaderJob(@Qualifier("fileReaderJob") Step step) {
        return jobBuilderFactory.get("fileReaderJob")
                .start(step)
                .listener(null)
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

}
