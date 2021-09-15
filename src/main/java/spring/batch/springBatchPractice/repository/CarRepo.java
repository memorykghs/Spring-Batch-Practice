package spring.batch.springBatchPractice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.Car;

@Repository
public interface CarRepo extends JpaRepository<Car, String> {

    /** 查詢全部，依PK欄位排序 */
    List<Car> findAllOrderByManufacturerAndType();

}
