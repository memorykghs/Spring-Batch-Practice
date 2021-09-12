package spring.batch.springBatchPractice.batch.writer;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spring.batch.springBatchPractice.dto.BookInfoDto;
import spring.batch.springBatchPractice.entity.AuthorInfo;
import spring.batch.springBatchPractice.entity.BookComment;
import spring.batch.springBatchPractice.entity.BookInfo;
import spring.batch.springBatchPractice.entity.CategoryInfo;
import spring.batch.springBatchPractice.entity.TagInfo;
import spring.batch.springBatchPractice.entity.UserInfo;
import spring.batch.springBatchPractice.exception.DataNotFoundException;
import spring.batch.springBatchPractice.repository.AuthorInfoRepo;
import spring.batch.springBatchPractice.repository.BookCommentRepo;
import spring.batch.springBatchPractice.repository.BookInfoRepo;
import spring.batch.springBatchPractice.repository.CategoryInfoRepo;
import spring.batch.springBatchPractice.repository.TagInfoRepo;
import spring.batch.springBatchPractice.repository.UserInfoRepo;

/**
 * 建立 ItemWriter
 * @author memorykghs
 */
@Component
public class BCHBORED001ItemWriter implements ItemWriter<BookInfoDto> {

    /** 書籍資料 Repo */
    @Autowired
    private BookInfoRepo bookInfoRepo;

    /** 書籍評論 Repo */
    @Autowired
    private BookCommentRepo bookCommentRepo;

    /** 標籤屬性 Repo */
    @Autowired
    private TagInfoRepo tagInfoRepo;

    /** 作者資訊 Repo */
    @Autowired
    private AuthorInfoRepo authorInfoRepo;

    /** 類別資訊 Repo */
    @Autowired
    private CategoryInfoRepo categoryInfoRepo;

    /** 使用者資訊 Repo */
    @Autowired
    private UserInfoRepo userInfoRepo;

    @Override
    public void write(List<? extends BookInfoDto> items) throws Exception {

        Timestamp now = new Timestamp(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();

        for (BookInfoDto item : items) {

            // 1. 依使用者名稱查詢ID
            String userName = item.getUpdName();
            UserInfo userInfo = userInfoRepo.findByUserName(userName).orElseThrow(() -> new DataNotFoundException("查無使用者資料"));

            // 2. 比對作者，若無資料則新增
            String authorName = item.getAuthorName();
            AuthorInfo authorInfo = authorInfoRepo.findByAuthorName(authorName).orElse(new AuthorInfo());
            if (authorInfo.getAuthorId() == null) {
                authorInfo.setAuthorName(authorName);
                authorInfo.setUpdId("SYSTEM");
                authorInfo.setUpdTime(now);

                authorInfoRepo.saveAndFlush(authorInfo);
            }

            // 3. 比對資料類別(懸疑、驚悚等)
            String category = item.getCategory();
            CategoryInfo categoryInfo = categoryInfoRepo.findByName(category).orElse(new CategoryInfo());
            if (categoryInfo.getCategoryId() == null) {
                categoryInfo.setName(category);
                categoryInfoRepo.saveAndFlush(categoryInfo);
            }

            // 4. 處理標籤資訊
            List<String> tagList = Arrays.asList(item.getTags().split("#"));
            tagList.stream().forEach(tag -> {
                TagInfo tagInfo = tagInfoRepo.findByName(tag).orElse(new TagInfo());

                if (tagInfo.getTagId() == null) {
                    tagInfo.setName(category);
                    tagInfoRepo.saveAndFlush(tagInfo);
                }

                sb.append(tagInfo.getTagId()).append(';');
            });

            // 5. 寫入BookInfo、BookComment
            BookInfo bookInfo = new BookInfo();

            String comment1 = item.getComment1();
            String comment = comment1 != null ? comment1 : item.getComment2();

            BookComment bookComment = new BookComment();
            bookComment.setBookInfo(bookInfo);
            bookComment.setComment(comment);
            bookComment.setUpdId(userInfo.getUserId());
            bookComment.setUpdTime(now);
            bookComment.setRecommend(Float.valueOf(item.getRecommend()));

            Set<BookComment> bookCommentSet = new HashSet<>();
            bookCommentSet.add(bookComment);

            bookInfo.setBookComments(bookCommentSet);
            bookInfo.setAuthorId(authorInfo.getAuthorId());
            bookInfo.setCategory(categoryInfo.getCategoryId());
            bookInfo.setTag(sb.toString());
            bookInfo.setDescription(item.getDescription());
            bookInfo.setUpdId(userInfo.getUserId());
            bookInfo.setUpdTime(now);

            bookInfoRepo.save(bookInfo);

            sb.setLength(0);
        }
    }
}
