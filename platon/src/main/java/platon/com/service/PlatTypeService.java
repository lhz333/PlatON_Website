package platon.com.service;

import java.util.List;

import platon.com.po.PlatTypeCn;
import platon.com.po.PlatTypeEn;

import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

public interface PlatTypeService {
	
	/**
	 * 插入分类 中文
	 * @return
	 * @throws Exception
	 */
	public Integer insTypeCn(PlatTypeCn cn) throws Exception;
	/**
	 * 插入分类 英文
	 * @return
	 * @throws Exception
	 */
	public Integer insTypeEn(PlatTypeEn en) throws Exception;
	
	/**
	 * 获取中文分类列表
	 * @return
	 */
	public Pagination<PlatTypeCn> findPlatTypeCn(PlatTypeCn cn);
	/**
	 * 获取英文分类列表
	 * @return
	 */
	public Pagination<PlatTypeEn> findPlatTypeEn(PlatTypeEn en);
	
	/**
	 * 英文
	 * 获取条件获取分类
	 * id  or  name
	 * @throws Exception 
	 */
	public List<PlatTypeEn> findPlatTypeEnByParams(PlatTypeEn en);
	
	/**
	 * 中文
	 * 获取条件获取分类
	 * id  or  name
	 * @throws Exception 
	 */
	public List<PlatTypeCn> findPlatTypeCnByParams(PlatTypeCn en);
	
	/**
	 * 英文
	 * 删除分类名称
	 * @throws Exception 
	 */
	public int removePlatTypeCn(PlatTypeEn en) throws Exception;
	/**
	 * 中文
	 * 删除分类名称
	 * @throws Exception 
	 */
	public int removePlatTypeCn(PlatTypeCn cn) throws Exception;
	/**
	 * 修改分类英文名称
	 * @throws Exception 
	 */
	public int uptPlatTypeEn(PlatTypeEn en) throws Exception;
	/**
	 * 修改分类中文名称
	 * @throws Exception 
	 */
	public int uptPlatTypeCn(PlatTypeCn cn) throws Exception;
	
}
