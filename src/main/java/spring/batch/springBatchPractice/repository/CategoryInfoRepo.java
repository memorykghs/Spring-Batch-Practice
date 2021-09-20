package spring.batch.springBatchPractice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.CategoryInfo;

@Repository
public interface CategoryInfoRepo extends JpaRepository<CategoryInfo, String> {

    /** 依類別名稱查詢 */
    Optional<CategoryInfo> findBySubName(String category);

}
