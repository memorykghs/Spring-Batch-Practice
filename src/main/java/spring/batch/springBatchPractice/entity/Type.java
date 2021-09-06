package spring.batch.springBatchPractice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TYPE")
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TYPE_ID")
    private String typeId;

    @Column(name = "NAME")
    private String name;

}
