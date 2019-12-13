package platon.com.service;

import java.util.List;

import platon.com.po.PlatFaqHistoryOpertion;

public interface PlatFaqHistoryOpertionService {
	
	
	/**
	 * 插入faq操作记录
	 * @param platNewsHistoryOpertion
	 */
	public void insFaqOpertion(PlatFaqHistoryOpertion faq) throws Exception;
	
	/**
	 * 中文操作记录
	 * @return
	 */
	public List<PlatFaqHistoryOpertion> findCnOpertion(PlatFaqHistoryOpertion faq);
	
}
