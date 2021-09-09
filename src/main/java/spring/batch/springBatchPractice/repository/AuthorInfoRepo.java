package spring.batch.springBatchPractice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.AuthorInfo;

@Repository
public interface AuthorInfoRepo extends JpaRepository<AuthorInfo, String>{
    
    /** 依作者名稱查詢 */
    Optional<AuthorInfo> findByAuthorName(String authorName);
}
