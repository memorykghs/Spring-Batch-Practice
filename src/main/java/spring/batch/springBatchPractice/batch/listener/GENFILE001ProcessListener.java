package spring.batch.springBatchPractice.batch.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

import spring.batch.springBatchPractice.entity.ItemInfo;

/**
 * Process Listener
 * @author 00586594
 */
public class GENFILE001ProcessListener implements ItemProcessListener<ItemInfo, ItemInfo>{
    private static final Logger LOGGER = LoggerFactory.getLogger(GENFILE001ProcessListener.class);

    @Override
    public void beforeProcess(ItemInfo item) {
        // Do nothing
        
    }

    @Override
    public void afterProcess(ItemInfo item, ItemInfo result) {
        // Do nothing
        
    }

    @Override
    public void onProcessError(ItemInfo item, Exception e) {
        String msg = new StringBuilder().append("BCHGFILE001:推薦作品清單匯出失敗，作品編號: ").append(item.getItemId()).toString();
        LOGGER.error(msg, e);
    }

}
