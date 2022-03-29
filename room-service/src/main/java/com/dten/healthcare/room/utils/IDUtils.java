package com.dten.healthcare.room.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
public class IDUtils {
    private static final Logger logger = LoggerFactory.getLogger(IDUtils.class);
    private static final byte[] lock = new byte[0];
    public static final String REQUEST_ID_KEY = "X-Request-Id";
    private static final ThreadLocal<String> requestIdThreadLocal = new ThreadLocal();

    // 位数，默认是8位
    private final static long w = 100000000;

    public static String createTimeStampID() {
        long r = 0;
        synchronized (lock) {
            r = (long) ((Math.random() + 1) * w);
        }

        return System.currentTimeMillis() + String.valueOf(r).substring(1);
    }

    public static String createRandomID(int places) {
        return RandomStringUtils.randomAlphanumeric(places);
    }

    public static String getRequestId(HttpServletRequest request) {
        String headerRequestId = null;
        String requestId;
        String parameterRequestId;
        if ((parameterRequestId = request.getParameter("X-Request-Id")) == null && (headerRequestId = request.getHeader("X-Request-Id")) == null) {
            logger.debug("Both request parameter and header didn't add x-request-id");
            requestId = UUID.randomUUID().toString();
        } else {
            requestId = parameterRequestId != null ? parameterRequestId : headerRequestId;
        }

        requestIdThreadLocal.set(requestId);
        return requestId;
    }

    public static void remove() {
        requestIdThreadLocal.remove();
    }

    public static String get() {
        return (String)requestIdThreadLocal.get();
    }

    public static String generateIncrementId(int length) {
        if (length < 14) {
            throw new IllegalArgumentException("the length is too short, it must great than 14");
        } else {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                    StringUtils.leftPad(ThreadLocalRandom.current().nextInt((int) Math.pow(10.0D, (double) (length - 14))) + "", length - 14, "0");
        }
    }

    // object列表转指定业务对象列表
    public static <T> List<T> objToList(Object obj, Class<T> cla) {
        List<T> list = new ArrayList<T>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (List<?>) obj) {
                list.add(cla.cast(o));
            }
            return list;
        }

        return null;
    }
}

