package spring.batch.springBatchPractice.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;
import spring.batch.springBatchPractice.util.DefaultIdSeqGenerator;

@Data
@Entity
@Table(name = "AUTHOR_INFO")
public class AuthorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEFAULT_SEQ")
    @GenericGenerator(name = "DEFAULT_SEQ", strategy = "spring.batch.springBatchPractice.util.DefaultIdSeqGenerator", parameters = {
            @Parameter(name = DefaultIdSeqGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = DefaultIdSeqGenerator.VALUE_PREFIX_PARAMETER, value = "A"),
            @Parameter(name = DefaultIdSeqGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d") })
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
