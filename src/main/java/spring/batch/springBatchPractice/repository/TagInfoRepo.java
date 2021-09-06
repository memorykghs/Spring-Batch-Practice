package spring.batch.springBatchPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.TagInfo;

@Repository
public interface TagInfoRepo extends JpaRepository<TagInfo, String>{

}
