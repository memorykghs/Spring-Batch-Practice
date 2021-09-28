package spring.batch.springBatchPractice.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import spring.batch.springBatchPractice.dto.ItemInfoDto;

/**
 * ItemWriter Listener
 * @author memorykghs
 */
public class BCHCURSOR001WriterListener implements ItemWriteListener<ItemInfoDto> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BCHCURSOR001WriterListener.class);

    @Override
    public void beforeWrite(List<? extends ItemInfoDto> items) {
        LOGGER.info("寫入資料開始");
    }

    @Override
    public void afterWrite(List<? extends ItemInfoDto> items) {
        LOGGER.info("寫入資料結束");
    }

    @Override
    public void onWriteError(Exception ex, List<? extends ItemInfoDto> items) {
        LOGGER.error("BCHCURSOR001: 寫入資料失敗", ex);
    }

}
