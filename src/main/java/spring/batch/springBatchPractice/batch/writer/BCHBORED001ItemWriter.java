package spring.batch.springBatchPractice.batch.writer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spring.batch.springBatchPractice.dto.ItemInfoDto;
import spring.batch.springBatchPractice.entity.AuthorInfo;
import spring.batch.springBatchPractice.entity.CategoryInfo;
import spring.batch.springBatchPractice.entity.ItemComment;
import spring.batch.springBatchPractice.entity.ItemInfo;
import spring.batch.springBatchPractice.entity.ItemTag;
import spring.batch.springBatchPractice.entity.TagInfo;
import spring.batch.springBatchPractice.entity.UserInfo;
import spring.batch.springBatchPractice.exception.DataNotFoundException;
import spring.batch.springBatchPractice.repository.AuthorInfoRepo;
import spring.batch.springBatchPractice.repository.CategoryInfoRepo;
import spring.batch.springBatchPractice.repository.ItemInfoRepo;
import spring.batch.springBatchPractice.repository.ItemTagRepo;
import spring.batch.springBatchPractice.repository.TagInfoRepo;
import spring.batch.springBatchPractice.repository.UserInfoRepo;

/**
 * 建立 ItemWriter
 * 
 * @author memorykghs
 */
@Component
public class BCHBORED001ItemWriter implements ItemWriter<ItemInfoDto> {

	/** 作品資料 Repo */
	@Autowired
	private ItemInfoRepo bookInfoRepo;
	
	/** 作品標籤對應 Repo */
	@Autowired
	private ItemTagRepo itemTagRepo;

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
	public void write(List<? extends ItemInfoDto> items) throws Exception {

		Timestamp now = new Timestamp(System.currentTimeMillis());

		for (ItemInfoDto item : items) {

			// 1. 依使用者名稱查詢ID
			String userName = item.getUpdName();
			UserInfo userInfo = userInfoRepo.findByUserName(userName)
					.orElseThrow(() -> new DataNotFoundException("查無使用者資料"));

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

			// 4. 寫入BookInfo、BookComment
			ItemInfo itemInfo = new ItemInfo();
			String comment1 = item.getComment1();
			String comments = comment1 != null ? comment1 : item.getComment2();

			ItemComment bookComment = new ItemComment();
			bookComment.setItemInfo(itemInfo);
			bookComment.setComments(comments);
			bookComment.setUpdId(userInfo.getUserId());
			bookComment.setUpdTime(now);

			Set<ItemComment> bookCommentSet = new HashSet<>();
			bookCommentSet.add(bookComment);

			itemInfo.setItemName(item.getItemName());
			itemInfo.setBookComments(bookCommentSet);
			itemInfo.setAuthorId(authorInfo.getAuthorId());
			itemInfo.setType("T00001");
			itemInfo.setCategory(categoryInfo.getCategoryId());
			itemInfo.setDescription(item.getDescription());
			itemInfo.setUpdId(userInfo.getUserId());
			itemInfo.setUpdTime(now);

			bookInfoRepo.save(itemInfo);

			// 5. 處理標籤資訊
			List<String> tagList = new ArrayList<>(Arrays.asList(item.getTags().split("#")));
			tagList.remove(0);
			tagList.stream().forEach(tag -> {
				TagInfo tagInfo = tagInfoRepo.findByName(tag).orElse(new TagInfo());
				System.out.println("==========> " + tagInfo);

				if (tagInfo.getTagId() == null) {
					tagInfo.setName(tag);
//					tagInfoRepo.saveAndFlush(tagInfo);
					tagInfoRepo.save(tagInfo);
				}

				ItemTag itemTag = new ItemTag();
				itemTag.setItemId(itemInfo.getItemId());
				itemTag.setTagId(tagInfo.getTagId());
				itemTagRepo.saveAndFlush(itemTag);
			});
		}
	}
}
