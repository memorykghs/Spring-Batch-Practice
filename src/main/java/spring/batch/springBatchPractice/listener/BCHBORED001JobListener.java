package spring.batch.springBatchPractice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class BCHBORED001JobListener implements JobExecutionListener{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BCHBORED001JobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("BCHBORED001Job：批次開始");
        
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("BCHBORED001Job：批次結束");
    }

}
