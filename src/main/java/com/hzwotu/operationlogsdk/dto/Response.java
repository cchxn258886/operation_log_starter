package com.hzwotu.operationlogsdk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;


@ApiModel(value = "统一响应对象")
public class Response<T> {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_CODE = "200";
    private static final String DEFAULT_CODE_ERROR = "500";
    public static final String BASINESS_CODE_ERROR = "BUSSINESS_ERROR";
    public static final String SUCCESS_CODE = "SUCCESS";

    @ApiModelProperty(value = "是否成功", example = "true")
    private Boolean success;
    @ApiModelProperty(value = "响应消息编码", example = DEFAULT_CODE)
    private String messageCode;
    @ApiModelProperty(value = "响应消息")
    private String message;
    @ApiModelProperty(value = "响应数据")
    private T data;
    @JsonIgnore
    private Object[] args;

    /**
     * Response Constructor
     */
    public Response() {
    }

    public Response(Boolean success, String messageCode, String message, T data) {
        this.success = success;
        this.messageCode = messageCode;
        this.message = message;
        this.data = data;
    }

    /**
     * ResultDto Constructor
     *
     * @param success
     * @param message
     * @param data
     */
    public Response(String success, String message, T data) {
        this.messageCode = success;
        this.message = message;
        this.data = data;
    }

    /**
     * ResultDto Constructor
     *
     * @param success
     * @param message
     * @param data
     * @param args
     */
    public Response(String success, String message, T data, Object... args) {
        super();
        this.messageCode = success;
        this.message = message;
        this.data = data;
        this.args = args;
    }

    public static Response success() {
        return (new Response()).code(DEFAULT_CODE).setSuccess(true);
    }

    public static Response error() {
        return (new Response()).code(DEFAULT_CODE_ERROR).setSuccess(false);
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public Response code(String code) {
        this.messageCode = code;
        return this;
    }

    public Response content(T content) {
        this.data = content;
        return this;
    }

    public Response message(String message) {
        this.message = message;
        return this;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public Response setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public static <T> Response<T> success(String code, T content) {
        Response<T> response = new Response();
        response.data = content;
        response.success = true;
        response.messageCode = code;
        return response;
    }

    public static <T> Response<T> success(String bizCode, String code, T content) {
        Response<T> response = success(code, content);
        response.messageCode = bizCode;
        return response;
    }

    public static <T> Response<T> success(T content) {
        return success(DEFAULT_CODE, content);
    }

    public static <T> Response<T> error(String message) {
        return error(DEFAULT_CODE_ERROR, message);
    }

    public static <T> Response<T> error(T data) {
        Response<T> response = new Response();
        response.data = data;
        response.success = false;
        response.messageCode = DEFAULT_CODE_ERROR;
        return response;
    }

    public static <T> Response<T> error(String code, T data) {
        Response<T> response = new Response();
        response.data = data;
        response.success = true;
        response.messageCode = code;
        return response;
    }

    public static <T> Response<T> error(String code, String message) {
        Response<T> response = new Response();
        if (code != null && !"".equals(code)) {
            response.messageCode = code;
        } else {
            response.messageCode = DEFAULT_CODE;
        }

        response.success = false;
        response.message = message;
        return response;
    }

    /**
     * 返回失败
     *
     * @param message
     * @return
     */
    public static <T> Response<T> fail(String message) {
        return error(DEFAULT_CODE, message);
    }

    /**
     * Description: whether this is non business error
     *
     * @return
     */
    @JsonIgnore

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
