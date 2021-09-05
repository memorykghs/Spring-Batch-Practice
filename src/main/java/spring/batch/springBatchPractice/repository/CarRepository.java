package spring.batch.springBatchPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    
}
