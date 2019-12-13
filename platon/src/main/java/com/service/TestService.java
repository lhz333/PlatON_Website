package com.service;

import java.util.List;

import com.model.Stu;

public interface TestService {
	
	public Stu findStu(Integer id);

	public void batchSaveStu(List<Stu> listStu);
	
	public String saveStu(Stu stu) throws Exception;
	
}
