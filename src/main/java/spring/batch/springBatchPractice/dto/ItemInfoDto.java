package spring.batch.springBatchPractice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ItemInfoDto {

    @JsonProperty("ItemName")
    private String ItemName;

    @JsonProperty("AuthorName")
    private String authorName;

    @JsonProperty("Category")
    private String category;

    @JsonProperty("Tags")
    private String tags;

    @JsonProperty("Recommend")
    private String recommend;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Comment1")
    private String comment1;

    @JsonProperty("Comment2")
    private String comment2;

    @JsonProperty("UpdDate")
    private String updDate;

    @JsonProperty("UpdName")
    private String updName;
}
