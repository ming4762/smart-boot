package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.ai.dify.constants.FileTransferMethodEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 获取应用参数 响应
 * @author shizhongming
 * 2025/2/10 20:34
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyParameterResponse {

    /**
     * 开场白
     */
    @JsonProperty("opening_statement")
    private String openingStatement;

    /**
     * 开场推荐问题列表
     */
    @JsonProperty("suggested_questions")
    private List<String> suggestedQuestions;


    /**
     * 启用回答后给出推荐问题。
     */
    @JsonProperty("suggested_questions_after_answer")
    private EnabledResponse suggestedQuestionsAfterAnswer;

    /**
     * 启用语音转文本。
     */
    @JsonProperty("speech_to_text")
    private EnabledResponse speechToText;

    /**
     * 引用和归属
     */
    @JsonProperty("retriever_resource")
    private EnabledResponse retrieverResource;

    /**
     * 标记回复
     */
    @JsonProperty("annotation_reply")
    private EnabledResponse annotationReply;

    /**
     * 用户输入表单配置
     */
    @JsonProperty("user_input_form")
    private List<UserInputForm> userInputForms;

    /**
     * 上传文件配置
     */
    @JsonProperty("file_upload")
    private FileUpLoad fileUpload;

    /**
     * 系统参数
     */
    @JsonProperty("system_parameters")
    private SystemParameter systemParameters;





    @Getter
    @Setter
    public static class EnabledResponse {
        private Boolean enabled;
    }

    @Getter
    @Setter
    public static class UserInputForm {

        /**
         * 文本输入控件
         */
        @JsonProperty("text-input")
        private ControlOption textInput;

        /**
         * 段落文本输入控件
         */
        private ControlOption paragraph;

        /**
         * 下拉控件
         */
        private ControlOption select;

    }

    @Getter
    @Setter
    public static class ControlOption {
        /**
         * 控件展示标签名
         */
        private String label;
        /**
         * 控件 ID
         */
        private String variable;
        /**
         * 是否必填
         */
        private Boolean required;
        /**
         * 默认值
         */
        private String defaultValue;

        /**
         * 选项值
         */
        private List<String> options;
    }

    /**
     * 上传文件配置
     */
    @Getter
    @Setter
    public static class FileUpLoad {
        /**
         * 图片设置 当前仅支持图片类型：png, jpg, jpeg, webp, gif
         */
        private FileUploadImage image;
    }

    @Getter
    @Setter
    public static class FileUploadImage {

        /**
         * 是否开启
         */
        private Boolean enabled;
        /**
         * 图片数量限制，默认值为 3
         */
        @JsonProperty("number_limits")
        private Integer numberLimits;

        /**
         * 传递方式列表，必须选择其中一个，可选值为 "remote_url" 和 "local_file"
         */
        @JsonProperty("transfer_methods")
        private List<FileTransferMethodEnum> transferMethods;
    }

    /**
     * 系统参数
     */
    @Getter
    @Setter
    public static class SystemParameter {
        /**
         * 文档上传的大小限制，单位为兆字节（MB）。
         */
        private Long fileSizeLimit;
        /**
         * 图片文件上传的大小限制，单位为兆字节（MB）。
         */
        private Long imageFileSizeLimit;
        /**
         * 音频文件上传的大小限制，单位为兆字节（MB）。
         */
        private Long audioFileSizeLimit;
        /**
         * 视频文件上传的大小限制，单位为兆字节（MB）。
         */
        private Long videoFileSizeLimit;
    }
}
