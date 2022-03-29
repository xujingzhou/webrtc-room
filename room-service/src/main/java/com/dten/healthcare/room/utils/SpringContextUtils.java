package com.dten.healthcare.room.utils;

import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/12
 * @Description:
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(SpringContextUtils.class);
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        if (ObjectUtil.isEmpty(SpringContextUtils.applicationContext)) {
            SpringContextUtils.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (ObjectUtil.isEmpty(SpringContextUtils.applicationContext)) {
            throw new IllegalStateException("application Context is empty.");
        }
    }

    public static Object getBean(String name) {
        checkApplicationContext();
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) throws ClassNotFoundException {
        return getApplicationContext().getBean(name, clazz);
    }

    public static Object getBean(String beanName, String className) throws ClassNotFoundException {
        Class<?> clz = Class.forName(className);
        return getApplicationContext().getBean(beanName, clz);
    }

    public static boolean containsBean(String name) {
        return getApplicationContext().containsBean(name);
    }

    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().isSingleton(name);
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().getType(name);
    }

    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().getAliases(name);
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     */
    public static String getEnvironmentProperty(String key) {
        return getApplicationContext().getEnvironment().getProperty(key);
    }

    /**
     * 获取spring.profiles.active
     */
    public static String getActiveProfile() {
        return getApplicationContext().getEnvironment().getActiveProfiles()[0];
    }

    // 反射创建新类
    public static Object createObject(String ClassPath, Object[] Params) {
        Class<?> cls = null;
        Object obj = null;

        try {
            cls = Class.forName(ClassPath);

            if (Params != null) {
                Class[] argsClass = new Class[Params.length];
                for (int i = 0; i < Params.length; i++) {
                    argsClass[i] = Params[i].getClass();
                }
                Constructor<?> cons = cls.getConstructor(argsClass);
                obj = cons.newInstance(Params);
            } else {
                obj = cls.newInstance();
            }
        } catch (Exception e) {
            logger.info("createObject exception = {}", e.toString());
        }

        return obj;
    }
}
