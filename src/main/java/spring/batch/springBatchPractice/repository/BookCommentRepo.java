package spring.batch.springBatchPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.ItemComment;
import spring.batch.springBatchPractice.entity.ItemCommentPK;

@Repository
public interface BookCommentRepo extends JpaRepository<ItemComment, ItemCommentPK>{

}
