package spring.batch.springBatchPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.TypeInfo;

@Repository
public interface TypeInfoRepo extends JpaRepository<TypeInfo, String>{

}
