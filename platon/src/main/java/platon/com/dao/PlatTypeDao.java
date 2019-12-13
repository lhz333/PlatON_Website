package platon.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatTypeCn;
import platon.com.po.PlatTypeEn;

import com.jdbcTemplateTool.JdbcTemplateTool;
import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

@Repository
public class PlatTypeDao {
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	
	/**
	 * 插入分类 中文
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public Integer insTypeCn(PlatTypeCn cn) throws Exception{
		jdbcTemplateTool.save(cn);
		return cn.getId();
		
	}
	/**
	 * 插入分类 英文
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public Integer insTypeEn(PlatTypeEn en) throws Exception{
		jdbcTemplateTool.save(en);
		return en.getId();
		
	}
	
	/**
	 * 获取中文分类列表
	 * @return
	 */
	public Pagination<PlatTypeCn> pagePlatTypeCn(PlatTypeCn cn,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_type_cn.id,plat_type_cn.`name`,count(plat_news_cn.id) count "); 
		sb.append(" from plat_type_cn LEFT JOIN plat_news_cn ON plat_type_cn.id=plat_news_cn.type_id ");
		sb.append(" where plat_type_cn.`status`=1 ");
		sb.append(" GROUP BY plat_type_cn.id ");
		sb.append(" order by plat_type_cn.id asc  ");
		
		Pagination<PlatTypeCn> faqs=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatTypeCn.class,page);
		return faqs;
		
	}
	/**
	 * 获取英文分类列表
	 * @return
	 */
	public Pagination<PlatTypeEn> pagePlatTypeEn(PlatTypeEn faq,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_type_en.id,plat_type_en.`name`,count(plat_news_en.id) count "); 
		sb.append(" from plat_type_en LEFT JOIN plat_news_en ON plat_type_en.id=plat_news_en.type_id ");
		sb.append(" where plat_type_en.`status`=1 ");
		sb.append(" GROUP BY plat_type_en.id ");
		sb.append(" order by plat_type_en.id asc  ");
		
		Pagination<PlatTypeEn> faqs=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatTypeEn.class,page);
		return faqs;
		
	}
	
	
	
	
	/**
	 * 英文
	 * 获取条件获取分类
	 * id  or  name
	 * @throws Exception 
	 */
	public List<PlatTypeEn> findPlatTypeEnByParams(PlatTypeEn en){
		StringBuilder sb=new StringBuilder();
		sb.append("select * from plat_type_en ");
		sb.append(" where `status`=1 ");
		if(en.getId()!=null){
			sb.append(" and id="+en.getId());
		}
		if(en.getName()!=null){
			sb.append(" and `name`='"+en.getName()+"'");
		}
		
		List<PlatTypeEn> list=jdbcTemplateTool.findList(sb.toString(),null, PlatTypeEn.class);
		return list;
	}
	/**
	 * 中文
	 * 获取条件获取分类
	 * id  or  name
	 * @throws Exception 
	 */
	public List<PlatTypeCn> findPlatTypeCnByParams(PlatTypeCn en){
		StringBuilder sb=new StringBuilder();
		sb.append("select * from plat_type_cn ");
		sb.append(" where `status`=1 ");
		if(en.getId()!=null){
			sb.append(" and id="+en.getId());
		}
		if(en.getName()!=null){
			sb.append(" and `name`='"+en.getName()+"'");
		}
		
		List<PlatTypeCn> list=jdbcTemplateTool.findList(sb.toString(),null, PlatTypeCn.class);
		return list;
	}
	
	
	/**
	 * 修改分类中文名称
	 * @throws Exception 
	 */
	public int uptPlatTypeCn(PlatTypeCn cn) throws Exception{
		String sql="update plat_type_cn set `name`='"+cn.getName()+"' where id="+cn.getId();
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
	}
	
	/**
	 * 修改分类英文名称
	 * @throws Exception 
	 */
	public int uptPlatTypeEn(PlatTypeEn en) throws Exception{
		String sql="update plat_type_en set `name`='"+en.getName()+"' where id="+en.getId();
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
	}
	
	
	/**
	 * 中文
	 * 删除分类名称
	 * @throws Exception 
	 */
	public int removePlatTypeCn(PlatTypeCn cn) throws Exception{
		String sql="delete from plat_type_cn where id="+cn.getId();
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
	}
	/**
	 * 英文
	 * 删除分类名称
	 * @throws Exception 
	 */
	public int removePlatTypeEn(PlatTypeEn en) throws Exception{
		String sql="delete from plat_type_en where id="+en.getId();
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
	}
	
	
	
	
}
