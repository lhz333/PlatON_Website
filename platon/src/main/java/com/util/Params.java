package com.util;

import java.util.Date;
import java.util.List;

public class Params {
	private Integer pageNo;
	private Integer pageSize;
	
	private String type; //cn en
	
	private Integer sortType; //1 升序、2降序
	
	private Date staTime;
	private Date endTime;
	
	private String strStaTime;
	private String strEndTime;
	
	private Integer length; //相关阅读，显示多少个
	private List<Integer> lableIds; //相关阅读的标签id
	
	private Date strCreateTime;  //创建开始时间
	private Date endCreateTime;  //创建结束时间
	
	private Date strReleaseTime; //发布开始时间
	private Date endReleaseTime;//发布结束时间
	
	
	private Integer lable; //标签id
	
	public Integer getSortType() {
		return sortType;
	}
	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStaTime() {
		return staTime;
	}
	public void setStaTime(Date staTime) {
		this.staTime = staTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getStrStaTime() {
		return strStaTime;
	}
	public void setStrStaTime(String strStaTime) {
		this.strStaTime = strStaTime;
	}
	public String getStrEndTime() {
		return strEndTime;
	}
	public void setStrEndTime(String strEndTime) {
		this.strEndTime = strEndTime;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public List<Integer> getLableIds() {
		return lableIds;
	}
	public void setLableIds(List<Integer> lableIds) {
		this.lableIds = lableIds;
	}
	public Integer getLable() {
		return lable;
	}
	public void setLable(Integer lable) {
		this.lable = lable;
	}
	public Date getStrCreateTime() {
		return strCreateTime;
	}
	public void setStrCreateTime(Date strCreateTime) {
		this.strCreateTime = strCreateTime;
	}
	public Date getEndCreateTime() {
		return endCreateTime;
	}
	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}
	public Date getStrReleaseTime() {
		return strReleaseTime;
	}
	public void setStrReleaseTime(Date strReleaseTime) {
		this.strReleaseTime = strReleaseTime;
	}
	public Date getEndReleaseTime() {
		return endReleaseTime;
	}
	public void setEndReleaseTime(Date endReleaseTime) {
		this.endReleaseTime = endReleaseTime;
	}
	
	
	
	
}
