package com.melson.base.cache;

import com.melson.base.utils.SpringContextUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Service;


/**
 * Created by Nelson on 2020/6/18.
 */
@Service
public class CacheUtil {
    /**
     * 通用函数，用于获取缓存中的数据
     * @param elementKey element key value
     * @param cls
     * @param <T> 数据类型
     * @return
     */
    public <T> T GetObjectValue(String elementKey, Class<T> cls) {
        Cache cache = SpringContextUtil.getBean(CacheKey.DicCaching, Cache.class);
        Element element = cache.get(elementKey);
        if (element == null || cache.isExpired(element))
            element = cache.get(elementKey);
        return (T) element.getObjectValue();
    }

    public void Put(String elementKey,Object value){
        Cache cache = SpringContextUtil.getBean(CacheKey.DicCaching, Cache.class);
        Element element = new Element(elementKey, value);
        cache.put(element);
    }
}
