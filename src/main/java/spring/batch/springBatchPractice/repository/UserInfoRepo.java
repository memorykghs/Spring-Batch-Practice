package spring.batch.springBatchPractice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.batch.springBatchPractice.entity.UserInfo;

/**
 * UserInfo Repo
 * @author memorykghs
 */
@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, String> {

    /** 依使用者名稱查尋 */
    Optional<UserInfo> findByUserName(String userName);
}
