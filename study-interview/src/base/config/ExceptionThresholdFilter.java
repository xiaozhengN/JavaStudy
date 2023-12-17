package base.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-18 1:27:25
 */
public class ExceptionThresholdFilter extends AbstractMatcherFilter<ILoggingEvent> {
    /**
     * 异常类
     */
    private String exceptionClazz;

    /**
     * 日志等级
     */
    private Level level;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!this.isStarted()) {
            return FilterReply.NEUTRAL;
        }
        if (!event.getLevel().isGreaterOrEqual(this.level)) {
            return FilterReply.DENY;
        }
        if (exceptionClazz == null || "".equals(exceptionClazz)) {
            return FilterReply.NEUTRAL;
        }
        return exceptionClazz.equals(event.getThrowableProxy().getClassName()) ? this.onMatch : this.onMismatch;
    }

    /**
     * 设置日志等级
     *
     * @param level 日志等级
     */
    public void setLevel(String level) {
        this.level = Level.toLevel(level);
    }

    /**
     * 设置异常类
     *
     * @param exceptionClazz 异常类
     */
    public void setExceptionClazz(String exceptionClazz) {
        this.exceptionClazz = exceptionClazz;
    }

    /**
     * 启动日志
     */
    public void start() {
        if (this.level != null) {
            super.start();
        }
    }
}
