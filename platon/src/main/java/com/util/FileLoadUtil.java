package com.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;



public class FileLoadUtil {
	private static String propertiesFile="resources/properties/system.properties";
	private static Properties properties=new Properties();
	static{
		try {
			InputStream in = FileLoadUtil.class.getResourceAsStream("/"+propertiesFile);
	        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			properties.load(bf);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static String getValueByKey(String key){
		return properties.getProperty(key);
	}
	
	/**  
	    * 根据主键key读取主键的值value  
	    * @param filePath 属性文件路径  
	    * @param key 键名  
	    */   
	    public static String readValue(String filePath, String key) {   
	        Properties props = new Properties();   
	        try {   
	            InputStream in = new BufferedInputStream(new FileInputStream(   
	                    filePath));   
	            props.load(in);   
	            String value = props.getProperty(key);   
	            System.out.println(key +"键的值是："+ value);   
	            return value;   
	        } catch (Exception e) {   
	            e.printStackTrace();   
	            return null;   
	        }   
	    }   
	
	
}
