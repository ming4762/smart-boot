package com.smart.smc.inter.qingdaoport.api.transfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.module.api.system.SysLogApi;
import com.smart.smc.inter.qingdaoport.SmartSmcQingdaoPortProperties;
import com.smart.smc.inter.qingdaoport.api.QingdaoPortCommonApi;
import com.smart.smc.inter.qingdaoport.constants.QingdaoPortUrlEnum;
import com.smart.smc.inter.qingdaoport.dto.parameter.QingdaoPortTransferApplyPushParameter;
import com.smart.smc.inter.qingdaoport.dto.parameter.QingdaoPortTransferDeleteParameter;
import com.smart.smc.inter.qingdaoport.dto.parameter.QingdaoPortTransferImoQueryParameter;
import com.smart.smc.inter.qingdaoport.dto.result.*;

import java.util.List;

/**
 * 默认实现类-云港通-智能转运平台
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 16:22
 * @since 5.0.0
 */
public class DefaultQingdaoPortTransferApiImpl extends QingdaoPortCommonApi implements QingdaoPortTransferApi {


    public DefaultQingdaoPortTransferApiImpl(SmartSmcQingdaoPortProperties properties, SysLogApi sysLogApi) {
        super(properties, sysLogApi);
    }

    /**
     * 查询智能转运平台参数
     *
     * @return 参数列表
     */
    @Override
    public List<QingdaoPortTransferParamQueryResult> queryTransferParam() {
        QingdaoPortListResult<QingdaoPortTransferParamQueryResult> result = this.doRequest(
                QingdaoPortUrlEnum.TRANSFER,
                null,
                "mtcgs/paramquery",
                false,
                new TypeReference<>() {
                }
        );
        return result.getData();
    }

    /**
     * 查询智能转运平台IMO号
     *
     * @param parameter 查询参数
     * @return IMO号列表
     */
    @Override
    public List<QingdaoPortTransferImoResult> queryTransferImo(QingdaoPortTransferImoQueryParameter parameter) {
        QingdaoPortListResult<QingdaoPortTransferImoResult> result = this.doRequest(
                QingdaoPortUrlEnum.TRANSFER,
                parameter,
                "mtcgs/imoquery",
                false,
                new TypeReference<>() {
                }
        );
        return result.getData();
    }

    /**
     * 发送转运申请
     *
     * @param parameter 发送参数
     * @return 发送结果
     */
    @Override
    public QingdaoPortTransferApplyPushResult pushTransferApply(QingdaoPortTransferApplyPushParameter parameter) {
        QingdaoPortObjectResult<QingdaoPortTransferApplyPushResult> result = this.doRequest(
                QingdaoPortUrlEnum.TRANSFER,
                parameter,
                "mtcgs/transferapplypush",
                true,
                new TypeReference<>() {
                }
        );
        return result.getData();
    }

     /**
     * 删除转运申请
     * @param parameter 删除参数
     * @return 删除结果
     */
    @Override
    public QingdaoPortTransferDeleteResult deleteTransferApply(QingdaoPortTransferDeleteParameter parameter) {
        QingdaoPortObjectResult<QingdaoPortTransferDeleteResult> result = this.doRequest(
                QingdaoPortUrlEnum.TRANSFER,
                parameter,
                "mtcgs/transfercontapush",
                true,
                new TypeReference<>() {
                }
        );
        return result.getData();
    }
}
