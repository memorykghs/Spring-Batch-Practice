package spring.batch.springBatchPractice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.TagInfo;

@Repository
public interface TagInfoRepo extends JpaRepository<TagInfo, String>{

    /** 依標籤名稱查詢 */
    Optional<TagInfo> findByName(String tag);
}
