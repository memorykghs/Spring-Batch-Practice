package spring.batch.springBatchPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.BookComment;
import spring.batch.springBatchPractice.entity.BookCommentPK;

@Repository
public interface BookCommentRepo extends JpaRepository<BookComment, BookCommentPK>{

}
