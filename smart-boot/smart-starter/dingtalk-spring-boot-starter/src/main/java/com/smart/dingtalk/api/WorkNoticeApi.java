package com.smart.dingtalk.api;

import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.commons.validate.utils.ValidatorUtils;
import com.smart.dingtalk.constants.DingtalkApiUrlEnum;
import com.smart.dingtalk.pojo.dto.WorkNoticeAsyncSendResult;
import com.smart.dingtalk.pojo.parameter.GetAccessTokenParameter;
import com.smart.dingtalk.pojo.parameter.WorkNoticeAsyncSendParameter;
import com.smart.dingtalk.pojo.parameter.message.*;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;

/**
 * 工作通知API
 * @author shizhongming
 * 2024/4/28 16:37
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class WorkNoticeApi extends AbstractDingtalkApi {

    private final AccessSecureApi accessSecureApi;

    /**
     * 发送工作通知接口
     * <a href="https://open.dingtalk.com/document/orgapp/asynchronous-sending-of-enterprise-session-messages">参考钉钉文档</a>
     * @param parameter 参数
     * @return 工作通知发送结果
     */
    @SneakyThrows(ApiException.class)
    public WorkNoticeAsyncSendResult syncSend(WorkNoticeAsyncSendParameter parameter, GetAccessTokenParameter accessTokenParameter) {
        // 校验参数
        ValidatorUtils.validate(parameter);
        // 获取token
        DingTalkClient client = this.getOldClient(DingtalkApiUrlEnum.WORK_NOTICE);
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(parameter.getAgentId());
        if (!CollectionUtils.isEmpty(parameter.getDeptIdList())) {
            request.setDeptIdList(String.join(",", parameter.getDeptIdList()));
        }
        if (!CollectionUtils.isEmpty(parameter.getUserIdList())) {
            request.setUseridList(String.join(",", parameter.getUserIdList()));
        }
        if (parameter.getToAllUser() != null) {
            request.setToAllUser(parameter.getToAllUser());
        }
        // 设置消息
        OapiMessageCorpconversationAsyncsendV2Request.Msg message = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        message.setMsgtype(parameter.getMessageType().getType());

        switch (parameter.getMessageType()) {
            case TEXT:
                message.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
                message.getText().setContent(((TextMessageParameter)parameter.getMessageParameter()).getContent());
                break;
            case IMAGE:
                message.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
                message.getImage().setMediaId(((ImageMessageParameter)parameter.getMessageParameter()).getMediaId());
                break;
            case FILE:
                message.setFile(new OapiMessageCorpconversationAsyncsendV2Request.File());
                message.getFile().setMediaId(((FileMessageParameter)parameter.getMessageParameter()).getMediaId());
                break;
            case LINK:
                message.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
                LinkMessageParameter linkMessageParameter = (LinkMessageParameter) parameter.getMessageParameter();
                message.getLink().setTitle(linkMessageParameter.getTitle());
                message.getLink().setText(linkMessageParameter.getText());
                message.getLink().setMessageUrl(linkMessageParameter.getMessageUrl());
                message.getLink().setPicUrl(linkMessageParameter.getPicUrl());
                break;
            case MARKDOWN:
                message.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
                MarkdownMessageParameter arkMessageParameter = (MarkdownMessageParameter) parameter.getMessageParameter();
                message.getMarkdown().setTitle(arkMessageParameter.getTitle());
                message.getMarkdown().setText(arkMessageParameter.getText());
                break;
            case VOICE:
                message.setVoice(new OapiMessageCorpconversationAsyncsendV2Request.Voice());
                message.getVoice().setMediaId(((VoiceMessageParameter)parameter.getMessageParameter()).getMediaId());
                break;
            case ACTION_CARD:
                message.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
                ActionCardMessageParameter actionCardMessageParameter = (ActionCardMessageParameter) parameter.getMessageParameter();
                message.getActionCard().setTitle(actionCardMessageParameter.getTitle());
                message.getActionCard().setMarkdown(actionCardMessageParameter.getMarkdown());
                message.getActionCard().setSingleTitle(actionCardMessageParameter.getSingleTitle());
                message.getActionCard().setSingleUrl(actionCardMessageParameter.getSingleUrl());
                break;
            case OA:
                throw new UnsupportedOperationException("暂不支持OA消息");
            default:
                // do nothing
        }

        request.setMsg(message);

        OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, this.accessSecureApi.getInnerAppAccessToken(accessTokenParameter).getAccessToken());
        this.validateResponse(response);

        return JsonUtils.parse(response.getBody(), WorkNoticeAsyncSendResult.class);
    }
}
