package spring.batch.springBatchPractice.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * GENFILE001 Job Listener
 * @author 00586594
 */
public class GENFILE001JobListener implements JobExecutionListener{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GENFILE001JobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("GENFILE001: 批次開始");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("GENFILE001: 批次結束");
        
    }

}
