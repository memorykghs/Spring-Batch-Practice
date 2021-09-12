package spring.batch.springBatchPractice.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "BOOK_COMMENT")
@IdClass(value = BookCommentPK.class)
public class BookComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    @ToString.Exclude
    private BookInfo bookInfo;

    @Id
    @Column(name = "UPD_TIME")
    private Timestamp updTime;

    @Id
    @Column(name = "UPD_ID")
    private String updId;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "RECOMMEND")
    private float recommend;

}
