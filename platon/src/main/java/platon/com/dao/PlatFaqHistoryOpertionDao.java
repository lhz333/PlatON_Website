package platon.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatFaqHistoryOpertion;
import platon.com.po.PlatNewsHistoryOpertionEn;

import com.jdbcTemplateTool.JdbcTemplateTool;

@Repository
public class PlatFaqHistoryOpertionDao {
	
	
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	
	/**
	 * 新增中文操作记录
	 * @throws Exception 
	 */
	public void insFaqOpertion(PlatFaqHistoryOpertion faq) throws Exception{
		jdbcTemplateTool.save(faq);
	}
	
	/**
	 * 中文操作记录列表
	 * @throws Exception 
	 */
	public List<PlatFaqHistoryOpertion> findFaqOpertion(PlatFaqHistoryOpertion faq){
		String sql="select * from plat_faq_history_opertion where faq_id="+faq.getFaqId();
		List<PlatFaqHistoryOpertion> list=jdbcTemplateTool.findList(sql, null,PlatFaqHistoryOpertion.class);
		return list;
	}
	
	
	
	
	
}
