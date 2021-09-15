package spring.batch.springBatchPractice.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * BCHAGGRE001 Job Listener
 * @author memorykghs
 */
public class BCHAGGRE001JobListener implements JobExecutionListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(BCHAGGRE001JobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("BCHAGGRE001Job：批次開始");
        
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("BCHAGGRE001Job：批次結束");
    }

}
