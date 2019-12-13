package platon.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatLable;
import platon.com.po.PlatNewLable;

import com.jdbcTemplateTool.JdbcTemplateTool;

@Repository
public class PlatLableDao {
	
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	
	/**
	 * 新增标签
	 * @param lable
	 * @return
	 */
	public Integer saveLable(PlatLable lable) throws Exception{
		jdbcTemplateTool.save(lable);
		return lable.getId();
	}
	
	
	/**
	 * 判断标签是否存在
	 * @param lable
	 * @return
	 */
	public PlatLable findLableByName(PlatLable lable) throws Exception{
		String sql="select id from plat_lable where lable=?";
		List<PlatLable> list=jdbcTemplateTool.findList(sql,new Object[]{lable.getLable()}, PlatLable.class);
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}
	
	
	
	/**
	 * 批量插入标签对应关系
	 */
	public Boolean saveNewLable(List<PlatNewLable> lable) throws Exception{
		for (PlatNewLable platNewLable : lable) {
			jdbcTemplateTool.save(platNewLable);;
		}
		return true;
	}
	
	
	/**
	 * 删除标签和文章对应关系
	 * lable  中文标签、英文标签
	 */
	public Boolean removeNewLable(PlatNewLable lable) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append(" delete from plat_new_lable where ");
		if(lable.getNewCnId()!=null){
			sb.append(" new_cn_id="+lable.getNewCnId());
		}
		if(lable.getNewEnId()!=null){
			sb.append(" new_en_id="+lable.getNewEnId());
		}
		jdbcTemplateTool.getJdbcTemplate().update(sb.toString());
		return true;
	}
	
	/**
	 * 根据新闻获取标签列表
	 */
	public List<PlatLable> finfLablesByNewsId(PlatNewLable lable){
		StringBuilder sb=new StringBuilder();
 		sb.append(" select  plat_lable.* from  plat_lable ");
 		sb.append(" LEFT JOIN plat_new_lable ON  plat_lable.id=plat_new_lable.lable_id ");
		if(lable.getNewCnId()!=null){
			sb.append(" where new_cn_id="+lable.getNewCnId());
		}
		if(lable.getNewEnId()!=null){
			sb.append(" where new_en_id="+lable.getNewEnId());
		}
		List<PlatLable> list=jdbcTemplateTool.findList(sb.toString(), null, PlatLable.class);
		return list;
	}
	
	
}
