package spring.batch.springBatchPractice.batch.ItemReader;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.batch.item.ReaderNotOpenException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

public class BCHCURSOR001ItemReader<Car> extends JdbcCursorItemReader<Car> {

    private boolean firstRead = true;

    @Autowired
    private RowMapper<Car> rowMapper;

    @Override
    public void setRowMapper(RowMapper<Car> rowMapper) {
        super.setRowMapper(rowMapper);
    }

    @Override
    protected Car doRead() throws Exception {
        if (rs == null) {
            throw new ReaderNotOpenException("Reader must be open before it can be read.");
        }

        try {
            if (firstRead) {
                if (!rs.next()) { //Subsequent calls to next() will be executed by rowMapper
                    return null;
                }
                firstRead = false;
            }

            Car item = readCursor(rs, getCurrentItemCount());
            return item;

        } catch (SQLException se) {
            throw getExceptionTranslator().translate("Attempt to process next row failed", getSql(), se);
        }
    }

    @Override
    protected Car readCursor(ResultSet rs, int currentRow) throws SQLException {
        Car result = super.readCursor(rs, currentRow);
        setCurrentItemCount(rs.getRow());
        return result;
    }

}
