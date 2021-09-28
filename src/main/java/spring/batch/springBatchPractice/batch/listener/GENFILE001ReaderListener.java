package spring.batch.springBatchPractice.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

import spring.batch.springBatchPractice.entity.ItemInfo;

/**
 * GENFILE001 Item Reader Listener
 * @author 00586594
 */
public class GENFILE001ReaderListener implements ItemReadListener<ItemInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GENFILE001ReaderListener.class);

    @Override
    public void beforeRead() {
        // Do nothing

    }

    @Override
    public void afterRead(ItemInfo item) {
        // Do nothing

    }

    @Override
    public void onReadError(Exception ex) {
        LOGGER.error("BCHGFILE001: 查詢資料失敗", ex);
    }

}
