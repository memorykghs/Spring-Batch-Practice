package spring.batch.springBatchPractice.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

import spring.batch.springBatchPractice.dto.ItemInfoDto;

public class BCHCURSOR001ReaderListener implements ItemReadListener<ItemInfoDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BCHCURSOR001ReaderListener.class);

    @Override
    public void beforeRead() {
        // Do nothing

    }

    @Override
    public void afterRead(ItemInfoDto item) {
        // Do nothing

    }

    @Override
    public void onReadError(Exception ex) {
        LOGGER.error("BCHCURSOR001: 讀取資料失敗", ex);
    }


}
