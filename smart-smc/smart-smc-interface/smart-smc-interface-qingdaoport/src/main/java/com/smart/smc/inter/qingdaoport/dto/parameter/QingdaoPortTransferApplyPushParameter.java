package com.smart.smc.inter.qingdaoport.dto.parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 云港通-智能转运平台-发送转运申请参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 16:27
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QingdaoPortTransferApplyPushParameter implements Serializable {

    @JsonProperty("TransferApplyDeclareHead")
    private TransferApplyDeclareHead transferApplyDeclareHead;

    @JsonProperty("TransferApplyDeclareList")
    private List<TransferApplyDeclareList> transferApplyDeclareList;


    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransferApplyDeclareHead implements Serializable {

        /**
         * 数据申报类型填写1（接口申报）
         */
        @JsonProperty("DataFlag")
        private Integer dataFlag;

        /**
         * 数据申报标志1：新增 2删除
         */
        @JsonProperty("DeclareType")
        private Integer declareType;

        /**
         * 转运申请编号（删除时必填）
         */
        @JsonProperty("TransferNo")
        private String transferNo;

        /**
         * 当前业务对应的转运航线ID
         */
        @JsonProperty("RouteId")
        private String routeId;

        /**
         * 转运申请运输方式1海运  2陆运
         */
        @JsonProperty("TrafWay")
        private String trafWay;

        /**
         * 转运申请船舶名称 -支线船名 英文
         */
        @JsonProperty("ShipName")
        private String shipName;

        /**
         * 转运申请航次
         */
        @JsonProperty("Voyage")
        private String voyage;

        /**
         * 预计离港时间
         */
        @JsonProperty("LeaveTime")
        private LocalDateTime leaveTime;

        /**
         * 转运申请申报企业代码
         */
        @JsonProperty("AgentCopCode")
        private String agentCopCode;

        /**
         * 转运申请申报企业代码
         */
        @JsonProperty("AgentCopName")
        private String agentCopName;
    }


    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransferApplyDeclareList implements Serializable {

        /**
         * 集装箱编号
         */
        @JsonProperty("ContaId")
        private String contaId;

        /**
         * 集装箱所属业务类型，详见代码说明表
         * 内贸提单不提交，一体化通关：是-出口目的港放行 否-出口内支线
         */
        @JsonProperty("CargoType")
        private Integer cargoType;

        /**
         * 进出境船舶IMO编号 – 先不传
         */
        @JsonProperty("Imo")
        private String imo;

        /**
         * 进出境运输工具航次– 先不传
         */
        @JsonProperty("RelativeVoyage")
        private String relativeVoyage;

        /**
         * 转关运输工具名称，货物类型为出口内支线时填写 支线船名
         */
        @JsonProperty("TrnShipName")
        private String trnShipName;

        /**
         * 转关运输工具航次，货物类型为出口内支线时填写 支线航次
         */
        @JsonProperty("TrnVoyage")
        private String trnVoyage;

        /**
         * 是否经停 1是  0否
         */
        @JsonProperty("StopoverFlag")
        private Integer stopoverFlag;

        /**
         * 出发地 航线选择
         */
        @JsonProperty("DepartPlace")
        private String departPlace;

        /**
         * 经停地，非经停业务该字段为空
         */
        @JsonProperty("StopoverPlace")
        private String stopoverPlace;

        /**
         * 目的地 航线选择
         */
        @JsonProperty("DestinationPlace")
        private String destinationPlace;

        /**
         * 全程单（是/否） CCA
         */
        @JsonProperty("WholeOrder")
        private String wholeOrder;

        /**
         * 箱号+航次ID trans_record_list唯一约束
         */
        @JsonIgnore
        private String ctnVoyageId;
        @JsonIgnore
        private String bkRouteId;
        /**
         * opm_bk.bk_id
         */
        @JsonIgnore
        private String bkId;
        /**
         * opm_bk.bl_no
         */
        @JsonIgnore
        private String blNo;
        /**
         * vpm_voyage.id
         */
        @JsonIgnore
        private String voyageId;
    }
}
