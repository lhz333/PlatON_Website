package platon.com.service;

import platon.com.po.PlatNewsCnEn;


public interface PlatNewsCnEnService {
	
	
	/**
	 * 新增关联关系
	 */
	public Integer insPlatNewsCnEn(PlatNewsCnEn news) throws Exception;
	
	
	/**
	 * 新增关联关系
	 */
	public PlatNewsCnEn findPlatNewsCnEn(PlatNewsCnEn news) throws Exception;
	
	
	/**
	 *删除关联理关系
	 */
	public Boolean removeNewsCnEn(PlatNewsCnEn news)  throws Exception;
	
}
