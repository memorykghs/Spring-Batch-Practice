package cub.fxs.bch.reserve.batch.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

import cub.fxs.bch.reserve.entity.BsrResvConfig;
import cub.fxs.bch.reserve.entity.BsrResvMaster;

/**
 * Process Listener
 * @author 00586594
 */
public class BCHDAWAR001ProcessListener implements ItemProcessListener<BsrResvMaster, BsrResvConfig>{
    private static final Logger LOGGER = LoggerFactory.getLogger(BCHDAWAR001ProcessListener.class);

    @Override
    public void beforeProcess(BsrResvMaster item) {
        // Do nothing
        
    }

    @Override
    public void afterProcess(BsrResvMaster item, BsrResvConfig result) {
        // Do nothing
        
    }

    @Override
    public void onProcessError(BsrResvMaster item, Exception e) {
        String msg = new StringBuilder().append("BSR-C-BCHDAWAR001:預約服務整合檔執行失敗, 預約編號: ").append(item.getResvId()).toString();
        LOGGER.error(msg, e);
    }

}
