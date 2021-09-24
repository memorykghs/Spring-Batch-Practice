package cub.fxs.bch.reserve.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import cub.fxs.bch.reserve.entity.BsrResvMaster;

public class BCHDAWAR001WriterListener implements ItemWriteListener<BsrResvMaster> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BCHDAWAR001WriterListener.class);

    @Override
    public void beforeWrite(List<? extends BsrResvMaster> items) {
        LOGGER.info("寫入資料開始");
    }

    @Override
    public void afterWrite(List<? extends BsrResvMaster> items) {
        LOGGER.info("寫入資料結束");
    }

    @Override
    public void onWriteError(Exception ex, List<? extends BsrResvMaster> items) {
        LOGGER.error("BSR-C-BCHDAWAR001: 寫入資料失敗", ex);
    }

}
