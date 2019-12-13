package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.MyTest;
import com.jdbcTemplateTool.exception.NoColumnAnnotationFoundException;
import com.jdbcTemplateTool.exception.NoIdAnnotationFoundException;
import com.model.Books;
import com.model.Stu;
import com.service.TestService;

@Service
public class TestServiceImpl  implements TestService{
	
	@Autowired
	private MyTest myTest;
	
	@Override
	public Stu findStu(Integer id) {
		try {
			Stu stu=myTest.getStu(id);
			return stu;
		} catch (NoIdAnnotationFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoColumnAnnotationFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void batchSaveStu(List<Stu> listStu) {
		myTest.batchSaveStu(listStu);;
	}
	@Transactional
	@Override
	public String saveStu(Stu stu) throws Exception{
		String ss=myTest.saveStu(stu);
		Books book=new Books();
		book.setBook("中文");
		myTest.saveBook(book);
//		int k=5/0;
		return ss;
	}
	
	
	
	
	
}
