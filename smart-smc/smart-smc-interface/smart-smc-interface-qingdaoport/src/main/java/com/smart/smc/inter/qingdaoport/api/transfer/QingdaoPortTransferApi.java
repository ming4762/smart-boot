package com.smart.smc.inter.qingdaoport.api.transfer;

import com.smart.smc.inter.qingdaoport.dto.parameter.QingdaoPortTransferApplyPushParameter;
import com.smart.smc.inter.qingdaoport.dto.parameter.QingdaoPortTransferDeleteParameter;
import com.smart.smc.inter.qingdaoport.dto.parameter.QingdaoPortTransferImoQueryParameter;
import com.smart.smc.inter.qingdaoport.dto.result.QingdaoPortTransferApplyPushResult;
import com.smart.smc.inter.qingdaoport.dto.result.QingdaoPortTransferDeleteResult;
import com.smart.smc.inter.qingdaoport.dto.result.QingdaoPortTransferImoResult;
import com.smart.smc.inter.qingdaoport.dto.result.QingdaoPortTransferParamQueryResult;

import java.util.List;

/**
 * 云港通-智能转运平台
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/12/10 16:19
 * @since 5.0.0
 */
public interface QingdaoPortTransferApi {


    /**
     * 查询智能转运平台参数
     * @return 参数列表
     */
    List<QingdaoPortTransferParamQueryResult> queryTransferParam();

    /**
     * 查询智能转运平台IMO号
     * @param parameter 查询参数
     * @return IMO号列表
     */
    List<QingdaoPortTransferImoResult> queryTransferImo(QingdaoPortTransferImoQueryParameter parameter);

    /**
     * 发送转运申请
     * @param parameter 发送参数
     * @return 发送结果
     */
    QingdaoPortTransferApplyPushResult pushTransferApply(QingdaoPortTransferApplyPushParameter parameter);
    /**
     * 删除转运申请
     * @param parameter 删除参数
     * @return 删除结果
     */
    QingdaoPortTransferDeleteResult deleteTransferApply(QingdaoPortTransferDeleteParameter parameter);
}