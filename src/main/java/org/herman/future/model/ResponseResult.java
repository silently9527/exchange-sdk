package org.herman.future.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;

/**
 * @author : wangwanlu
 * @since : 2020/3/25, Wed
 **/
public class ResponseResult {

    private int code;

    private String msg;

    public ResponseResult() {
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE).append("code", code)
                .append("msg", msg).toString();
    }
}
