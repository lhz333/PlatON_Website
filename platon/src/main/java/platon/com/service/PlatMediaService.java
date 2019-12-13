package platon.com.service;

import java.util.List;

import platon.com.po.PlatMedia;

import com.jdbcTemplateTool.page.Pagination;

public interface PlatMediaService {
	
	/**
	 * PlatMedia发布及撤回 删除
	 * @throws Exception 
	 */
	public int uptMediaStatus(PlatMedia media) throws Exception;
	
	/**
	 * 获取媒体名称
	 * @return
	 */
	public List<PlatMedia> findPlatMedia();
	
	/**
	 * 编辑PlatMedia列表
	 * @throws Exception 
	 */
	public Integer uptMedia(PlatMedia media) throws Exception;
	
	/**
	 * 获取单个 PlatMedia
	 * @return
	 */
	public PlatMedia findPlatMediaById(PlatMedia media);
	
	/**
	 * 获取媒体列表
	 * 官网
	 * @return
	 */
	public Pagination<PlatMedia> findPlatMediaOnline(PlatMedia media);
	
	
	/**
	 * 获取媒体列表
	 * @return
	 */
	public Pagination<PlatMedia> findPlatMedia(PlatMedia media);
	
	/**
	 * 插入新媒体
	 * @param media
	 * @return
	 * @throws Exception 
	 */
	public Integer insMedia(PlatMedia media) throws Exception;
	
	
}
