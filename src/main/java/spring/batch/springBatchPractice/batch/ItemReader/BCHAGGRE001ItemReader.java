package spring.batch.springBatchPractice.batch.ItemReader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import spring.batch.springBatchPractice.entity.Car;
import spring.batch.springBatchPractice.repository.CarRepo;

/**
 * AggregateItemReader
 * @author memorykghs
 */
public class BCHAGGRE001ItemReader implements ItemReader<Car> {

    @Autowired
    private CarRepo carRepo;

    @Override
    public Car read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        return null;
    }

}
