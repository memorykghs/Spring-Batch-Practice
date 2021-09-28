package spring.batch.springBatchPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.ItemTag;
import spring.batch.springBatchPractice.entity.ItemTagPK;

@Repository
public interface ItemTagRepo extends JpaRepository<ItemTag, ItemTagPK>{

}
