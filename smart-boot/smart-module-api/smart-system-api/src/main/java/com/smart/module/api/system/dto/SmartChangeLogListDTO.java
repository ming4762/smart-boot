package com.smart.module.api.system.dto;

import com.smart.module.api.system.constants.SmartChangeLogEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 修改记录列表DTO
 * @author zhongming4762
 * 2023/8/1 9:51
 */
@Getter
@Setter
@ToString
public class SmartChangeLogListDTO implements Serializable {

    private Long id;

    private String ident;

    private Long businessId;

    private String businessData;

    private SmartChangeLogEnum operateType;

    protected Long createUserId;

    protected LocalDateTime createTime;

    protected String createBy;

    private String params1;
    private String params2;
    private String params3;

    private List<Detail> detailList;

    @Getter
    @Setter
    @ToString
    public static class Detail implements Serializable {
        private Long id;

        private Long logId;

        private String changeField;

        private String beforeValue;

        private String afterValue;
    }
}
