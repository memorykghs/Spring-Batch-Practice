package cub.fxs.bch.reserve.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

import cub.fxs.bch.reserve.entity.BsrResvMaster;

/**
 * BCHDAWAR001 Item Reader Listener
 * @author 00586594
 */
public class BCHDAWAR001ReaderListener implements ItemReadListener<BsrResvMaster> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BCHDAWAR001ReaderListener.class);

    @Override
    public void beforeRead() {
        // Do nothing

    }

    @Override
    public void afterRead(BsrResvMaster item) {
        // Do nothing

    }

    @Override
    public void onReadError(Exception ex) {
        LOGGER.error("BSR-C-BCHDAWAR001: 查詢資料失敗", ex);
    }

}
