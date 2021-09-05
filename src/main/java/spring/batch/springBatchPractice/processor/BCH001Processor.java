package spring.batch.springBatchPractice.processor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

public class BCH001Processor implements ItemProcessor<Map, BsrResvResidual> {

    @Override
    public BsrResvResidual process(Map item) throws Exception {

        String resvTime = (String) item.get("RESV_TIME");
        String resvHour = resvTime.substring(0, 2);
        String resvMinute = resvTime.substring(2);

        // 塞入 BsrResvResidual
        BsrResvResidual bsrResvResidual = new BsrResvResidual();
        bsrResvResidual.setBranchId((String) item.get("BRANCH_ID"));
        bsrResvResidual.setCounterType((String) item.get("COUNTER_TYPE"));
        bsrResvResidual.setResidual((BigDecimal) item.get("QTY"));
        bsrResvResidual.setResvTime(handleDate(resvHour, resvMinute));

        return bsrResvResidual;
    }
}
