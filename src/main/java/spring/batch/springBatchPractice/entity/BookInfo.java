package spring.batch.springBatchPractice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "BOOK_INFO")
public class BookInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bookInfo")
    private Set<BookComment> bookComments;

    @Id
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
