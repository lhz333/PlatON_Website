package platon.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatNewsHistoryOpertionCn;
import platon.com.po.PlatNewsHistoryOpertionEn;

import com.jdbcTemplateTool.JdbcTemplateTool;

@Repository
public class PlatNewsHistoryOpertionDao {
	
	
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	
	/**
	 * 新增中文操作记录
	 * @throws Exception 
	 */
	public void insCnOpertion(PlatNewsHistoryOpertionCn cn) throws Exception{
		jdbcTemplateTool.save(cn);
	}
	
	/**
	 * 中文操作记录列表
	 * @throws Exception 
	 */
	public List<PlatNewsHistoryOpertionCn> findCnOpertion(PlatNewsHistoryOpertionCn cn){
		String sql="select * from plat_news_history_opertion_cn where new_id="+cn.getNewId();
		List<PlatNewsHistoryOpertionCn> list=jdbcTemplateTool.findList(sql, null,PlatNewsHistoryOpertionCn.class);
		return list;
	}
	
	
	
	/**
	 * 新增英文操作记录
	 * @throws Exception 
	 */
	public void insEnOpertion(PlatNewsHistoryOpertionEn en) throws Exception{
		jdbcTemplateTool.save(en);
	}
	
	/**
	 * 英文操作记录列表
	 * @throws Exception 
	 */
	public List<PlatNewsHistoryOpertionEn> findEnOpertion(PlatNewsHistoryOpertionEn en){
		String sql="select * from plat_news_history_opertion_en where new_id="+en.getNewId();
		List<PlatNewsHistoryOpertionEn> list=jdbcTemplateTool.findList(sql, null,PlatNewsHistoryOpertionEn.class);
		return list;
	}
	
	
	
	
	
}
