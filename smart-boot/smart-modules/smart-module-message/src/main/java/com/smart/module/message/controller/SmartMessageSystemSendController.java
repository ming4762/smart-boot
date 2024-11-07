package com.smart.module.message.controller;

import com.github.pagehelper.Page;
import com.smart.framework.commons.core.message.PageData;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.utils.CrudPageHelper;
import com.smart.module.message.model.SmartMessageSystemSendPO;
import com.smart.module.message.pojo.dbo.SmartMessageSendMessageDO;
import com.smart.module.message.pojo.paramteter.SmartMessageMarkAsReadParameter;
import com.smart.module.message.pojo.paramteter.SmartMessageSendParameter;
import com.smart.module.message.service.SmartMessageSystemSendService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shizhongming
 * 2023/1/20 14:15
 * @since 3.0.0
 */
@RestController
@RequestMapping("/smart/message/messageSend")
public class SmartMessageSystemSendController extends BaseController<SmartMessageSystemSendService, SmartMessageSystemSendPO> {

    @PostMapping("pageCurrentUserMessage")
    @Operation(summary = "查询当前人员消息")
    public Result<PageData<SmartMessageSendMessageDO>> pageCurrentUserMessage(@RequestBody SmartMessageSendParameter parameter) {
        try (Page<SmartMessageSystemSendPO> page = this.doPage(parameter)) {
            CrudPageHelper.setPage(page);
            List<SmartMessageSendMessageDO> dataList = this.service.listCurrentSendMessage(parameter);
            return Result.success(new PageData<>(dataList, page.getTotal()));
        }
    }

    @PostMapping("markAsRead")
    @Operation(summary = "标记为已读")
    public Result<Boolean> markAsRead(@RequestBody SmartMessageMarkAsReadParameter parameter) {
        return Result.success(this.service.markAsRead(parameter));
    }
}
