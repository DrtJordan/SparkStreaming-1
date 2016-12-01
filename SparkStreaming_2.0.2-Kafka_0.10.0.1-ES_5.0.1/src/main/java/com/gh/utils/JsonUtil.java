package com.gh.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {
	public static ObjectMapper mapper = new ObjectMapper();

	// 将 list,array,map,bean等转换为 json格式
	public static String formatJson(Object obj){
		try {
			return mapper.writeValueAsString(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//取出json中某filed
	public static String get(String json,String filed){
		JsonNode readTree = null;
		try {
			readTree = mapper.readTree(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readTree.isNull() ? null : readTree.get(filed).toString();
	}

	//获取json tree
	public static JsonNode getJsonNode(String json){
		//Person pp2 = mm.readValue(json, Person.class);  将json直接转换为bean
		JsonNode readTree = null;  						// 将json读为tree树
		try {
			readTree = mapper.readTree(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readTree;
	}

	//将json直接转换为bean
	public static <T> T getBean(String json,Class<T> cl){
		T bean = null;
		try {
			bean = mapper.readValue(json, cl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

}
