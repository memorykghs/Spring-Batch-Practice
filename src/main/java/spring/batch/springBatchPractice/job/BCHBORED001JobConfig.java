package spring.batch.springBatchPractice.job;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaTransactionManager;

import spring.batch.springBatchPractice.dto.BookInfoDto;
import spring.batch.springBatchPractice.entity.AuthorInfo;
import spring.batch.springBatchPractice.entity.BookInfo;
import spring.batch.springBatchPractice.entity.CategoryInfo;
import spring.batch.springBatchPractice.entity.TagInfo;
import spring.batch.springBatchPractice.listener.BCHBORED001ReaderListener;
import spring.batch.springBatchPractice.listener.BCHBORED001StepListener;
import spring.batch.springBatchPractice.processor.BCH001Processor;
import spring.batch.springBatchPractice.repository.AuthorInfoRepo;
import spring.batch.springBatchPractice.repository.BookInfoRepo;
import spring.batch.springBatchPractice.repository.CategoryInfoRepo;
import spring.batch.springBatchPractice.repository.TagInfoRepo;

public class BCHBORED001JobConfig {

    /** JobBuilderFactory */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** StepBuilderFactory */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /** 書籍資料 Repo */
    @Autowired
    private BookInfoRepo bookInfoRepo;

    /** 標籤屬性 Repo */
    @Autowired
    private TagInfoRepo tagInfoRepo;

    /** 作者資訊 Repo */
    @Autowired
    private AuthorInfoRepo authorInfoRepo;

    /** 類別資訊 Repo */
    @Autowired
    private CategoryInfoRepo categoryInfoRepo;

    /** Mapping 欄位名稱 */
    private static final String[] MAPPER_FIELD = new String[] { "BookName", "Author", "Category", "Tags", "Recommend", "Description",
            "Comment1", "Comment2", "UpdDate", "UpdName" };

    /** 每批件數 */
    private static final int FETCH_SIZE = 10;

    @Bean
    public Job fileReaderJob(@Qualifier("fileReaderJob")
    Step step) {
        return jobBuilderFactory.get("fileReaderJob").start(step).listener(null).build();
    }

    /**
     * 註冊 Step
     * @param itemReader
     * @param process
     * @param itemWriter
     * @param jpaTransactionManager
     * @return
     */
    @Bean("fileReaderStep")
    private Step fileReaderStep(ItemReader<BookInfoDto> itemReader, BCH001Processor process, ItemWriter<BsrResvResidual> itemWriter,
            JpaTransactionManager jpaTransactionManager) {
        return stepBuilderFactory.get("BCH001Step1").transactionManager(jpaTransactionManager).<BookInfoDto, BookInfoDto> chunk(FETCH_SIZE)
                .reader(itemReader).faultTolerant().skip(Exception.class).skipLimit(Integer.MAX_VALUE).writer(itemWriter)
                .listener(new BCHBORED001StepListener()).listener(new BCHBORED001ReaderListener()).build();
    }

    /**
     * 建立 FileReader
     * @return
     */
    @Bean
    public ItemReader<BookInfoDto> getItemReader() {
        return new FlatFileItemReaderBuilder<BookInfoDto>().name("fileReader").resource(new ClassPathResource("/excel/書單.csv"))
                .linesToSkip(1).lineMapper(getBookInfoLineMapper()).build();
    }

