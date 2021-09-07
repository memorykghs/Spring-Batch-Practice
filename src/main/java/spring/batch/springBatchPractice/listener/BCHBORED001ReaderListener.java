package spring.batch.springBatchPractice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

import cub.fxs.bch.reserve.entity.BsrResvConfig;
import spring.batch.springBatchPractice.dto.BookInfoDto;

public class BCHBORED001ReaderListener implements ItemReadListener<BookInfoDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BCHBORED001ReaderListener.class);

    @Override
    public void beforeRead() {
        // Do nothing

    }

    @Override
    public void afterRead(BookInfoDto item) {
        // Do nothing

    }

    @Override
    public void onReadError(Exception ex) {
        LOGGER.error("BCHBORED001: 讀取資料失敗", ex);
    }


}
