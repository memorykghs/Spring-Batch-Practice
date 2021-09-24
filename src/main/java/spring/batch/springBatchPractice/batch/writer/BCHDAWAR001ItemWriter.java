package cub.fxs.bch.reserve.batch.writer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;

import cub.fxs.bch.reserve.entity.BsrResvMaster;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BCHDAWAR001ItemWriter extends FlatFileItemWriter {

    private LineAggregator<BsrResvMaster> lineAggregator;

    private LineAggregator<BsrResvMaster> headerLineAggregator = new DelimitedLineAggregator<>();

    private boolean writeHeader = true;

    private static final List<String> headerList = new ArrayList<>();
    static {
        headerList.add("預約編號");
        headerList.add("預約業務");
        headerList.add("預約日期");
        headerList.add("預約時間");
        headerList.add("預約客戶證號");
        headerList.add("分行代碼");
        headerList.add("最後維護日期");
        headerList.add("最後維護時間");
        headerList.add("最後維護者");
        headerList.add("報到狀態");
        headerList.add("非本人客戶證號");
        headerList.add("交易結果");
        headerList.add("交易行員");
        headerList.add("交易日期");
        headerList.add("資料下檔日");
    }

    @Override
    public void setLineAggregator(LineAggregator lineAggregator) {
        this.lineAggregator = lineAggregator;
        super.setLineAggregator(lineAggregator);
    }

    @Override
    public void write(List items) throws Exception {

        if (writeHeader) {
            super.setLineAggregator(headerLineAggregator);
            super.write(headerList);
            super.setLineAggregator(lineAggregator);

            writeHeader = false;
        }

        super.write(items);
    }

}