    /**
     * 建立 FileWriter
     * @param entityManagerFactory
     * @return
     */
    @Bean
    public ItemWriter<BookInfoDto> getFileWriter(EntityManagerFactory entityManagerFactory) {

        Timestamp now = new Timestamp(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();

        return items -> {
            items.stream().forEach(item -> {

                // 1. 比對作者，若無資料則新增
                String authorName = item.getAuthorName();
                AuthorInfo authorInfo = authorInfoRepo.findByAuthorName(authorName).orElse(new AuthorInfo());
                if (authorInfo.getAuthorId() == null) {
                    authorInfo.setAuthorName(authorName);
                    authorInfo.setUpdId("SYSTEM");
                    authorInfo.setUpdTime(now);

                    authorInfoRepo.saveAndFlush(authorInfo);
                }
                String authorId = authorInfo.getAuthorId();

                // 2. 比對資料類別(懸疑、驚悚等)
                String category = item.getCategory();
                CategoryInfo categoryInfo = categoryInfoRepo.findByName(category).orElse(new CategoryInfo());
                if (categoryInfo.getCategoryId() == null) {
                    categoryInfo.setName(category);
                    categoryInfoRepo.saveAndFlush(categoryInfo);
                }
                String categoryId = categoryInfo.getCategoryId();

                // 3. 處理標籤資訊
                List<String> tagList = Arrays.asList(item.getTags().split("#"));
                tagList.stream().forEach(tag -> {
                    TagInfo tagInfo = tagInfoRepo.findByName(tag).orElse(new TagInfo());

                    if (tagInfo.getTagId() == null) {
                        tagInfo.setName(category);
                        tagInfoRepo.saveAndFlush(tagInfo);
                    }

                    sb.append(tagInfo.getTagId()).append(';');
                });

                // TODO 4. 依使用者名稱查詢ID

                // 5. 寫入BookInfo、BookComment
                BookInfo bookInfo = new BookInfo();
                bookInfo.setAuthorId(authorId);
                bookInfo.setCategory(categoryId);
                bookInfo.setDescription(item.getDescription());
                bookInfo.setUpdId("SYSTEM");
                bookInfo.setUpdTime(now);

            });
        };
    }

    /**
     * 建立 FileReader mapping 規則
     * @return
     */
    private LineMapper<BookInfoDto> getBookInfoLineMapper() {
        DefaultLineMapper<BookInfoDto> bookInfoLineMapper = new DefaultLineMapper<>();

        // 1. 設定每一筆資料的欄位拆分規則，預設以逗號拆分
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(MAPPER_FIELD);

        // 2. 指定 fieldSet 對應邏輯
        //        FieldSetMapper<BookInfoDto> fieldSetMapper = fieldSet -> {
        //            BookInfoDto bookInfDto = new BookInfoDto();
        //            bookInfDto.setBookName(fieldSet.readString(0));
        //            bookInfDto.setAuthor(fieldSet.readString(1));
        //            bookInfDto.setCategory(fieldSet.readString(2));
        //            bookInfDto.setTags(fieldSet.readString(3));
        //            bookInfDto.setRecommend(fieldSet.readString(4));
        //            bookInfDto.setDescription(fieldSet.readString(5));
        //            bookInfDto.setComment1(fieldSet.readString(6));
        //            bookInfDto.setComment2(fieldSet.readString(7));
        //            bookInfDto.setUpdDate(fieldSet.readString(8));
        //            bookInfDto.setUpdName(fieldSet.readString(9));

        //            bookInfDto.setBookName(fieldSet.readString("BookName"));
        //            bookInfDto.setAuthor(fieldSet.readString("Author"));
        //            bookInfDto.setCategory(fieldSet.readString("Category"));
        //            bookInfDto.setTags(fieldSet.readString("Tags"));
        //            bookInfDto.setRecommend(fieldSet.readString("Recommend"));
        //            bookInfDto.setDescription(fieldSet.readString("Description"));
        //            bookInfDto.setComment1(fieldSet.readString("Comment1"));
        //            bookInfDto.setComment2(fieldSet.readString("Comment2"));
        //            bookInfDto.setUpdDate(fieldSet.readString("UpdDate"));
        //            bookInfDto.setUpdName(fieldSet.readString("UpdName"));
        //
        //            return bookInfDto;
        //        };

        BeanWrapperFieldSetMapper<BookInfoDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BookInfoDto.class);

        bookInfoLineMapper.setLineTokenizer(tokenizer);
        bookInfoLineMapper.setFieldSetMapper(fieldSetMapper);
        return bookInfoLineMapper;
    }
}
