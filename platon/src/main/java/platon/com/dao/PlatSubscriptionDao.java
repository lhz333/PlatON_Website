package platon.com.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatNewsCn;
import platon.com.po.PlatSubscription;

import com.jdbcTemplateTool.JdbcTemplateTool;
import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

@Repository
public class PlatSubscriptionDao {
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	private static final SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
	
	
	/**
	 * 获取订阅列表
	 */
	public Pagination<PlatSubscription> findPlatSubscription(PlatSubscription platSubscription,Page page){
		StringBuilder sb=new StringBuilder();
		sb.append("select * from plat_subscription where `status`!=2 ");
		if(platSubscription.getStaTime()!=null && platSubscription.getEndTime()!=null){
			sb.append(" and DATE(create_time)>=DATE("+df.format(platSubscription.getStaTime())+") and DATE(create_time)<=DATE("+df.format(platSubscription.getEndTime())+") ");
		}
		if(platSubscription.getEmail()!=null && !"".equals(platSubscription.getEmail())){
			sb.append(" and email like '%"+platSubscription.getEmail()+"%' ");
		}
		if(platSubscription.getIdType()!=null){
			sb.append(" and id_type="+platSubscription.getIdType());
		}
		if(platSubscription.getIds()!=null && !"".equals(platSubscription.getIds())){
			sb.append(" and id in("+platSubscription.getIds()+")");
		}
		sb.append(" ORDER BY create_time desc  ");
		Pagination<PlatSubscription> list=jdbcTemplateTool.findListWithPage(sb.toString(), null, PlatSubscription.class, page);
		return list;
		
	}
	
	/**
	 * 获取订阅列表
	 * 非分页
	 */
	public List<PlatSubscription> findListPlatSubscription(PlatSubscription platSubscription){
		StringBuilder sb=new StringBuilder();
		sb.append("select * from plat_subscription where `status`=1 ");
		if(platSubscription.getStrStaTime()!=null && platSubscription.getStrEndTime()!=null
				&& !"".equals(platSubscription.getStrStaTime()) && !"".equals(platSubscription.getStrEndTime())
				&& !"null".equals(platSubscription.getStrStaTime()) && !"null".equals(platSubscription.getStrEndTime())){
			sb.append(" and DATE(create_time)>=DATE('"+platSubscription.getStrStaTime()+"') and DATE(create_time)<=DATE('"+platSubscription.getStrEndTime()+"') ");
		}
		if(platSubscription.getEmail()!=null && !"".equals(platSubscription.getEmail()) && !"null".equals(platSubscription.getEmail())){
			sb.append(" and email like '%"+platSubscription.getEmail()+"%' ");
		}
		if(platSubscription.getIdType()!=null){
			sb.append(" and id_type="+platSubscription.getIdType());
		}
		if(platSubscription.getIds()!=null && !"".equals(platSubscription.getIds()) && !"null".equals(platSubscription.getIds())){
			sb.append(" and id in("+platSubscription.getIds()+")");
		}
		sb.append(" ORDER BY create_time desc  ");
		List<PlatSubscription> list=jdbcTemplateTool.findList(sb.toString(), null, PlatSubscription.class);
		return list;
		
	}
	
	
	
	
	/**
	 * 新增订阅管理
	 * @param platSubscription
	 * @return
	 * @throws Exception
	 */
	public Integer insSubscription(PlatSubscription platSubscription) throws Exception{
		jdbcTemplateTool.save(platSubscription);
		return platSubscription.getId();
		
	}
	
	/**
	 * 修改订阅管理
	 * @param platSubscription
	 * @return
	 * @throws Exception
	 */
	public Integer uptSubscription(PlatSubscription platSubscription) throws Exception{
		int i=jdbcTemplateTool.update(platSubscription);
		return i;
	}
	
	/**
	 * 获取单个订阅信息
	 * @param platSubscription
	 * @return
	 * @throws Exception
	 */
	public PlatSubscription findPlatSubscriptionById(PlatSubscription platSubscription) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append("select * from plat_subscription where `status`!=2 ");
		if(platSubscription.getId()!=null){
			sb.append(" and id="+platSubscription.getIds());
		}
		if(platSubscription.getEmail()!=null){
			sb.append(" and email='"+platSubscription.getEmail()+"'");
		}
		List<PlatSubscription> list=jdbcTemplateTool.findList(sb.toString(), null, PlatSubscription.class);
		if(list!=null && list.size()>0)
			return list.get(0);
		
		return null;
		
	}
	

	/**
	 * 删除订阅
	 * @throws Exception 
	 */
	public int removePlatSubscription(PlatSubscription platSubscription) throws Exception{
		String sql="update plat_subscription set `status`=2 where id="+platSubscription.getId();
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
		
	}
	
	
}
