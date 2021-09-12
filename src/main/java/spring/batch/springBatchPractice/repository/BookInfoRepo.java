package spring.batch.springBatchPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.BookInfo;

@Repository
public interface BookInfoRepo extends JpaRepository<BookInfo, String>{

}
