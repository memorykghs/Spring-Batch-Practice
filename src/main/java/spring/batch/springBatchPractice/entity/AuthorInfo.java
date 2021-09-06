package spring.batch.springBatchPractice.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "AUTHOR_INFO")
public class AuthorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "AUTHOR_ID")
    private String authorId;

    @Column(name = "AUTHOR_NAME")
    private String authorName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UPD_ID")
    private String updId;

    @Column(name = "UPD_TIME")
    private Timestamp updTime;

}
