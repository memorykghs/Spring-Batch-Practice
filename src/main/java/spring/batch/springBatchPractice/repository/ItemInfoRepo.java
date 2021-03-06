package spring.batch.springBatchPractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.ItemInfo;

@Repository
public interface ItemInfoRepo extends JpaRepository<ItemInfo, String>{

}
