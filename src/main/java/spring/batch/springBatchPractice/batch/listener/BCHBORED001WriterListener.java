package spring.batch.springBatchPractice.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import spring.batch.springBatchPractice.dto.BookInfoDto;

/**
 * ItemWriter Listener
 * @author memorykghs
 */
public class BCHBORED001WriterListener implements ItemWriteListener<BookInfoDto> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BCHBORED001WriterListener.class);

    @Override
    public void beforeWrite(List<? extends BookInfoDto> items) {
        LOGGER.info("寫入資料開始");
    }

    @Override
    public void afterWrite(List<? extends BookInfoDto> items) {
        LOGGER.info("寫入資料結束");
    }

    @Override
    public void onWriteError(Exception ex, List<? extends BookInfoDto> items) {
        LOGGER.error("BCHBORED001: 寫入資料失敗", ex);
    }

}
