package platon.com.service;

import java.util.List;

import platon.com.po.PlatSubscription;

import com.jdbcTemplateTool.page.Pagination;

public interface PlatSubscriptionService {
	
	
	/**
	 * 获取订阅列表
	 */
	public Pagination<PlatSubscription> findPlatSubscription(PlatSubscription platSubscription);
	
	
	/**
	 * 获取订阅列表
	 * 非分页
	 */
	public List<PlatSubscription> findListPlatSubscription(PlatSubscription platSubscription);
	
	/**
	 * 新增订阅管理
	 * @param platSubscription
	 * @return
	 * @throws Exception
	 */
	public Integer insSubscription(PlatSubscription platSubscription) throws Exception;
	
	/**
	 * 修改订阅管理
	 * @param platSubscription
	 * @return
	 * @throws Exception
	 */
	public Integer uptSubscription(PlatSubscription platSubscription) throws Exception;
	
	
	/**
	 * 删除订阅
	 * @param platSubscription
	 * @return
	 * @throws Exception
	 */
	public Integer removeSubscription(PlatSubscription platSubscription) throws Exception;
	
}
