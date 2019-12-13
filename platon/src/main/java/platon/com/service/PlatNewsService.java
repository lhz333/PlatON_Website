package platon.com.service;

import java.util.List;

import platon.com.po.PlatNews;
import platon.com.po.PlatNewsCn;
import platon.com.po.PlatNewsCnEn;
import platon.com.po.PlatNewsEn;
import platon.com.po.PlatUser;

import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

public interface PlatNewsService {
	
	/**
	 * 获取新闻列表
	 * 中文
	 * @return
	 */
	public Pagination<PlatNewsCn> findPlatNewsCn(PlatNewsCn news);
	
	
	
	/**
	 * 新增新闻列表
	 * 中文
	 * @throws Exception 
	 */
	public Integer insNewsCn(PlatNewsCn news,PlatUser u) throws Exception;
	
	
	
	/**
	 * 官网展示新闻列表
	 * 中文
	 * web
	 * @return
	 */
	public Pagination<PlatNewsCn> findPlatNewsCnOnline(PlatNewsCn news);
	
	
	/**
	 * 官网展示新闻列表
	 * 英文
	 * web
	 * @return
	 */
	public Pagination<PlatNewsEn> findPlatNewsEnOnline(PlatNewsEn news);
	
	
	/**
	 * 官网展示新闻列表
	 * 中文
	 * yidong
	 * @return
	 */
	public Pagination<PlatNewsCn> findPlatNewsCnMobileOnline(PlatNewsCn news);
	
	
	/**
	 * 官网展示新闻列表
	 * 英文
	 * yidong
	 * @return
	 */
	public Pagination<PlatNewsEn> findPlatNewsEnMobileOnline(PlatNewsEn news);
	
	
	/**
	 * 编辑新闻列表
	 * 中文
	 * @throws Exception 
	 */
	public Integer uptNewsCn(PlatNewsCn news,PlatUser u) throws Exception;
	
	
	
	/**
	 * 获取单个新闻
	 * 中文
	 * @throws Exception 
	 */
	public PlatNewsCn findPlatNewsCnById(PlatNewsCn news) throws Exception;
	
	
	
	/**
	 * 新闻发布及撤回
	 * 中文
	 * @throws Exception 
	 */
	public Integer uptNewsCnStatus(PlatNewsCn news,PlatUser u) throws Exception;
	
	
	
	
	/**
	 * 升序降序
	 * 中文
	 */
	public Integer sortNewsCnUporDown(PlatNewsCn news) throws Exception;
	
	
	/**
	 * 获取未绑定新闻列表
	 * 中文
	 * @param news
	 * @return
	 */
	public Pagination<PlatNewsCn> findNotBindPlatNewsCn(PlatNewsCn news);
	
	/**
	 * 获取未绑定新闻列表
	 * 英文
	 * @param news
	 * @return
	 */
	public Pagination<PlatNewsEn> findNotBindPlatNewsEn(PlatNewsEn news);
	
	
	
	/**
	 * 获取新闻列表
	 * 英文
	 * @return
	 */
	public Pagination<PlatNewsEn> findPlatNewsEn(PlatNewsEn news);
	
	
	
	/**
	 * 新增新闻列表
	 * 英文
	 * @throws Exception 
	 */
	public Integer insNewsEn(PlatNewsEn news,PlatUser u) throws Exception;
	
	
	
	/**
	 * 编辑新闻列表
	 * 英文
	 * @throws Exception 
	 */
	public Integer uptNewsEn(PlatNewsEn news,PlatUser u) throws Exception;
	
	
	
	/**
	 * 获取单个新闻
	 * 英文
	 * @throws Exception 
	 */
	public PlatNewsEn findPlatNewsEnById(PlatNewsEn news) throws Exception;
	
	
	
	/**
	 * 新闻发布及撤回
	 * 英文
	 * @throws Exception 
	 */
	public Integer uptNewsEnStatus(PlatNewsEn news,PlatUser u) throws Exception;
	
	
	
	

	/**
	 * 升序降序
	 * 英文
	 */
	public Integer sortNewsEnUporDown(PlatNewsEn news) throws Exception;
	
	
	
	
	/**
	 * 中英文新闻详情页调整
	 * @return
	 */
	public PlatNews findNewsDtail(PlatNewsCnEn news);
	
	
	/**
	 * 根据分类id获取文章
	 * 中文
	 * @param Id
	 * @return
	 */
	public List<PlatNewsCn> findPlatCnByTypeId(Integer id);
	
	/**
	 * 根据分类id获取文章
	 * 英文
	 * @param Id
	 * @return
	 */
	public List<PlatNewsEn> findPlatEnByTypeId(Integer id);
	
	
	/**
	 * zh
	 * 根据标签id 获取
	 * 相关阅读 文章
	 * 
	 */
	public List<PlatNewsCn> findPlatNewsCnByLableId(PlatNews news);
	

	/**
	 * ying
	 * 根据标签id 获取
	 * 相关阅读 文章
	 * 
	 */
	public List<PlatNewsEn> findPlatNewsEnByLableId(PlatNews news);
	
	
	
	/**
	 * zh
	 * 根据标签id 获取
	 * 相关阅读 文章
	 * 
	 */
	public Pagination<PlatNewsCn> pagePlatNewsCnByLableId(PlatNews news);
	

	/**
	 * ying
	 * 根据标签id 获取
	 * 相关阅读 文章
	 * 
	 */
	public Pagination<PlatNewsEn> pagePlatNewsEnByLableId(PlatNews news);
	
	
	
	
}
