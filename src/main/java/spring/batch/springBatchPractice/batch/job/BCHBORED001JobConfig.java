package spring.batch.springBatchPractice.batch.job;

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

import spring.batch.springBatchPractice.batch.listener.BCHBORED001JobListener;
import spring.batch.springBatchPractice.batch.listener.BCHBORED001ReaderListener;
import spring.batch.springBatchPractice.batch.listener.BCHBORED001StepListener;
import spring.batch.springBatchPractice.batch.listener.BCHBORED001WriterListener;
import spring.batch.springBatchPractice.dto.BookInfoDto;

/**
 * BCHBORED001 Job Config
 * 讀取 csv 檔案 Job
 * @author memorykghs
 */
public class BCHBORED001JobConfig {

    /** JobBuilderFactory */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** StepBuilderFactory */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /** Mapping 欄位名稱 */
    private static final String[] MAPPER_FIELD = new String[] { "BookName", "Author", "Category", "Tags", "Recommend", "Description",
            "Comment1", "Comment2", "UpdDate", "UpdName" };

    /** 每批件數 */
    private static final int FETCH_SIZE = 10;

    @Bean
    public Job fileReaderJob(@Qualifier("fileReaderStep") Step step) {
        return jobBuilderFactory.get("BCHBORED001Job")
                .start(step)
                .listener(new BCHBORED001JobListener())
                .build();
    }

    /**
     * 註冊 Step
     * @param itemReader
     * @param process
     * @param itemWriter
     * @param jpaTransactionManager
     * @return
     */
    @Bean
    @Qualifier("fileReaderStep")
    private Step fileReaderStep(ItemReader<BookInfoDto> itemReader, ItemWriter<BookInfoDto> itemWriter,
            JpaTransactionManager jpaTransactionManager) {
        return stepBuilderFactory.get("BCH001Step1")
                .transactionManager(jpaTransactionManager)
                .<BookInfoDto, BookInfoDto> chunk(FETCH_SIZE)
                .reader(itemReader).faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .writer(itemWriter)
                .listener(new BCHBORED001StepListener())
                .listener(new BCHBORED001ReaderListener())
                .listener(new BCHBORED001WriterListener())
                .build();
    }
    
    /**
     * Step Transaction
     * @return
     */
    @Bean
    public JpaTransactionManager jpaTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        return transactionManager;
    }

    /**
     * 建立 FileReader
     * @return
     */
    @Bean
    public ItemReader<BookInfoDto> getItemReader() {
        return new FlatFileItemReaderBuilder<BookInfoDto>().name("fileItemReader")
                .resource(new ClassPathResource("excel/書單.csv"))
                .encoding("UTF-8")
                .linesToSkip(1)
                .lineMapper(getBookInfoLineMapper())
                .build();
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
