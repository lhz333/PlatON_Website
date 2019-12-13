package platon.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatNewsCnEn;

import com.jdbcTemplateTool.JdbcTemplateTool;

@Repository
public class PlatNewsCnEnDao {
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	
	
	/**
	 * 新增中英文绑定对应关系
	 * insert
	 * @throws Exception 
	 */
	public Integer insPlatNewsCnEn(PlatNewsCnEn news) throws Exception{
		jdbcTemplateTool.save(news);
		return news.getId();
	}
	
	
	/**
	 * 新增中英文绑定对应关系
	 * insert
	 * @throws Exception 
	 */
	public Boolean removeNewsCnEn(PlatNewsCnEn news) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append(" delete from plat_news_cn_en where `status`=1 ");
		if(news.getCnId()!=null){
			sb.append(" and cn_id="+news.getCnId());
		}else if(news.getEnId()!=null){
			sb.append(" and en_id="+news.getEnId());
		}
		jdbcTemplateTool.getJdbcTemplate().update(sb.toString());
		return true;
	}
	
	
	
	

	/**
	 * 根据中英文id获取绑定关系
	 * @throws Exception 
	 */
	public PlatNewsCnEn findPlatNewsCnEn(PlatNewsCnEn news) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append(" select * from plat_news_cn_en where `status`=1 ");
		if(news.getCnId()!=null){
			sb.append(" and cn_id="+news.getCnId());
		}else if(news.getEnId()!=null){
			sb.append(" and en_id="+news.getEnId());
		}
		
		List<PlatNewsCnEn> list=jdbcTemplateTool.findList(sb.toString(), null, PlatNewsCnEn.class);
		if(list!=null && list.size()>0)
			return list.get(0);
		
		return null;
	}
	
	
	
}
