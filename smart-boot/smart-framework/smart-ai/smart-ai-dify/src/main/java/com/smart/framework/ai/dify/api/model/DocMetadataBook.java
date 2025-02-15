package com.smart.framework.ai.dify.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 文档元数据
 * 针对图书
 * @author shizhongming
 * 2025/2/15 15:32
 * @since 5.0.0
 */
@Getter
@Setter
public class DocMetadataBook {

    /**
     * 书名 Book title
     */
    private String title;

    /**
     * 图书语言 Book language
     */
    private String language;

    /**
     * 作者 Book author
     */
    private String author;

    /**
     * 出版社 Publisher name
     */
    private String publisher;

    /**
     * 出版日期 Publication date
     */
    @JsonProperty("publication_date")
    private String publicationDate;

    /**
     * ISBN号码 ISBN number
     */
    private Long isbn;

    /**
     * 图书分类 Book category
     */
    private String category;
}
