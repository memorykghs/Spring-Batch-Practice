package spring.batch.springBatchPractice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ITEM_TAG")
@IdClass(value = ItemTagPK.class)
public class ItemTag implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ITEM_ID")
	private String itemId;

	@Id
	@Column(name = "TAG_ID")
	private String tagId;

}
