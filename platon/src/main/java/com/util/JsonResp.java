package com.util;

public class JsonResp<T> {
	
	private T data; //返回数据
	private int code=0; //返回状态
	private String message; //说明
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
		
	
	
}
