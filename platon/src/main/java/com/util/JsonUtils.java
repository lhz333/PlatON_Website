package com.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

public class JsonUtils {
    private static Logger logger = LogManager.getLogger(JsonUtils.class);

    protected static final PropertyFilter filter = new PropertyFilter() {
        //过滤不需要的字段
        public boolean apply(Object source, String name, Object value) {

            if(value instanceof List){
                return true;
            }

            if(value == null){
                return false;
            }
            return true;

            /*if(value instanceof List){
                if(value == null || ((List)value).size()==0){
                    return false;
                }
            }
            if(value instanceof Map){
                if(value == null || ((Map)value).isEmpty()){
                    return false;
                }
            }
            return true;*/
        }
    };


    protected static ValueFilter valueFilter = new ValueFilter() {
        @Override
        public Object process(Object obj, String s, Object v) {
            if(v==null) {
                if (obj instanceof Map) {
                    //对于map类型，无法判断v对应的类型，所以返回""
                    return "";
                } else {
                    //其他类型，默认都是POJO,则找出属性s对应的类型，如果是int，则返回0
                    try {
                        Field field = obj.getClass().getDeclaredField(s);
                        Class type = (Class)field.getGenericType();
                        if (type.getName().equals("java.lang.Integer")) {
                            return 0;
                        } else if (type.getName().equals("java.lang.Double")) {
                            return 0.00;
                        } else if (type.getName().equals("java.lang.String")) {
                            return "";
                        } else{
                            return null;
                        }
                    } catch (NoSuchFieldException e) {
                        //e.printStackTrace();
                    }
                }
            }
            return v;
        }
    };

    public static String toJsonString(Object data){
        return  JSON.toJSONString(data,
                new SerializeFilter[]{filter},
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);

       /* return  JSON.toJSONString(data,
                new SerializeFilter[]{filter,valueFilter},
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);
        */
    }

    public static Object fromJson(String jsonString) {
        Object jsonObject = JSON.parse(jsonString);
        if(jsonObject instanceof JSONArray){
            return handleJSONArray((JSONArray)jsonObject);
        }else if(jsonObject instanceof JSONObject){
            return handleJSONObject((JSONObject)jsonObject);
        }else{
            return jsonObject;
        }
    }

    private static List handleJSONArray(JSONArray jsonArray){
        List list = new ArrayList();
        for (Object object : jsonArray) {
            if(object instanceof  JSONArray ){
                list.add(handleJSONArray((JSONArray) object));
            }else if(object instanceof JSONObject){
                JSONObject jsonObject = (JSONObject) object;
                HashMap<String, Object>  map = new HashMap();
                for (Map.Entry entry : jsonObject.entrySet()) {
                    if(entry.getValue() instanceof  JSONArray){
                        map.put((String)entry.getKey(), handleJSONArray((JSONArray)entry.getValue()));
                    }else if(entry.getValue() instanceof  JSONObject){
                        map.put((String)entry.getKey(), handleJSONObject((JSONObject) entry.getValue()));
                    }else{
                        map.put((String)entry.getKey(), entry.getValue());
                    }
                }
                list.add(map);
            }else{
                list.add(object);
            }
        }
        return list;
    }

    private static HashMap<String, Object> handleJSONObject(JSONObject jsonObject){
        HashMap<String, Object> map = new HashMap();
        for (Map.Entry entry : jsonObject.entrySet()) {
            if(entry.getValue() instanceof  JSONArray){
                map.put((String)entry.getKey(), handleJSONArray((JSONArray)entry.getValue()));
            }else if(entry.getValue() instanceof  JSONObject){
                map.put((String)entry.getKey(), handleJSONObject((JSONObject) entry.getValue()));
            }else{
                map.put((String) entry.getKey(), entry.getValue());
            }
        }
        return map;
    }
}
