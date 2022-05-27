package com.smart.commons.core.message;

import com.smart.commons.core.exception.BaseException;
import com.smart.commons.core.exception.BusinessException;
import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.http.IHttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 后台传输工具类
 * @author jackson
 */
@Getter
@Setter
@Schema(title = "通用api接口", description = "通用api接口返回")
public class Result<T> {

    private static final long serialVersionUID = 9144229906004159463L;
    @Schema(title = "状态码", example = "200", required = true)
    private Integer code = ResultCodeEnum.SUCCESS.getCode();

    @Schema(title = "返回信息", example = "成功")
    private String message = null;

    @Schema(title = "接口返回状态", example = "true", required = true)
    private boolean success = true;

    @Schema(title = "接口返回数据")
    private T data = null;

    private static <T> Result<T> newInstance() {
        return new Result<>();
    }

    /**
     * 成功消息
     * @param code 消息码
     * @param message message
     * @param data data
     * @param <T> T
     * @return 成功消息
     */
    public static <T> Result<T> success(Integer code, String message, T data) {
        final Result<T> result = newInstance();
        result.setCode(code);
        result.setMessage(message);
        if (data == null) {
            result.setData(null);
        } else {
            Field[] fields = data.getClass().getDeclaredFields();
            if (fields.length == 0) {
                result.setData(null);
            }
        }
        result.setData(data);
        return result;
    }

    /**
     * 成功消息
     * @param message message
     * @param data data
     * @param <T> T
     * @return 成功消息
     */
    public static <T> Result<T> success(T data,String message) {
        return Result.success(ResultCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 成功消息
     * @param data data
     * @param <T> T
     * @return 成功消息
     */
    public static <T> Result<T> success(T data) {
        return Result.success(data, ResultCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 成功消息
     * @return 成功消息
     */
    public static <T> Result<T> success() {
        return Result.success(null, ResultCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 返回分页数据
     * @param rows 数据
     * @param total 总条数
     * @param <T> 泛型
     * @return 分页数据
     */
    public static <T> Result<PageData<T>> success(@NonNull List<T> rows, Long total) {
        return Result.success(new PageData<>(rows, total));
    }


    /**
     * 失败消息
     * @param code code
     * @param message message
     * @param data data
     * @param <T>  T
     * @return 失败消息
     */
    public static <T> Result<T> failure(Integer code, String message, T data) {
        final Result<T> result = newInstance();
        result.setSuccess(Boolean.FALSE);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败消息
     * @param code code
     * @param message message
     * @param <T> T
     * @return 失败消息
     */
    public static <T> Result<T> failure(Integer code, String message) {
        return Result.failure(code, message, null);
    }

    /**
     * 失败消息
     * @param message message
     * @param data data
     * @param <T> T
     * @return 失败消息
     */
    public static <T> Result<T> failure(String message, T data) {
        return Result.failure(ResultCodeEnum.FAILURE.getCode(), message, data);
    }

    /**
     * 失败消息
     * @param message message
     * @param <T> T
     * @return 失败消息
     */
    @NonNull
    public static <T> Result<T> failure(String message) {
        return Result.failure(ResultCodeEnum.FAILURE.getCode(), message);
    }

    /**
     * 失败消息
     * @param bindingResult 错误信息
     * @param <T> T
     * @return 失败消息
     */
    @NonNull
    public static <T> Result<T> failure(@NonNull BindingResult bindingResult) {
        String errorMessage = Optional.ofNullable(bindingResult.getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("参数校验失败");
        return failure(HttpStatus.BAD_REQUEST.getCode(), errorMessage);
    }

    /**
     * 错误信息
     * @param e E
     * @return 错误信息
     */
    public static Result<BaseException> failure(@NonNull BaseException e) {
        return failure(e.getCode(), e.getMessage());
    }

    public static Result<Exception> failure(@NonNull Exception e) {
        return failure(ResultCodeEnum.FAILURE.getCode(), e.toString());
    }

    /**
     * 业务异常
     * @param e 异常信息
     * @return 返回数据
     */
    public static Result<String> failure(@NonNull BusinessException e) {
        return failure(e.getCode(), e.getMessage(), e.toString());
    }

    /**
     *
     * @param status status
     * @param <T> T
     * @return 信息
     */
    public static <T> Result<T> ofStatus(IHttpStatus status) {
        return ofStatus(status, null);
    }

    public static <T> Result<T> ofStatus(IHttpStatus status, String message) {
        return ofStatus(status, message, null);
    }

    public static Result<String> ofExceptionStatus(IHttpStatus status, Exception e) {
        return ofStatus(status, e.getMessage());
    }

    public static <T> Result<T> ofStatus(IHttpStatus status, String message, T data) {
        String returnMessage = StringUtils.isEmpty(message) ? status.getMessage() : message;
        if (Objects.equals(status.getCode(), HttpStatus.OK.getCode())) {
            return success(status.getCode(), returnMessage, data);
        } else {
            return failure(status.getCode(), returnMessage, data);
        }
    }
}
