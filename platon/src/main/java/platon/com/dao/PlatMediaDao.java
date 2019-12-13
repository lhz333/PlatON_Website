package platon.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatFaq;
import platon.com.po.PlatMedia;

import com.jdbcTemplateTool.JdbcTemplateTool;
import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

@Repository
public class PlatMediaDao {
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	
	/**
	 * 插入新媒体
	 * @param media
	 * @return
	 * @throws Exception 
	 */
	public Integer insMedia(PlatMedia media) throws Exception{
		jdbcTemplateTool.save(media);
		return media.getId();
		
	}
	
	/**
	 * 获取媒体列表
	 * @return
	 */
	public Pagination<PlatMedia> findPlatMedia(PlatMedia media,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select * from plat_media where status!=2");
		if(media.getTitle()!=null && !"".equals(media.getTitle())){
			sb.append(" and `title` like '%"+media.getTitle()+"%'  ");
		}
		if(media.getName()!=null && !"".equals(media.getName())){
			sb.append(" and `name` = '"+media.getName()+"'  ");
		}
		if(media.getStatus()!=null ){
			sb.append(" and `status`="+media.getStatus());
		}
		sb.append("  order by create_time desc  ");
		
		Pagination<PlatMedia> mediaList=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatMedia.class,page);
		return mediaList;
		
	}
	
	
	
	/**
	 * 获取媒体列表
	 * 官网
	 * @return
	 */
	public Pagination<PlatMedia> findPlatMediaOnline(PlatMedia media,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select * from plat_media where status="+media.getStatus());
		if(media.getTitle()!=null && !"".equals(media.getTitle())){
			sb.append(" and `title` like '%"+media.getTitle()+"%'  ");
		}
		if(media.getName()!=null && !"".equals(media.getName())){
			sb.append(" and `name` = '"+media.getName()+"'  ");
		}
		sb.append("  order by create_time desc  ");
		
		Pagination<PlatMedia> mediaList=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatMedia.class,page);
		return mediaList;
		
	}
	
	
	/**
	 * 获取单个 PlatMedia
	 * @return
	 */
	public PlatMedia findPlatMediaById(PlatMedia media){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select * from plat_media where `status`!=2 ");
		if(media.getId()!=null){
			sb.append(" and id="+media.getId());
		}
		if(media.getStatus()!=null){
			sb.append(" and status="+media.getStatus());
		}
		List<PlatMedia> medias=jdbcTemplateTool.findList(sb.toString(),null,PlatMedia.class);
		if(medias==null || medias.size()==0)
			return null;
		return medias.get(0);
		
	}
	
	
	
	/**
	 * 编辑PlatMedia列表
	 * @throws Exception 
	 */
	public Integer uptMedia(PlatMedia media) throws Exception{
		return jdbcTemplateTool.update(media);
	}
	
	/**
	 * 获取媒体名称
	 * @return
	 */
	public List<PlatMedia> findPlatMedia(){
		String sql="select id,`name` from plat_media";
		return jdbcTemplateTool.findList(sql, null, PlatMedia.class);
	}
	

	/**
	 * PlatMedia发布及撤回 删除
	 * @throws Exception 
	 */
	public int uptPlatMediaStatus(PlatMedia media) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append(" update plat_media set `status`="+media.getStatus());
		if(media.getCreateTime()!=null)
			sb.append(" ,release_time="+media.getReleaseTime());
		sb.append(" where id="+media.getId());
		int i=jdbcTemplateTool.getJdbcTemplate().update(sb.toString());
		return i;
		
	}
	
	
	
}
