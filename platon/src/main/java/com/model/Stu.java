package com.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="test_stu")
public class Stu {
	
	private int id;
	
	
	private String stuName1;
	
	private int stuAge;
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="stu_name")
	public String getStuName1() {
		return stuName1;
	}
	public void setStuName1(String stuName1) {
		this.stuName1 = stuName1;
	}
	public int getStuAge() {
		return stuAge;
	}
	public void setStuAge(int stuAge) {
		this.stuAge = stuAge;
	}
	
	
	
}
