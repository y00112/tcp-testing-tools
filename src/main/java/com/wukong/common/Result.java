package com.wukong.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Result 结果返回类
**/

public class Result {
    /**
     * 是否成功
    **/
    private Boolean isSuccess;
    /**
     * 错误信息
     **/
    private String errorMsg;
    /**
     * 请求状态 200-成功 400-失败
     **/
    private Integer status;
    /**
     * 当前时间戳
     **/
    private Long timestamp;
    /**
     * 返回结果
     **/
    private Object data;

    public static Result success() {
        return new Result(true, null, 200, System.currentTimeMillis(),null);
    }

    public static Result success(Object data) {
        return new Result(true, null, 200,System.currentTimeMillis(),data);
    }

    public static Result success(List<?> data) {
        return new Result(true, null, 200,System.currentTimeMillis(),data);
    }

    public static Result error(String errorMsg) {
        return new Result(false, errorMsg, 500,System.currentTimeMillis(),null);
    }
}
