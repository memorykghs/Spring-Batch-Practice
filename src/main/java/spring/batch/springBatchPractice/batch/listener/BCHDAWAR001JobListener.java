package cub.fxs.bch.reserve.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * BCHDAWAR001 Job Listener
 * @author 00586594
 */
public class BCHDAWAR001JobListener implements JobExecutionListener{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BCHDAWAR001JobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("BSR-C-BCHDAWAR001: 批次開始");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("BSR-C-BCHDAWAR001: 批次結束");
        
    }

}
