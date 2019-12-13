package com.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
 
/**
 * 系统属性工具类
 */
public class PropertiesFileUtil {
	
		private static Properties config = null;
		private static Logger logger = Logger.getLogger(PropertiesFileUtil.class);
		
		static {
			InputStream in = PropertiesFileUtil.class.getClassLoader().getResourceAsStream("system.properties");
			config = new Properties();
			try {
				config.load(in);
				in.close();
			} catch (IOException e) {
				logger.error("加载系统参数配置异常========", e);
			}
		}
	 
		/**
		 * 根据key 得到value
		 * @param key
		 * @return
		 */
		public static String get(String key) {
			try {
				String value = config.getProperty(key);
				return value;
			} catch (Exception e) {
				logger.error("系统参数配置错误========", e);
				return null;
			}
		}
}
