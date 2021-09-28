package spring.batch.springBatchPractice.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import spring.batch.springBatchPractice.entity.ItemInfo;

public class GENFILE001WriterListener implements ItemWriteListener<ItemInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GENFILE001WriterListener.class);

    @Override
    public void beforeWrite(List<? extends ItemInfo> items) {
        LOGGER.info("寫入資料開始");
    }

    @Override
    public void afterWrite(List<? extends ItemInfo> items) {
        LOGGER.info("寫入資料結束");
    }

    @Override
    public void onWriteError(Exception ex, List<? extends ItemInfo> items) {
        LOGGER.error("BCHGFILE001: 寫入資料失敗", ex);
    }

}
