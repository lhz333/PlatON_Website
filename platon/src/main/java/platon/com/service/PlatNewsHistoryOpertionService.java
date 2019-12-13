package platon.com.service;

import java.util.List;

import platon.com.po.PlatNewsHistoryOpertionCn;
import platon.com.po.PlatNewsHistoryOpertionEn;

public interface PlatNewsHistoryOpertionService {
	
	
	/**
	 * 中文操作记录插入
	 * @param platNewsHistoryOpertion
	 */
	public void insCnOpertion(PlatNewsHistoryOpertionCn platNewsHistoryOpertion) throws Exception;
	
	/**
	 * 英文操作记录插入
	 * @param platNewsHistoryOpertion
	 */
	public void insEnOpertion(PlatNewsHistoryOpertionEn platNewsHistoryOpertion) throws Exception;
	
	
	/**
	 * 中文操作记录
	 * @return
	 */
	public List<PlatNewsHistoryOpertionCn> findCnOpertion(PlatNewsHistoryOpertionCn cn);
	
	/**
	 * 英文操作记录
	 * @return
	 */
	public List<PlatNewsHistoryOpertionEn> findEnOpertion(PlatNewsHistoryOpertionEn en);
	
}
