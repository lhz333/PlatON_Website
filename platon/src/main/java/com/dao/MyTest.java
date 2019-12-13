package com.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jdbcTemplateTool.JdbcTemplateTool;
import com.jdbcTemplateTool.exception.NoColumnAnnotationFoundException;
import com.jdbcTemplateTool.exception.NoIdAnnotationFoundException;
import com.model.Books;
import com.model.Stu;

@Repository
public class MyTest {
	private static final Integer batchNumber=500;
	
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	/**
	 *获取对象
	 */
	public Stu getStu(Integer id) throws NoIdAnnotationFoundException, NoColumnAnnotationFoundException{
		Stu stu=jdbcTemplateTool.get(Stu.class, id);
		return stu;
	}
	
	public String saveStu(Stu stu) throws Exception{
		jdbcTemplateTool.save(stu);
		return "ok";
	}
	
	/**
	 * 批量更新
	 * @param stu
	 */
	public void batchSaveStu(List<Stu> stuList){
		String sql="insert into test_stu(stu_name,stu_age) value(?,?);";
		if(stuList!=null){
			List<Object[]> list=new ArrayList<Object[]>();
			
			for (int i = 0; i < stuList.size(); i++) {
				Object[] obj=new Object[2];
//				obj[0]=stuList.get(i).getStuName();
				obj[1]=stuList.get(i).getStuAge();
				list.add(obj);
				
				//一次批量提交500
				if(list.size()>=batchNumber){
					jdbcTemplateTool.batchUpdate(sql, list);
					list=new ArrayList<Object[]>();
				}
				
			}
			if(list.size()>0){
				jdbcTemplateTool.batchUpdate(sql, list);
				list=new ArrayList<Object[]>();
			}
			
		}
		
	}
	public void saveBook(Books book){
		jdbcTemplateTool.getJdbcTemplate().execute("INSERT INTO test_books(id,book,create_time) values(1,'"+book.getBook()+"',now());");
	}
	
}
