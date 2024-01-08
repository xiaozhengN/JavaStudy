package base.utils;

import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-23 23:17:49
 */
public class TimeUtils {
    public static final String DATE_MONTH = "yyyy-MM";
    public static final String DATE_FORMATTER_DASH = "yyyy-MM-dd";
    public static final String DATE_FORMATTER_SLASH = "yyyy/MM/dd";
    public static final String DATE_TIME_FORMATTER_DASH = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMATTER_TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_TIME_FORMATTER_T = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_TIME_FORMATTER_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_TIME_FORMATTER_SLASH_S = "yyyy/MM/dd HH:mm:ss.S";
    public static final String DATE_TIME_FORMATTER_UTC = "EEE MMM dd HH:mm:ss Z yyyy";
    public static final String DATE_TIME_FORMATTER_NO_SEPARATION = "yyyyMMddHHmmssSSS";
    public static final String DATE_TIME_FORMATTER_NO_MILLISECONDS = "yyyyMMddHHmmss";

    public static final DateTimeFormatter OP_DATE_MONTH = DateTimeFormatter.ofPattern(DATE_MONTH);
    public static final DateTimeFormatter OP_DATE_FORMATTER_DASH = DateTimeFormatter.ofPattern(DATE_FORMATTER_DASH);
    public static final DateTimeFormatter OP_DATE_FORMATTER_SLASH = DateTimeFormatter.ofPattern(DATE_FORMATTER_SLASH);
    public static final DateTimeFormatter OP_DATE_TIME_FORMATTER_DASH = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_DASH);
    public static final DateTimeFormatter OP_DATE_TIME_FORMATTER_TZ = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_TZ);
    public static final DateTimeFormatter OP_DATE_TIME_FORMATTER_T = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_T);
    public static final DateTimeFormatter OP_DATE_TIME_FORMATTER_SLASH = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_SLASH);
    public static final DateTimeFormatter OP_DATE_TIME_FORMATTER_SLASH_S = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_SLASH_S);
    public static final DateTimeFormatter OP_DATE_TIME_FORMATTER_UTC = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_UTC);
    public static final DateTimeFormatter OP_DATE_TIME_FORMATTER_NO_SEPARATION = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_NO_SEPARATION);
    public static final DateTimeFormatter OP_DATE_TIME_FORMATTER_NO_MILLISECONDS = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_NO_MILLISECONDS);

    public static final long ONE_DATE_TIME = 24 * 60 * 60 * 1000;

    public static final int DATE_FORMATTER_DASH_LENGTH = 10;

    public static final int MONTH_INDEX = 1;

    public static final int DAY_INDEX = 2;

    public static final int MONTH_OR_DAY_LENGTH = 2;

    public static String getNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMATTER_NO_MILLISECONDS);
        return simpleDateFormat.format(new Date());
    }

    public static long convertMillisecond(String pattern, String dateTime) {
        return LocalDateTime.parse(dateTime, getDateTimeFormatter(pattern)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String convertDateTime(String pattern, long seconds) {
        Instant instant = Instant.ofEpochMilli(seconds);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static long differDays(long time1, long time2) {
        long t = time2 - time1;
        return t / ONE_DATE_TIME;
    }

    public static long differTime(Long time1, Long time2) {
        if (time1 == null || time2 == null) {
            return 0L;
        }
        return time2 - time1;
    }

    public static boolean isEmpty(Long time) {
        return time == null || time == 0;
    }

    public static TimeBean getOneDayLongTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        Date end = calendar.getTime();
        return TimeBean.builder().startTime(start.getTime()).endTime(end.getTime()).build();
    }

    private static DateTimeFormatter getDateTimeFormatter(String pattern) {
        switch (pattern) {
            case DATE_MONTH:
                return OP_DATE_MONTH;
            case DATE_FORMATTER_DASH:
                return OP_DATE_FORMATTER_DASH;
            case DATE_FORMATTER_SLASH:
                return OP_DATE_FORMATTER_SLASH;
            case DATE_TIME_FORMATTER_DASH:
                return OP_DATE_TIME_FORMATTER_DASH;
            case DATE_TIME_FORMATTER_TZ:
                return OP_DATE_TIME_FORMATTER_TZ;
            case DATE_TIME_FORMATTER_T:
                return OP_DATE_TIME_FORMATTER_T;
            case DATE_TIME_FORMATTER_SLASH:
                return OP_DATE_TIME_FORMATTER_SLASH;
            case DATE_TIME_FORMATTER_SLASH_S:
                return OP_DATE_TIME_FORMATTER_SLASH_S;
            case DATE_TIME_FORMATTER_UTC:
                return OP_DATE_TIME_FORMATTER_UTC;
            case DATE_TIME_FORMATTER_NO_SEPARATION:
                return OP_DATE_TIME_FORMATTER_NO_SEPARATION;
            case DATE_TIME_FORMATTER_NO_MILLISECONDS:
                return OP_DATE_TIME_FORMATTER_NO_MILLISECONDS;
            default:
                return DateTimeFormatter.ofPattern(pattern);
        }
    }

    // TODO 还有几个常用方法

    @Data
    @Builder
    static class TimeBean {
        private long startTime;

        private long endTime;
    }
}
