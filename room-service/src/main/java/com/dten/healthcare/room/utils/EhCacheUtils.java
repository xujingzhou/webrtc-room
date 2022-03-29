package com.dten.healthcare.room.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.dten.healthcare.room.common.constant.CacheEnum;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.util.*;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/30
 * @Description:
 */
public class EhCacheUtils {
    private final static Logger logger = LoggerFactory.getLogger(EhCacheUtils.class);
    private final static URL PATH = new ClassPathResource("ehcache.xml").getUrl();
    public final static String CACHE = CacheEnum.CACHE.getName();
    private CacheManager manager;
    private static EhCacheUtils ehCache;

    public static EhCacheUtils getInstance() {
        try {
            if (ObjectUtil.isEmpty(ehCache)) {
                ehCache = new EhCacheUtils(PATH);
            }

        } catch (Exception e) {
            logger.error("EhCacheUtil: get instance exception = {}", e.toString());
        }

        return ehCache;
    }

    private EhCacheUtils(URL path) {
        try {
            manager = CacheManager.create(path);
            logger.info("EhCacheUtil: ehcache.xml path = {}", path);

        } catch (Exception e) {
            logger.error("EhCacheUtil: get ehcache.xml exception = {}", e.toString());
        }
    }

    public Cache getCache(String cacheName) {
        return manager.getCache(cacheName);
    }

    public void put(String cacheName, String key, Object value) {
        try {
            Cache cache = getCache(cacheName);
            if (ObjectUtil.isNotEmpty(cache)) {
                cache.remove(key);
                Element element = new Element(key, value);
                cache.put(element);
                logger.info("EhCacheUtil: put to cacheName = {}, key = {}, element = {}", cacheName, key, JSON.toJSONString(value));
            }

        } catch (Exception e) {
            logger.error("EhCacheUtil: put cache exception = {}", e.toString());
        }
    }

    public void putAndExpireTime(String cacheName, String key, Object value, int expireTime) {
        try {
            Cache cache = getCache(cacheName);
            if (ObjectUtil.isNotEmpty(cache)) {
                cache.remove(key);
                Element element = new Element(key, value);
                element.setTimeToLive(expireTime);
                cache.put(element);
                logger.info("EhCacheUtil: put to cacheName = {}, key = {}, element = {}", cacheName, key, JSON.toJSONString(value));
            }

        } catch (Exception e) {
            logger.error("EhCacheUtil: put cache exception = {}", e.toString());
        }
    }

    public void putAll(String cacheName, List<Element> elements) {

        try {
            Cache cache = getCache(cacheName);
            cache.putAll(elements);
        } catch (Exception e) {
            logger.error("EhCacheUtil: putAll exception = {}", e.toString());
        }
    }

    public Object get(String cacheName, String key) {
        try {
            Cache cache = getCache(cacheName);
            Element element = cache.get(key);
            if (ObjectUtil.isNotEmpty(element)) {
                logger.info("EhCacheUtil: get cacheName = {}, key = {}, element = {}", cacheName, key, JSON.toJSONString(element.getObjectValue()));
                return element.getObjectValue();
            } else {
                logger.info("EhCacheUtil: cache.get(key) is empty.  cacheName = {}, key = {}", cacheName, key);
                return null;
            }

        } catch (Exception e) {
            logger.error("EhCacheUtil: get cache exception = {}", e.toString());
            return null;
        }
    }

    public List<Object> getAllValuesByCacheName(String cacheName) {
        List<Object> results = new ArrayList<Object>();
        try {
            Cache cache = getCache(cacheName);
            for (Object key : cache.getKeys()) {
                Element element = cache.get(key);
                if (ObjectUtil.isNotEmpty(element)) {
                    results.add(element.getObjectValue());
                } else {
                    logger.error("EhCacheUtil: getAllValuesByCacheName() element is empty. cacheName = {}, key = {}, element = {}", cacheName, key.toString(), JSON.toJSONString(element));
                    return null;
                }
            }

            return results;
        } catch (Exception e) {
            logger.error("EhCacheUtil: getAllValuesByCacheName exception = {}", e.toString());
            return null;
        }
    }

    public boolean remove(String cacheName, String key) {
        boolean result = false;
        try {
            Cache cache = getCache(cacheName);
            result = cache.remove(key);
            logger.info("EhCacheUtil: remove cacheName = {}, key = {}", cacheName, key);

        } catch (Exception e) {
            logger.error("EhCacheUtil: remove cache exception = {}", e.toString());
        }

        return result;
    }

    public void removeAll(String cacheName) {
        try {
            getCache(cacheName).removeAll();
            logger.info("EhCacheUtil: removeAll cacheName = {}", cacheName);

        } catch (Exception e) {
            logger.error("EhCacheUtil: removeAll cache exception = {}", e.toString());
        }
    }

    public Map<String, String> getConfigurations(String cacheName) {
        Map<String, String> results = new HashMap<String, String>();
        Cache cache = getCache(cacheName);
        CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
        MemoryStoreEvictionPolicy policy = cacheConfiguration.getMemoryStoreEvictionPolicy();
        results.put("timeToIdleSeconds", String.valueOf(cacheConfiguration.getTimeToIdleSeconds()));
        results.put("timeToLiveSeconds", String.valueOf(cacheConfiguration.getTimeToLiveSeconds()));
        results.put("maxElementsInMemory", String.valueOf(cacheConfiguration.getMaxElementsInMemory()));
        results.put("policy", policy.toString());

        return results;
    }

    public void shutdown() {
        if (ObjectUtil.isNotEmpty(manager)) {
            manager.shutdown();
        }
    }
}
