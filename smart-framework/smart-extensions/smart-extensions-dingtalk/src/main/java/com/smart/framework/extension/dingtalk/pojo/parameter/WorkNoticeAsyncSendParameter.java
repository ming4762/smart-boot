package com.smart.framework.extension.dingtalk.pojo.parameter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.extension.dingtalk.constants.DingtalkMessageTypeEnum;
import com.smart.framework.extension.dingtalk.pojo.parameter.message.AbstractMessageParameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2024/4/28 17:23
 * @since 3.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkNoticeAsyncSendParameter implements Serializable {
    @Serial
    private static final long serialVersionUID = 3835738496355334209L;

    @NotNull(message = "微应用的AgentID不能为空")
    @JsonProperty("agent_id")
    private Long agentId;

    @Size(max = 100, message = "最大用户列表长度100")
    @JsonProperty("userid_list")
    private List<String> userIdList;

    @Size(max = 20, message = "部门ID最大列表长度20")
    @JsonProperty("dept_id_list")
    private List<String> deptIdList;

    /**
     * 是否发送给企业全部用户
     */
    @JsonProperty("to_all_user")
    private Boolean toAllUser;

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    @JsonProperty("msgtype")
    private DingtalkMessageTypeEnum messageType;

    @NotNull(message = "消息内容不能为空")
    private AbstractMessageParameter messageParameter;

}
