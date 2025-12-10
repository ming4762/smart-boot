package com.smart.smc.inter.qingdaoport.constants;

import lombok.Getter;
import lombok.Setter;

/**
 * 云港通-智能转运平台-验证状态枚举
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 16:30
 * @since 5.0.0
 */
@Getter
@Setter
public enum QingdaoPortTransferVerifyStatusEnum {

    Z("表头数据项填写不完整", "整体退回"),
    Y("未找到航线备案信息", "整体退回"),
    X("未找到船舶备案信息", "整体退回"),
    W("船舶MMSI或IMO未维护", "整体退回"),
    V("未核销数据超过海关允许范围", "整体退回"),
    I("转运申请删除与申报主体不一致", "整体退回"),
    H("通用（整船）", "整体退回"),
    T("申报业务类型超范围", "按箱退回"),
    S("未找到当前航线对应的监管场所信息", "按箱退回"),
    L("未找到转运航线对应的海关现场代码", "按箱退回"),
    M("集装箱对应舱单的卸货地超出转运申请配置的港口范围", "按箱退回"),
    N("集装箱对应舱单的申报地海关超出转运申请配置的海关代码范围", "按箱退回"),
    O("未找到对应的舱单信息", "按箱退回"),
    P("集装箱数据项填写不完整", "按箱退回"),
    Q("集装箱出发地、经停地、目的地在转运航线中未备案", "按箱退回"),
    R("集装箱未放行", "按箱退回"),
    E("已存在（新增箱子）", "按箱退回"),
    A("不存在（删除箱子)", "按箱退回"),
    U("通用（集装箱）", "按箱退回"),
    B("集装箱验证通过", "成功")

    ;

    private final String remark;

    private final String type;

    QingdaoPortTransferVerifyStatusEnum(String remark, String type) {
        this.remark = remark;
        this.type = type;
    }
}
