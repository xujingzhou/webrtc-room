package com.dten.healthcare.room.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConcurrentDateUtil {

    private static final ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static Date parse(String dateStr) throws ParseException {
        if (StringUtils.isNotEmpty(dateStr)) {
            return threadLocal.get().parse(dateStr);
        } else {
            return null;
        }
    }

    public static String format(Date date) {
        if (ObjectUtil.isNotEmpty(date)) {
            return threadLocal.get().format(date);
        } else {
            return null;
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateStringToStamp(String dateStr) throws ParseException {
        if (StrUtil.isNotEmpty(dateStr)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date simpleDate = simpleDateFormat.parse(dateStr);
            return simpleDate.getTime();
        } else {
            return -1;
        }
    }

    public static long dateToStamp(Date date) throws ParseException {
        if (ObjectUtil.isNotEmpty(date)) {
            return date.getTime();
        } else {
            return -1;
        }
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDateString(long stamp) {
        if (stamp > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(stamp);
            return simpleDateFormat.format(date);
        } else {
            return null;
        }
    }

    public static Date stampToDate(long stamp) {
        if (stamp > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new Date(stamp);
        } else {
            return null;
        }
    }

    public static Timestamp stampToTimestamp(long stamp) {
        if (stamp > 0) {
            return new Timestamp(stamp);
        } else {
            return null;
        }
    }

    public static long timestampToStamp(Timestamp timestamp) {
        if (ObjectUtil.isNotEmpty(timestamp)) {
            return timestamp.getTime();
        } else {
            return -1;
        }
    }
}
