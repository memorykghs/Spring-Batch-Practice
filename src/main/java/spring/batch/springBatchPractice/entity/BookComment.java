package spring.batch.springBatchPractice.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "COMMENT")
@IdClass(value = BookCommentPK.class)
public class BookComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "UPD_TIME")
    private Timestamp updTime;

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "BOOK_ID")
    private String bookId;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "RECOMMEND")
    private float recommend;

}
