package spring.batch.springBatchPractice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class BCHBORED001StepListener implements StepExecutionListener{
    private static final Logger LOGGER = LoggerFactory.getLogger(BCHBORED001StepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("開始讀檔");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String msg = new StringBuilder()
                .append("BCHBORED001: 讀取csv檔筆數: ")
                .append(stepExecution.getReadCount())
                .append(", 成功筆數: ")
                .append(stepExecution.getWriteCount())
                .append(", 失敗筆數: ")
                .append(stepExecution.getSkipCount()).toString();
        LOGGER.info(msg);
        return ExitStatus.COMPLETED;
    }
}
