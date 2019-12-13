package platon.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatFaq;

import com.jdbcTemplateTool.JdbcTemplateTool;
import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

@Repository
public class PlatFaqDao {
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	
	/**
	 * 插入faq
	 * @param faq
	 * @return
	 * @throws Exception 
	 */
	public Integer insFaq(PlatFaq faq) throws Exception{
		jdbcTemplateTool.save(faq);
		return faq.getId();
		
	}
	
	/**
	 * 获取faq列表
	 * @return
	 */
	public Pagination<PlatFaq> findPlatFaq(PlatFaq faq,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select * from plat_faq where status!=2");
		if(faq.getProblemCn()!=null && !"".equals(faq.getProblemCn())){
			sb.append(" and problem_cn like '%"+faq.getProblemCn()+"%'  ");
		}
		sb.append("  order by sort_id desc  ");
		
		Pagination<PlatFaq> faqs=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatFaq.class,page);
		return faqs;
		
	}
	
	
	
	
	/**
	 * 官网展示faq列表
	 * @return
	 */
	public Pagination<PlatFaq> findPlatFaqOnline(PlatFaq news,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select * from plat_faq where status=1  ");
		sb.append("  order by sort_id desc   ");
		Pagination<PlatFaq> faqs=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatFaq.class,page);
		return faqs;
		
	}
	
	
	
	/**
	 * 获取单个 faq
	 * @return
	 */
	public PlatFaq findPlatFaqById(PlatFaq faq){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select * from plat_faq where `status`!=2 ");
		if(faq.getId()!=null){
			sb.append(" and id="+faq.getId());
		}
		if(faq.getStatus()!=null){
			sb.append(" and status="+faq.getStatus());
		}
		List<PlatFaq> faqs=jdbcTemplateTool.findList(sb.toString(),null,PlatFaq.class);
		if(faq==null || faqs.size()==0)
			return null;
		return faqs.get(0);
		
	}
	
	
	
	/**
	 * 编辑faq列表
	 * @throws Exception 
	 */
	public Integer uptFaq(PlatFaq faq) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append("update plat_faq set  ");
		if(faq.getProblemCn()==null)
			sb.append(",`problem_cn`="+faq.getProblemCn());
		else
			sb.append(",`problem_cn`='"+faq.getProblemCn()+"'");
		if(faq.getReplyCn()==null)
			sb.append(",`reply_cn`="+faq.getReplyCn());
		else
			sb.append(",`reply_cn`='"+faq.getReplyCn()+"'");
		if(faq.getProblemEn()==null)
			sb.append(",`problem_en`="+faq.getProblemEn());
		else
			sb.append(",`problem_en`='"+faq.getProblemEn()+"'");
		
		if(faq.getReplyEn()==null)
			sb.append(",`reply_en`="+faq.getReplyEn());
		else
			sb.append(",`reply_en`='"+faq.getReplyEn()+"'");
		
		if(faq.getComplete()==null)
			sb.append(",`complete`="+faq.getComplete());
		else
			sb.append(",`complete`='"+faq.getComplete()+"'");
		
		
			sb.append(",`creator`='"+faq.getCreator()+"'");
			sb.append(",`create_time`=now()");
		
		sb.append(" where id="+faq.getId());
		String sql=sb.toString().replaceFirst(",","");
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
		
	}
	
	
	
	

	/**
	 * faq发布及撤回 删除
	 * 中文
	 * @throws Exception 
	 */
	public int uptFaqStatus(PlatFaq faq) throws Exception{
		String sql="update plat_faq set `status`="+faq.getStatus()+" where id="+faq.getId();
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
		
	}
	
	
	
	/**
	 * 排序
	 */
	public Integer sortFaq(PlatFaq faq){
		StringBuilder sb=new StringBuilder();
		sb.append("update plat_faq set sort_id=? where id=?");
		return jdbcTemplateTool.getJdbcTemplate().update(sb.toString(), new Object[]{faq.getSortId(),faq.getId()});
	}
	
	
	/**
	 * 获取单个有效回答（未删除）
	 * @throws Exception 
	 */
	public PlatFaq findFaqByStatus(PlatFaq faq) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append("select * from plat_faq ");
		if(faq.getSortType().intValue()==1){
			sb.append(" where sort_id>? and status!=2  ORDER BY sort_id asc limit 1");
		}else if(faq.getSortType().intValue()==2){
			sb.append(" where sort_id<? and status!=2 ORDER BY sort_id desc limit 1");
		}
		List<PlatFaq> list=jdbcTemplateTool.findList(sb.toString(),new Object[]{faq.getSortId()}, PlatFaq.class);
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
		
	}
	
	
	/**
	 * 获取最大id
	 * 中文
	 * @return
	 */
	public Integer findMaxId(){
		String sql="select IFNULL(max(id),0) maxId from plat_faq";
		Long maxId=jdbcTemplateTool.findCount(sql, null);
		if(maxId==null)
			return 1;
		return maxId.intValue();
	}
	
}
