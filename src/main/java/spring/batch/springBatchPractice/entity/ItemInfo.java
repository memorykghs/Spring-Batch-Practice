package spring.batch.springBatchPractice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import spring.batch.springBatchPractice.util.ItemIdSeqGenerator;

@Data
@Entity
@Table(name = "ITEM_INFO")
public class ItemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "itemInfo")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ItemComment> bookComments;

    @Id
    @GeneratedValue(generator = "custom_generator")
    @GenericGenerator(name = "custom_generator", strategy = "spring.batch.springBatchPractice.util.ItemIdentifierGenerator", parameters = {
            @Parameter(name = ItemIdSeqGenerator.VALUE_PREFIX_PARAMETER, value = "I")})
    @Column(name = "ITEM_ID")
    private String itemId;
    
    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "AUTHOR_ID")
    private String authorId;
    
    @Column(name = "TYPE")
    private String type;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UPD_ID")
    private String updId;

    @Column(name = "UPD_TIME")
    private Timestamp updTime;
}
