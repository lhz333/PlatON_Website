package platon.com.service;

import platon.com.po.PlatFaq;

import com.jdbcTemplateTool.page.Pagination;

public interface PlatFaqService {
	
	

	/**
	 * 插入faq
	 * @param faq
	 * @return
	 * @throws Exception 
	 */
	public Integer insFaq(PlatFaq faq) throws Exception;
	
	/**
	 * 获取faq列表
	 * @return
	 */
	public Pagination<PlatFaq> findPlatFaq(PlatFaq faq);
	
	
	
	
	/**
	 * 官网展示faq列表
	 * @return
	 */
	public Pagination<PlatFaq> findPlatFaqOnline(PlatFaq news);
	
	
	
	/**
	 * 获取单个 faq
	 * @return
	 */
	public PlatFaq findPlatFaqById(PlatFaq faq);
	
	
	
	/**
	 * 编辑faq列表
	 * @throws Exception 
	 */
	public Integer uptFaq(PlatFaq faq) throws Exception;
	
	
	

	/**
	 * faq发布及撤回 删除
	 * 中文
	 * @throws Exception 
	 */
	public int uptFaqStatus(PlatFaq faq) throws Exception;
	
	
	
	/**
	 * 排序
	 */
	public Integer sortFaq(PlatFaq faq) throws Exception;
	
	
	
}
