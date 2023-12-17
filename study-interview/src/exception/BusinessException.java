package exception;

import lombok.Getter;

import java.util.Arrays;

/**
 * 自定义业务异常
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-05 22:27:54
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String[] messages;

    /**
     * 包含错误信息的有参构造
     *
     * @param errorCode 错误码
     * @param messages  错误信息
     */
    public BusinessException(String errorCode, String... messages) {
        super();
        this.errorCode = errorCode;
        this.messages = messages;
    }

    /**
     * 仅包含错误信息的构造方法
     *
     * @param errorCode 错误码
     */
    public BusinessException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "BusinessException {" + "errorCode = '" + errorCode + "'" + ", messages = " + Arrays.toString(messages) + "}";
    }
}
