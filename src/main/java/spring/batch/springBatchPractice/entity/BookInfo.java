package spring.batch.springBatchPractice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import spring.batch.springBatchPractice.util.BookIdSeqGenerator;

@Data
@Entity
@Table(name = "BOOK_INFO")
public class BookInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bookInfo")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BookComment> bookComments;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
    @GenericGenerator(name = "BOOK_SEQ", strategy = "spring.batch.springBatchPractice.util.BookIdSeqGenerator", parameters = {
            @Parameter(name = BookIdSeqGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = BookIdSeqGenerator.VALUE_PREFIX_PARAMETER, value = "B"),
            @Parameter(name = BookIdSeqGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d") })
    @Column(name = "BOOK_ID")
    private String bookId;

    @Column(name = "AUTHOR_ID")
    private String authorId;
    
    @Column(name = "TYPE")
    private String type;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "TAG")
    private String tag;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "RECOMMEND")
    private float recommend;

    @Column(name = "UPD_ID")
    private String updId;

    @Column(name = "UPD_TIME")
    private Timestamp updTime;
}
