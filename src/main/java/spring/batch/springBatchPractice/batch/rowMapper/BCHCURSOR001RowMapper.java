package spring.batch.springBatchPractice.batch.rowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import spring.batch.springBatchPractice.entity.Car;

/**
 * JdbcCursorItemReader RowMapper
 * @author memorykghs
 */
@Component
public class BCHCURSOR001RowMapper implements RowMapper<Car>{

    @Override
    public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
        Car car = new Car();
        car.setManufacturer(rs.getString("MANUFACTURER"));
        car.setType(rs.getString("TYPE"));
        car.setMinPrice(new BigDecimal(rs.getString("MIN_PRICE")));
        car.setPrice(new BigDecimal(rs.getString("PRICE")));

        return car;
    }

}
