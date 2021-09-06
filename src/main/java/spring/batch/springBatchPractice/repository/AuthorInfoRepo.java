package spring.batch.springBatchPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.AuthorInfo;

@Repository
public interface AuthorInfoRepo extends JpaRepository<AuthorInfo, String>{

}
