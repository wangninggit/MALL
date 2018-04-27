package com.mall.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties loge;


    static{
        String filename = "mall.properties";
        loge = new Properties();
        try {
            loge.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(filename)));
        } catch (IOException e) {
            logger.error("文件加载异常",e);
        }
    }

    public static String getProperty(String key){
        String value = loge.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }
    public static String getProperty(String key,String defaultValue){
        String value = loge.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return defaultValue;
        }
        return value.trim();
    }
}
