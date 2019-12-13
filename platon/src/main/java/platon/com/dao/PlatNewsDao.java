package platon.com.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatNews;
import platon.com.po.PlatNewsCn;
import platon.com.po.PlatNewsCnEn;
import platon.com.po.PlatNewsEn;
import platon.com.po.PlatTypeCn;
import platon.com.po.PlatTypeEn;

import com.jdbcTemplateTool.JdbcTemplateTool;
import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;
@Repository
public class PlatNewsDao {
	
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	private static final SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 获取新闻列表
	 * 中文
	 * @return
	 */
	public Pagination<PlatNewsCn> findPlatNewsCn(PlatNewsCn news,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_news_cn.*,IFNULL(plat_news_cn_en.id,0) bind_status,  ");
		sb.append(" plat_type_cn.`name` type_name ");
		sb.append(" from plat_news_cn LEFT JOIN plat_news_cn_en ON plat_news_cn.id=plat_news_cn_en.cn_id  ");		
		sb.append(" LEFT JOIN plat_type_cn ON plat_news_cn.type_id=plat_type_cn.id ");
		sb.append(" where  plat_news_cn.status!=2 ");
		if(news.getTitle()!=null && !"".equals(news.getTitle())){
			sb.append(" and plat_news_cn.title like '%"+news.getTitle()+"%'");
		}
		if(news.getStatus()!=null ){
			sb.append(" and plat_news_cn.status="+news.getStatus());
		}
		if(news.getTypeId()!=null )
			sb.append(" and plat_news_cn.type_id="+news.getTypeId());
		
		if(news.getStrCreateTime()!=null && !"".equals(news.getStrCreateTime())
				&& news.getEndCreateTime()!=null && !"".equals(news.getEndCreateTime())){
			sb.append(" and DATE_FORMAT(plat_news_cn.create_time,'%Y%m%d%H%i%s')>=DATE_FORMAT("+df.format(news.getStrCreateTime())+",'%Y%m%d%H%i%s')");
			sb.append(" and DATE_FORMAT(plat_news_cn.create_time,'%Y%m%d%H%i%s')<DATE_FORMAT("+df.format(news.getEndCreateTime())+",'%Y%m%d%H%i%s')");
			
		}
		
		if(news.getStrReleaseTime()!=null && !"".equals(news.getStrReleaseTime())
				&& news.getEndReleaseTime()!=null && !"".equals(news.getEndReleaseTime())){
			sb.append(" and DATE_FORMAT(plat_news_cn.release_time,'%Y%m%d%H%i%s')>=DATE_FORMAT("+df.format(news.getStrCreateTime())+",'%Y%m%d%H%i%s')");
			sb.append(" and DATE_FORMAT(plat_news_cn.release_time,'%Y%m%d%H%i%s')<DATE_FORMAT("+df.format(news.getEndCreateTime())+",'%Y%m%d%H%i%s')");
			
		}
		
		
		sb.append(" order by sort_id desc");
		
		Pagination<PlatNewsCn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsCn.class,page);
		return pageNews;
		
	}
	
	public void test1(Date date){
		String sql="select DATE_FORMAT("+date+",'%Y-%m-%d %H:%i:%s')";
		Map<String,Object> map=jdbcTemplateTool.getJdbcTemplate().queryForMap(sql);
		System.out.println(map);
	}
	
	
	/**
	 * 获取新闻列表
	 * 英文
	 * @return
	 */
	public Pagination<PlatNewsEn> findPlatNewsEn(PlatNewsEn news,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_news_en.*,IFNULL(plat_news_cn_en.id,0) bind_status, ");
		sb.append(" plat_type_en.`name` type_name ");
		sb.append(" from plat_news_en LEFT JOIN plat_news_cn_en ON plat_news_en.id=plat_news_cn_en.en_id   ");
		sb.append(" LEFT JOIN plat_type_en ON plat_news_en.type_id=plat_type_en.id ");
		sb.append(" where  plat_news_en.status!=2 ");
		if(news.getTitle()!=null && !"".equals(news.getTitle())){
			sb.append(" and plat_news_en.title like '%"+news.getTitle()+"%'");
		}
		if(news.getStatus()!=null && !"".equals(news.getStatus())){
			sb.append(" and plat_news_en.status="+news.getStatus());
		}
		if(news.getTypeId()!=null ){
			sb.append(" and plat_news_en.type_id="+news.getTypeId());
		}
		
		if(news.getStrCreateTime()!=null && !"".equals(news.getStrCreateTime())
				&& news.getEndCreateTime()!=null && !"".equals(news.getEndCreateTime())){
			sb.append(" and DATE_FORMAT(plat_news_en.create_time,'%Y%m%d%H%i%s')>=DATE_FORMAT("+df.format(news.getStrCreateTime())+",'%Y%m%d%H%i%s')");
			sb.append(" and DATE_FORMAT(plat_news_en.create_time,'%Y%m%d%H%i%s')<DATE_FORMAT("+df.format(news.getEndCreateTime())+",'%Y%m%d%H%i%s')");
			
		}
		
		if(news.getStrReleaseTime()!=null && !"".equals(news.getStrReleaseTime())
				&& news.getEndReleaseTime()!=null && !"".equals(news.getEndReleaseTime())){
			sb.append(" and DATE_FORMAT(plat_news_en.release_time,'%Y%m%d%H%i%s')>=DATE_FORMAT("+df.format(news.getStrCreateTime())+",'%Y%m%d%H%i%s')");
			sb.append(" and DATE_FORMAT(plat_news_en.release_time,'%Y%m%d%H%i%s')<DATE_FORMAT("+df.format(news.getEndCreateTime())+",'%Y%m%d%H%i%s')");
			
		}
		
		sb.append(" order by sort_id desc");
		
		Pagination<PlatNewsEn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsEn.class,page);
		return pageNews;
		
	}
	
	/**
	 * 获取新闻top第一条数据
	 * 中文
	 * @return
	 */
	public PlatNewsCn findTopFindPlatNewsCn(){
		StringBuilder sb=new StringBuilder();
		sb.append(" select * from plat_news_cn where `status`=1 ORDER BY sort_id desc  limit 1");
		List<PlatNewsCn> list=jdbcTemplateTool.findList(sb.toString(), null,PlatNewsCn.class);
		if(list==null || list.size()==0)
			return null;
		PlatNewsCn topCn=list.get(0);
			return topCn;
	}
	
	
	/**
	 * 获取新闻top第一条数据
	 * 英文
	 * @return
	 */
	public PlatNewsEn findTopFindPlatNewsEn(){
		StringBuilder sb=new StringBuilder();
		sb.append(" select * from plat_news_en where `status`=1 ORDER BY sort_id desc  limit 1");
		List<PlatNewsEn> list=jdbcTemplateTool.findList(sb.toString(), null,PlatNewsEn.class);
		if(list==null || list.size()==0)
			return null;
		PlatNewsEn topCn=list.get(0);
			return topCn;
	}
	
	
	/**
	 * 官网展示新闻列表
	 * 中文
	 * @return
	 */
	public Pagination<PlatNewsCn> findPlatNewsCnOnline(PlatNewsCn news,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_news_cn.* from plat_news_cn "); 
		sb.append(" LEFT JOIN plat_new_lable ON plat_news_cn.id=plat_new_lable.new_cn_id ");
		sb.append(" LEFT JOIN plat_lable ON plat_lable.id=plat_new_lable.lable_id ");
		sb.append(" where  plat_news_cn.`status`=1 ");
		sb.append(" and plat_news_cn.sort_id<(select MAX(sort_id) from plat_news_cn where  plat_news_cn.status=1) ");
		if(news.getTitle()!=null && !"".equals(news.getTitle())){
			sb.append(" and plat_news_cn.title like '%"+news.getTitle()+"%' ");
		}
		if(news.getLable()!=null ){
			sb.append(" and plat_lable.id="+news.getLable());
		}
		
		sb.append(" order by plat_news_cn.sort_id desc ");   
		
//		sb.append(" select * from plat_news_cn where  status=1  ");
//		sb.append(" and sort_id<(select MAX(sort_id) from plat_news_cn where  plat_news_cn.status=1) ");
//		sb.append("  order by sort_id desc   ");
		Pagination<PlatNewsCn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsCn.class,page);
		return pageNews;
		
	}
	
	/**
	 * 官网展示新闻列表
	 * 中文
	 * @return
	 */
	public Pagination<PlatNewsCn> findPlatNewsCnMobileOnline(PlatNewsCn news,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_news_cn.* from plat_news_cn ");  
		sb.append(" LEFT JOIN plat_new_lable ON plat_news_cn.id=plat_new_lable.new_cn_id "); 
		sb.append(" LEFT JOIN plat_lable ON plat_lable.id=plat_new_lable.lable_id  ");
		sb.append(" where  plat_news_cn.`status`=1 ");
		
		if(news.getTitle()!=null && !"".equals(news.getTitle())){
			sb.append(" and plat_news_cn.title like '%"+news.getTitle()+"%' ");
		}
		if(news.getLable()!=null ){
			sb.append(" and plat_lable.id="+news.getLable());
		}
		if(news.getTypeId()!=null){
			sb.append(" and plat_news_cn.type_id="+news.getTypeId());
		}
		if(news.getTypsList()!=null && news.getTypsList().size()>0){
			for (PlatTypeCn tp :news.getTypsList() ) {
				sb.append(" and plat_news_cn.type_id<>"+tp.getId());
			}
		}
		
		
		sb.append(" GROUP BY plat_news_cn.id  order by plat_news_cn.sort_id desc ");   
		Pagination<PlatNewsCn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsCn.class,page);
		return pageNews;
		
	}
	
	
	/**
	 * 官网展示新闻列表
	 * 英文
	 * @return
	 */
	public Pagination<PlatNewsEn> findPlatNewsEnOnline(PlatNewsEn news,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select * from plat_news_en where  status=1  ");
		sb.append(" and sort_id<(select MAX(sort_id) from plat_news_en where status=1) ");
		sb.append("  order by sort_id desc   ");
		Pagination<PlatNewsEn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsEn.class,page);
		return pageNews;
		
	}
	
	/**
	 * 官网展示新闻列表
	 * 英文
	 * @return
	 */
	public Pagination<PlatNewsEn> findPlatNewsEnMobileOnline(PlatNewsEn news,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_news_en.* from plat_news_en ");  
		sb.append(" LEFT JOIN plat_new_lable ON plat_news_en.id=plat_new_lable.new_en_id "); 
		sb.append(" LEFT JOIN plat_lable ON plat_lable.id=plat_new_lable.lable_id  ");
		sb.append(" where  plat_news_en.`status`=1 ");
		
		if(news.getTitle()!=null && !"".equals(news.getTitle())){
			sb.append(" and plat_news_en.title like '%"+news.getTitle()+"%' ");
		}
		if(news.getLable()!=null ){
			sb.append(" and plat_lable.id="+news.getLable());
		}
		if(news.getTypeId()!=null){
			sb.append(" and plat_news_en.type_id="+news.getTypeId());
		}
		if(news.getTypsList()!=null && news.getTypsList().size()>0){
			for (PlatTypeEn tp :news.getTypsList() ) {
				sb.append(" and plat_news_en.type_id<>"+tp.getId());
			}
		}
		
		
		sb.append(" GROUP BY plat_news_en.id order by plat_news_en.sort_id desc ");   
		
		Pagination<PlatNewsEn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsEn.class,page);
		return pageNews;
		
	}
	
	

	/**
	 * 中英文新闻详情页调整
	 * @return
	 */
	public PlatNews findNewsDtail(PlatNewsCnEn news){
		StringBuilder sb=new StringBuilder();
		
		if(news.getEnId()!=null){
			sb.append("select plat_news_cn.* from  plat_news_cn LEFT JOIN plat_news_cn_en ON plat_news_cn.id=plat_news_cn_en.cn_id  ");
			sb.append(" where plat_news_cn.`status`!=2 and plat_news_cn_en.en_id="+news.getEnId());
			if(news.getStatus()!=null)
			sb.append(" and plat_news_cn.`status`="+news.getStatus());
			List<PlatNews> list=jdbcTemplateTool.findList(sb.toString(), null, PlatNews.class);
			if(list==null || list.size()==0)
				return null;
			return list.get(0);
		}
		
		if(news.getCnId()!=null){
			sb.append("select plat_news_en.* from  plat_news_en LEFT JOIN plat_news_cn_en ON plat_news_en.id=plat_news_cn_en.en_id  ");
			sb.append("where plat_news_en.`status`!=2 and plat_news_cn_en.cn_id="+news.getCnId());
			if(news.getStatus()!=null)
			sb.append(" and plat_news_en.`status`="+news.getStatus());
			List<PlatNews> list=jdbcTemplateTool.findList(sb.toString(), null, PlatNews.class);
			if(list==null || list.size()==0)
				return null;
			return list.get(0);
		}
		
		return null;
	}
	
	
	
	
	/**
	 * 获取新闻列表 为绑定的列表
	 * 中文
	 * @return
	 */
	public Pagination<PlatNewsCn> findNotBindPlatNewsCn(PlatNewsCn news,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_news_cn.*  from plat_news_cn  ");
		
		sb.append(" where  not EXISTS( select cn_id from plat_news_cn_en where plat_news_cn.id=plat_news_cn_en.cn_id)  ");
		sb.append(" and  plat_news_cn.status!=2 ");
		if(news.getTitle()!=null && !"".equals(news.getTitle())){
			sb.append(" and plat_news_cn.title like '%"+news.getTitle()+"%'");
		}
		if(news.getStatus()!=null && !"".equals(news.getStatus())){
			sb.append(" and plat_news_cn.status="+news.getStatus());
		}
		sb.append(" order by sort_id desc");
		
		Pagination<PlatNewsCn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsCn.class,page);
		return pageNews;
		
	}
	
	

	/**
	 * 获取新闻列表 为绑定的列表
	 * 英文
	 * @return
	 */
	public Pagination<PlatNewsEn> findNotBindPlatNewsEn(PlatNewsEn news,Page page){
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_news_en.*  from plat_news_en  ");
		
		sb.append(" where  not EXISTS( select en_id from plat_news_cn_en where plat_news_en.id=plat_news_cn_en.en_id)  ");
		sb.append(" and  plat_news_en.status!=2 ");
		if(news.getTitle()!=null && !"".equals(news.getTitle())){
			sb.append(" and plat_news_en.title like '%"+news.getTitle()+"%'");
		}
		if(news.getStatus()!=null && !"".equals(news.getStatus())){
			sb.append(" and plat_news_en.status="+news.getStatus());
		}
		sb.append(" order by sort_id desc");
		
		Pagination<PlatNewsEn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsEn.class,page);
		return pageNews;
		
	}
	
	
	/**
	 * 新增新闻列表
	 * 中文
	 * @throws Exception 
	 */
	public Integer insNewsCn(PlatNewsCn news) throws Exception{
		jdbcTemplateTool.save(news);
		return news.getId();
		
	}
	

	/**
	 * 新增新闻列表
	 * 英文
	 * @throws Exception 
	 */
	public Integer insNewsEn(PlatNewsEn news) throws Exception{
		jdbcTemplateTool.save(news);
		return news.getId();
		
	}
	
	
	
	/**
	 * 编辑新闻列表
	 * 中文
	 * @throws Exception 
	 */
	public Integer uptNewsCn(PlatNewsCn news) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append(" update plat_news_cn  set ");
		if(news.getTitle()==null)
			sb.append(",`title`="+news.getTitle());
		else
			sb.append(",`title`='"+news.getTitle()+"'");
		if(news.getNewsText()==null)
			sb.append(",`news_text`="+news.getNewsText());
		else
			sb.append(",`news_text`='"+news.getNewsText()+"'");
		if(news.getAuthor()==null)
			sb.append(",`author`="+news.getAuthor());
		else
			sb.append(",`author`='"+news.getAuthor()+"'");
		
		if(news.getTopImgUrl()==null)
			sb.append(",`top_img_url`="+news.getTopImgUrl());
		else
			sb.append(",`top_img_url`='"+news.getTopImgUrl()+"'");
		if(news.getImgUrl()==null)
			sb.append(",`img_url`="+news.getImgUrl());
		else
			sb.append(",`img_url`='"+news.getImgUrl()+"'");
		
		if(news.getComplete()==null)
			sb.append(",`complete`="+news.getComplete());
		else
			sb.append(",`complete`='"+news.getComplete()+"'");
		
		
		if(news.getReleaseTime()==null)
			sb.append(",`release_time`="+news.getReleaseTime());
		else
			sb.append(",`release_time`=DATE_FORMAT("+df.format(news.getReleaseTime())+",'%Y%m%d%H%i%s')");
		
		sb.append(" ,`type_id`="+news.getTypeId());
		
		sb.append(" where id="+news.getId());
		String sql=sb.toString().replaceFirst(",","");
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
		
	}
	
	
	/**
	 * 编辑新闻列表
	 * 英文
	 * @throws Exception 
	 */
	public int uptNewsEn(PlatNewsEn news) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append(" update plat_news_en  set ");
		if(news.getTitle()==null)
			sb.append(",`title`="+news.getTitle());
		else
			sb.append(",`title`='"+news.getTitle()+"'");
		if(news.getNewsText()==null)
			sb.append(",`news_text`="+news.getNewsText());
		else
			sb.append(",`news_text`='"+news.getNewsText()+"'");
		if(news.getAuthor()==null)
			sb.append(",`author`="+news.getAuthor());
		else
			sb.append(",`author`='"+news.getAuthor()+"'");
		
		if(news.getTopImgUrl()==null)
			sb.append(",`top_img_url`="+news.getTopImgUrl());
		else
			sb.append(",`top_img_url`='"+news.getTopImgUrl()+"'");
		if(news.getImgUrl()==null)
			sb.append(",`img_url`="+news.getImgUrl());
		else
			sb.append(",`img_url`='"+news.getImgUrl()+"'");
		
		if(news.getComplete()==null)
			sb.append(",`complete`="+news.getComplete());
		else
			sb.append(",`complete`='"+news.getComplete()+"'");
		
		if(news.getReleaseTime()==null)
			sb.append(",`release_time`="+news.getReleaseTime());
		else
			sb.append(",`release_time`=DATE_FORMAT("+df.format(news.getReleaseTime())+",'%Y%m%d%H%i%s')");
					
		sb.append(" ,`type_id`="+news.getTypeId());
		
		sb.append(" where id="+news.getId());
		String sql=sb.toString().replaceFirst(",","");
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
		
	}
	
	
	/**
	 * 获取单个新闻
	 * 中文
	 * @throws Exception 
	 */
	public PlatNewsCn findPlatNewsCnById(PlatNewsCn news) throws Exception{
		StringBuilder sb=new StringBuilder();
		
		sb.append(" select plat_news_cn.*,plat_type_cn.`name` type_name from plat_news_cn ");
		sb.append(" LEFT JOIN  plat_type_cn ON plat_news_cn.type_id=plat_type_cn.id ");
		sb.append("  where plat_news_cn.status!=2 ");
		if(news.getId()!=null){
			sb.append(" and plat_news_cn.id="+news.getId());
		}
		if(news.getStatus()!=null){
			sb.append(" and plat_news_cn.`status`="+news.getStatus());
		}
		List<PlatNewsCn> list=jdbcTemplateTool.findList(sb.toString(),null, PlatNewsCn.class);
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
		
		
	}
	
	

	/**
	 * 获取单个新闻
	 * 英文
	 * @throws Exception 
	 */
	public PlatNewsEn findPlatNewsEnById(PlatNewsEn news) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append(" select plat_news_en.*,plat_type_en.`name` type_name from plat_news_en ");
		sb.append(" LEFT JOIN  plat_type_en ON plat_news_en.type_id=plat_type_en.id ");
		sb.append("  where plat_news_en.status!=2 ");
		if(news.getId()!=null){
			sb.append(" and plat_news_en.id="+news.getId());
		}
		if(news.getStatus()!=null){
			sb.append(" and plat_news_en.`status`="+news.getStatus());
		}
		List<PlatNewsEn> list=jdbcTemplateTool.findList(sb.toString(),null, PlatNewsEn.class);
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
		
	}
	/**
	 * 获取新闻上一页和下一页id
	 * @param news
	 * @return
	 * @throws Exception
	 */
	public PlatNewsEn findPlatNewsEnUpOrNext(PlatNewsEn news)throws Exception{
//		Integer sortId=news.getSortId();
//		String sql="select id next from plat_news_en where sort_id<? limit 1";
//		List<Integer> nexts=jdbcTemplateTool.findList(sql, new Object[]{sortId}, java.lang.Integer.class);
//		
//		sql="select id from plat_news_en where sort_id>? limit 1";
//		List<Integer> ups=jdbcTemplateTool.findList(sql, new Object[]{sortId}, java.lang.Integer.class);
//		PlatNewsEn en=new PlatNewsEn();
//		if(nexts!=null && nexts.size()>0){
//			en.setNextSortId(nexts.get(0));
//		}
//		if(ups!=null && ups.size()>0){
//			en.setUpSortId(ups.get(0));
//		}
		
		Integer sortId=news.getSortId();
		String sql="select id next_sort_id from plat_news_en where sort_id<"+sortId+" limit 1";
		List<PlatNewsEn> nexts=jdbcTemplateTool.findList(sql, null,PlatNewsEn.class);
		
		sql="select id up_sort_id from plat_news_en where sort_id>"+sortId+" limit 1";
		List<PlatNewsEn> ups=jdbcTemplateTool.findList(sql, null,PlatNewsEn.class);
		PlatNewsEn en=new PlatNewsEn();
		if(nexts!=null && nexts.size()>0){
			en.setNextSortId(nexts.get(0).getNextSortId());
		}
		if(ups!=null && ups.size()>0){
			en.setUpSortId(ups.get(0).getUpSortId());
		}
		return en;
		
	}
	
	/**
	 * 获取新闻上一页和下一页id
	 * @param news
	 * @return
	 * @throws Exception
	 */
	public PlatNewsCn findPlatNewsCnUpOrNext(PlatNewsCn news)throws Exception{
		Integer sortId=news.getSortId();
		String sql="select id next_sort_id from plat_news_cn where sort_id<"+sortId+" limit 1";
		List<PlatNewsCn> nexts=jdbcTemplateTool.findList(sql, null,PlatNewsCn.class);
		
		sql="select id up_sort_id from plat_news_cn where sort_id>"+sortId+" limit 1";
		List<PlatNewsCn> ups=jdbcTemplateTool.findList(sql, null,PlatNewsCn.class);
		PlatNewsCn en=new PlatNewsCn();
		if(nexts!=null && nexts.size()>0){
			en.setNextSortId(nexts.get(0).getNextSortId());
		}
		if(ups!=null && ups.size()>0){
			en.setUpSortId(ups.get(0).getUpSortId());
		}
		return en;
		
	}
	
	
	/**
	 * 获取单个有效新闻（未删除）
	 * 中文
	 * @throws Exception 
	 */
	public PlatNewsCn findPlatNewsCnByStatus(PlatNewsCn news) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append("select * from plat_news_cn ");
		if(news.getSortType().intValue()==1){
			sb.append(" where sort_id>? and status!=2  ORDER BY sort_id asc limit 1");
		}else if(news.getSortType().intValue()==2){
			sb.append(" where sort_id<? and status!=2 ORDER BY sort_id desc limit 1");
		}
		List<PlatNewsCn> list=jdbcTemplateTool.findList(sb.toString(),new Object[]{news.getSortId()}, PlatNewsCn.class);
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
		
	}
	
	/**
	 * 获取单个有效新闻（未删除）
	 * 英文
	 * @throws Exception 
	 */
	public PlatNewsEn findPlatNewsEnByStatus(PlatNewsEn news) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append("select * from plat_news_en ");
		if(news.getSortType().intValue()==1){
			sb.append(" where sort_id>? and status!=2  ORDER BY sort_id asc limit 1");
		}else if(news.getSortType().intValue()==2){
			sb.append(" where sort_id<? and status!=2 ORDER BY sort_id desc limit 1");
		}
		List<PlatNewsEn> list=jdbcTemplateTool.findList(sb.toString(),new Object[]{news.getSortId()}, PlatNewsEn.class);
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
		
		
	}
	
	
	
	/**
	 * 新闻发布及撤回 删除
	 * 中文
	 * @throws Exception 
	 */
	public int uptNewsCnStatus(PlatNewsCn news) throws Exception{
		String sql="update plat_news_cn set `status`="+news.getStatus()+" where id="+news.getId();
		int i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
		
	}
	
	/**
	 * 新闻发布及撤回
	 * 英文
	 * @throws Exception 
	 */
	public Integer uptNewsEnStatus(PlatNewsEn news) throws Exception{
		String sql="update plat_news_en set `status`="+news.getStatus()+" where id="+news.getId();
		Integer i=jdbcTemplateTool.getJdbcTemplate().update(sql);
		return i;
		
	}
	
	
	
	/**
	 * 排序
	 * 中文
	 */
	public Integer sortNewsCn(PlatNewsCn news){
		StringBuilder sb=new StringBuilder();
		sb.append("update plat_news_cn set sort_id=? where id=?");
		return jdbcTemplateTool.getJdbcTemplate().update(sb.toString(), new Object[]{news.getSortId(),news.getId()});
	}
	
	/**
	 * 排序
	 * 英文
	 */
	public Integer sortNewsEn(PlatNewsEn news){
		StringBuilder sb=new StringBuilder();
		sb.append("update plat_news_en set sort_id=? where id=?");
		return jdbcTemplateTool.getJdbcTemplate().update(sb.toString(), new Object[]{news.getSortId(),news.getId()});
	}
	
	
	/**
	 * 获取最大id
	 * 中文
	 * @return
	 */
	public Integer findMaxCnId(){
		String sql="select IFNULL(max(id),0) maxId from plat_news_cn";
		Long maxId=jdbcTemplateTool.findCount(sql, null);
		if(maxId==null)
			return 1;
		return maxId.intValue();
	}
	
	
	/**
	 * 获取最大id
	 * 英文
	 * @return
	 */
	public Integer findMaxEnId(){
		String sql="select IFNULL(max(id),0) maxId from plat_news_en";
		Long maxId=jdbcTemplateTool.findCount(sql, null);
		if(maxId==null)
			return 1;
		return maxId.intValue();
	}
	
	/**
	 * 根据分类id获取文章
	 * @param Id
	 * @return
	 */
	public List<PlatNewsCn> findPlatCnByTypeId(Integer id){
		String sql="select id,sort_id,title,author,release_time,top_img_url,img_url from plat_news_cn where  type_id="+id;
		List<PlatNewsCn> list=jdbcTemplateTool.findList(sql, null, PlatNewsCn.class);
		return list;
		
	}
	/**
	 * 根据分类id获取文章
	 * @param Id
	 * @return
	 */
	public List<PlatNewsEn> findPlatEnByTypeId(Integer id){
		String sql="select id,sort_id,title,author,release_time,top_img_url,img_url from plat_news_en where  type_id="+id;
		List<PlatNewsEn> list=jdbcTemplateTool.findList(sql, null, PlatNewsEn.class);
		return list;
	}
	
	
	
	
	/**
	 * 根据分类id获取文章
	 * @param Id
	 * @return
	 */
	public List<PlatNewsCn> findPlatCnByTypeIdOnline(Integer id){
		StringBuilder sb=new StringBuilder();
		sb.append(" select id,sort_id,title,author,release_time,top_img_url,img_url from plat_news_cn ");
		sb.append(" where `status`=1 and  type_id="+id);
		List<PlatNewsCn> list=jdbcTemplateTool.findList(sb.toString(), null, PlatNewsCn.class);
		return list;
		
	}
	/**
	 * 根据分类id获取文章
	 * @param Id
	 * @return
	 */
	public List<PlatNewsEn> findPlatEnByTypeIdOnline(Integer id){
		StringBuilder sb=new StringBuilder();
		sb.append(" select id,sort_id,title,author,release_time,top_img_url,img_url from plat_news_en ");
		sb.append(" where `status`=1 and  type_id="+id);
		
		List<PlatNewsEn> list=jdbcTemplateTool.findList(sb.toString(), null, PlatNewsEn.class);
		return list;
	}
	
	
	/**
	 * 根据标签获取关联新闻列表
	 * 中文，
	 */
	public List<PlatNewsCn> findPlatNewsCnByLableId(Integer lableId,Integer status){
		StringBuilder sb=new StringBuilder();
		sb.append(" select plat_news_cn.id,plat_news_cn.sort_id,plat_news_cn.title,plat_news_cn.author, ");
		sb.append(" plat_news_cn.release_time,plat_news_cn.top_img_url,plat_news_cn.img_url from plat_news_cn ");
		sb.append(" LEFT JOIN plat_new_lable ON plat_news_cn.id=plat_new_lable.new_cn_id ");
		sb.append(" where plat_new_lable.lable_id="+lableId);
		if(status!=null){
			sb.append(" and plat_news_cn.`status`="+status);
		}else{
			sb.append(" and plat_news_cn.`status`!=2");
		}
		sb.append(" ORDER BY plat_news_cn.create_time desc ");
		List<PlatNewsCn> list=jdbcTemplateTool.findList(sb.toString(), null, PlatNewsCn.class);
		return list;
 	}
	

	/**
	 * 根据标签获取关联新闻列表
	 * 英文
	 */
	public List<PlatNewsEn> findPlatNewsEnByLableId(Integer lableId,Integer status){
		StringBuilder sb=new StringBuilder();
		sb.append(" select plat_news_en.id,plat_news_en.sort_id,plat_news_en.title,plat_news_en.author, ");
		sb.append(" plat_news_en.release_time,plat_news_en.top_img_url,plat_news_en.img_url from plat_news_en ");
		sb.append(" LEFT JOIN plat_new_lable ON plat_news_en.id=plat_new_lable.new_en_id ");
		sb.append(" where plat_new_lable.lable_id="+lableId);
		if(status!=null){
			sb.append(" and plat_news_en.`status`="+status);
		}else{
			sb.append(" and plat_news_en.`status`!=2");
		}
		sb.append(" ORDER BY plat_news_en.create_time desc ");
		List<PlatNewsEn> list=jdbcTemplateTool.findList(sb.toString(), null, PlatNewsEn.class);
		return list;
 	}
	

	
	/**
	 * 根据标签获取关联新闻列表 分页
	 * 
	 * 中文，
	 */
	public Pagination<PlatNewsCn> pagePlatNewsCnByLableId(PlatNews news,Page page){
		StringBuilder sb=new StringBuilder();
		sb.append(" select plat_news_cn.id,plat_news_cn.sort_id,plat_news_cn.title,plat_news_cn.author, ");
		sb.append(" plat_news_cn.release_time,plat_news_cn.top_img_url,plat_news_cn.img_url from plat_news_cn ");
		sb.append(" LEFT JOIN plat_new_lable ON plat_news_cn.id=plat_new_lable.new_cn_id ");
		sb.append(" where plat_new_lable.lable_id="+news.getLable());
		if(news.getStatus()!=null){
			sb.append(" and plat_news_cn.`status`="+news.getStatus());
		}else{
			sb.append(" and plat_news_cn.`status`!=2");
		}
		sb.append(" ORDER BY plat_news_cn.create_time desc ");
		
		Pagination<PlatNewsCn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsCn.class,page);
		return pageNews;
 	}
	

	/**
	 * 分页
	 * 根据标签获取关联新闻列表
	 * 英文
	 */
	public Pagination<PlatNewsEn> pagePlatNewsEnByLableId(PlatNews news,Page page){
		StringBuilder sb=new StringBuilder();
		sb.append(" select plat_news_en.id,plat_news_en.sort_id,plat_news_en.title,plat_news_en.author, ");
		sb.append(" plat_news_en.release_time,plat_news_en.top_img_url,plat_news_en.img_url from plat_news_en ");
		sb.append(" LEFT JOIN plat_new_lable ON plat_news_en.id=plat_new_lable.new_en_id ");
		sb.append(" where plat_new_lable.lable_id="+news.getLable());
		if(news.getStatus()!=null){
			sb.append(" and plat_news_en.`status`="+news.getStatus());
		}else{
			sb.append(" and plat_news_en.`status`!=2");
		}
		sb.append(" ORDER BY plat_news_en.create_time desc ");
		
		Pagination<PlatNewsEn> pageNews=jdbcTemplateTool.findListWithPage(sb.toString(),null,PlatNewsEn.class,page);
		return pageNews;
 	}
	
	
	
	
	/**
	 * 获取最近新闻5条
	 * @param Id
	 * @return
	 */
	public List<PlatNewsCn> findTopNewsCn(Integer length){
		StringBuilder sb=new StringBuilder();
		sb.append(" select id,sort_id,title,author,release_time,top_img_url,img_url from plat_news_cn ");
		sb.append(" where `status`=1 ORDER BY create_time desc ");
		
		if(length!=null){
			sb.append(" limit "+length);
		}else{
			sb.append(" limit 5 ");
		}
		
		List<PlatNewsCn> list=jdbcTemplateTool.findList(sb.toString(), null, PlatNewsCn.class);
		return list;
		
	}
	/**
	 * 获取最近新闻5条
	 * @param Id
	 * @return
	 */
	public List<PlatNewsEn> findTopNewsEn(Integer length){
		StringBuilder sb=new StringBuilder();
		sb.append(" select id,sort_id,title,author,release_time,top_img_url,img_url from plat_news_en ");
		sb.append(" where `status`=1 ORDER BY create_time desc ");
		
		if(length!=null){
			sb.append(" limit "+length);
		}else{
			sb.append(" limit 5 ");
		}
		
		List<PlatNewsEn> list=jdbcTemplateTool.findList(sb.toString(), null, PlatNewsEn.class);
		return list;
	}
	
	
	
}
