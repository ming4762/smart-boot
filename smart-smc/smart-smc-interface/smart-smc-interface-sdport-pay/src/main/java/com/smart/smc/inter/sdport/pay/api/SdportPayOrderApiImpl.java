package com.smart.smc.inter.sdport.pay.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.freemarker.engine.TemplateEngine;
import com.smart.framework.freemarker.template.SmartClassPathTemplateElement;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.SysParameterApi;
import com.smart.smc.inter.sdport.pay.constants.NotifyUrlEnum;
import com.smart.smc.inter.sdport.pay.constants.PayResultStatusEnum;
import com.smart.smc.inter.sdport.pay.constants.SdportPayUrlEnum;
import com.smart.smc.inter.sdport.pay.exception.SdportPayException;
import com.smart.smc.inter.sdport.pay.exception.SdportPayUnRegisterException;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayCreateOrderInterfaceParameter;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayCreateOrderParameter;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayQueryOrderParameter;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayWakeupCashierParameter;
import com.smart.smc.inter.sdport.pay.pojo.result.OrderPayCreateOrderResult;
import com.smart.smc.inter.sdport.pay.pojo.result.PayQueryOrderResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author shizhongming
 * 2024/10/29 13:44
 * @since 1.0.0
 */
@Component
@Slf4j
public class SdportPayOrderApiImpl extends CommonApi implements SdportPayOrderApi {

    /**
     * 前台通知地址
     */
    private static final String PAY_RETURN_URL = "PAY_RETURN_URL";

    private final TemplateEngine templateEngine;

    public SdportPayOrderApiImpl(SysLogApi sysLogApi, SysParameterApi sysParameterApi, TemplateEngine templateEngine) {
        super(sysLogApi, sysParameterApi);
        this.templateEngine = templateEngine;
    }

    /**
     * 收银台下单
     * 此接口用于客户在业务平台发起付款时业务平台在山港云付平台预下单使用，预下单完成后调用唤起收银台接口让用户跳转至收银台。
     *
     * @param parameter 参数
     * @return 结果
     */
    @Override
    public OrderPayCreateOrderResult createOrder(PayCreateOrderParameter parameter) {
        PayCreateOrderInterfaceParameter interfaceParameter = new PayCreateOrderInterfaceParameter();
        BeanUtils.copyProperties(parameter, interfaceParameter);
        interfaceParameter.setTransType("2001");

        //后台通知地址
        interfaceParameter.setPayNotifyUrl(this.getNotifyUrl(NotifyUrlEnum.PAY_NOTIFY_URL));
        //前台通知地址
        String payReturnUrl = sysParameterApi.getParameter(PAY_RETURN_URL);
        interfaceParameter.setPayReturnUrl(payReturnUrl);

        try {
            return this.doRequest(SdportPayUrlEnum.CREATE_ORDER, interfaceParameter, new TypeReference<>() {
            });
        } catch (SdportPayException e) {
            if (e.getResult() != null && PayResultStatusEnum.USER_NOT_FOUND.getCode().equals(e.getResult().getStatus())) {
                // 用户不存在，注册
                throw new SdportPayUnRegisterException(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    /**
     * 唤起收银台
     * 本接口用于业务平台在调用收银台下单接口成功后唤起统一收银台使用，收银台支持 PC、H5、APP，用户跳转收银台选择支付方式进行付款，本接口为 form 表单提交
     *
     * @param parameter 参数
     * @return 结果
     */
    @Override
    public String wakeupCashier(PayWakeupCashierParameter parameter) {
        List<Map<String, String>> formData = JsonUtils.parse(JsonUtils.toJsonString(parameter), new TypeReference<Map<String, String>>() {
                }).entrySet().stream()
                .filter(item -> StringUtils.hasText(item.getValue()))
                .map(item -> Map.of(
                        "name", item.getKey(),
                        "value", item.getValue()
                )).toList();

        String wakeupCashierHtml = this.templateEngine.processToString(
                new SmartClassPathTemplateElement("templates/sdportPay/PayWakeupCashier.ftl"),
                Map.of(
                        "submitUrl", this.getRequestUrl(SdportPayUrlEnum.WAKEUP_CASHIER),
                        "parameterList", formData
                )
        );
        log.debug("唤起收银台，html：" + wakeupCashierHtml);
        return wakeupCashierHtml;
    }

    /**
     * 订单取消
     * 本接口用于将未支付订单置成取消状态
     *
     * @param batchNo 批次号
     * @return 是否取消成功
     */
    @Override
    public boolean cancelOrder(String batchNo) {
        return this.doRequest(SdportPayUrlEnum.CREATE_ORDER, Map.of("batchNo", batchNo), new TypeReference<>() {
        });
    }

    /**
     * 订单结果查询
     * 本接口用于业务平台查询订单信息使用，可用于查询支付方式和支付状态等数据，可在系统掉单时主动补单时使用。
     *
     * @param parameter 参数
     * @return 结果
     */
    @Override
    public PayQueryOrderResult queryOrder(PayQueryOrderParameter parameter) {
        return this.doRequest(SdportPayUrlEnum.QUERY_ORDER, parameter, new TypeReference<>() {
        });
    }
}
