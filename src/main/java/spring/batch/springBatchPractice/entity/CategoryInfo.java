package spring.batch.springBatchPractice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "CATEGORY")
public class CategoryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CATEGORY_ID")
    private String categoryId;

    @Column(name = "NAME")
    private String name;

}
