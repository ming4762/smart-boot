package com.smart.framework.ai.dify.constants;

import lombok.Getter;

/**
 * 文档类型
 * @author shizhongming
 * 2025/2/15 1:42
 * @since 5.0.0
 */
@Getter
public enum DocumentTypeEnum implements EnumValue {
    /**
     * 文档类型
     */
    BOOK("book", "图书 Book"),
    WEB_PAGE("web_page", "网页 Web page"),
    PAPER("paper", "学术论文/文章 Academic paper/article"),
    SOCIAL_MEDIA_POST("social_media_post", "社交媒体帖子 Social media post"),
    WIKIPEDIA_ENTRY("wikipedia_entry", "维基百科条目 Wikipedia entry"),
    PERSONAL_DOCUMENT("personal_document", "个人文档 Personal document"),
    BUSINESS_DOCUMENT("business_document", "商业文档 Business document"),
    IM_CHAT_LOG("im_chat_log", "即时通讯记录 Chat log"),
    SYNCED_FROM_NOTION("synced_from_notion", "Notion同步文档 Notion document"),
    SYNCED_FROM_GITHUB("synced_from_github", "GitHub同步文档 GitHub document"),
    OTHERS("others", "其他文档类型 Other document types");
    ;

    private final String value;

    private final String description;
    DocumentTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
