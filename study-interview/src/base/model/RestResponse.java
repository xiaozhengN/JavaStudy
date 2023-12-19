package base.model;

import lombok.Data;

/**
 * RestResponse
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-20 1:44:35
 */
@Data
public class RestResponse<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 数据体
     */
    private T data;

    /**
     * 无参构造
     */
    public RestResponse() {}

    /**
     * 有参构造
     *
     * @param code 状态码
     * @param data 数据体
     */
    public RestResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 有参构造
     *
     * @param code 状态码
     */
    public RestResponse(int code) {
        this.code = code;
    }

    /**
     * 返回成功
     *
     * @param data 数据体
     * @return RestResponse
     * @param <T> 泛型
     */
    public static <T> RestResponse<T> ok(T data) {
        return new RestResponse<>(200, data);
    }

    /**
     * 指定错误码
     *
     * @param code 错误码
     * @return RestResponse
     */
    public static RestResponse fail(int code) {
        return new RestResponse(code);
    }

    /**
     * 返回失败
     *
     * @return RestResponse
     */
    public static RestResponse fail() {
        return new RestResponse(500);
    }
}
