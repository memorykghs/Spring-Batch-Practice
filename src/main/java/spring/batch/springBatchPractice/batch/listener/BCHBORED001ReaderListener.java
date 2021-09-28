package spring.batch.springBatchPractice.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

import spring.batch.springBatchPractice.dto.ItemInfoDto;

public class BCHBORED001ReaderListener implements ItemReadListener<ItemInfoDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BCHBORED001ReaderListener.class);

    @Override
    public void beforeRead() {
    	LOGGER.info("BCHBORED001: 讀取資料開始");

    }

    @Override
    public void afterRead(ItemInfoDto item) {
    	System.out.println("==========> " + item.getItemName());

    }

    @Override
    public void onReadError(Exception ex) {
        LOGGER.error("BCHBORED001: 讀取資料失敗", ex);
    }


}
