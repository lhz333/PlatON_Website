package platon.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import platon.com.dao.PlatSubscriptionDao;
import platon.com.po.PlatSubscription;
import platon.com.service.PlatSubscriptionService;

import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

@Service
public class PlatSubscriptionServiceImpl implements PlatSubscriptionService{
	
	
	@Autowired
	private PlatSubscriptionDao platSubscriptionDao;
	
	/**
	 * 获取订阅列表
	 */
	public Pagination<PlatSubscription> findPlatSubscription(PlatSubscription platSubscription){
		Pagination<PlatSubscription> pageRes=platSubscriptionDao.findPlatSubscription(platSubscription,new Page(platSubscription.getPageNo(),platSubscription.getPageSize()));
		pageRes.setPageSize(platSubscription.getPageSize());
		if(pageRes.getTotalCount()>0){
			List<PlatSubscription> list=pageRes.getResult();
			int no=0;
			if((platSubscription.getPageNo()-1)*platSubscription.getPageSize()<=0){
				no=1;
			}else{
				no=(platSubscription.getPageNo()-1)*platSubscription.getPageSize()+1;
			}
			for (PlatSubscription p : list) {
				p.setDescNo(no++);
				if(p.getName()!=null && !"".equals(p.getName()))
					p.setName(p.getName().replace("\\'","'"));
				if(p.getCompanyName()!=null && !"".equals(p.getCompanyName()))
					p.setCompanyName(p.getCompanyName().replace("\\'","'"));
				if(p.getEmail()!=null && !"".equals(p.getEmail()))
					p.setEmail(p.getEmail().replace("\\'","'"));
				
			}
		}
		
		return pageRes;
	}
	

	@Override
	public List<PlatSubscription> findListPlatSubscription(
			PlatSubscription platSubscription) {
		List<PlatSubscription> list=platSubscriptionDao.findListPlatSubscription(platSubscription);
		int no=1;
		for (PlatSubscription platSubscription2 : list) {
			platSubscription2.setDescNo(no++);
			if(platSubscription2.getName()!=null && !"".equals(platSubscription2.getName()))
				platSubscription2.setName(platSubscription2.getName().replace("\\'","'"));
			if(platSubscription2.getCompanyName()!=null && !"".equals(platSubscription2.getCompanyName()))
				platSubscription2.setCompanyName(platSubscription2.getCompanyName().replace("\\'","'"));
			if(platSubscription2.getEmail()!=null && !"".equals(platSubscription2.getEmail()))
				platSubscription2.setEmail(platSubscription2.getEmail().replace("\\'","'"));
		}
		return list;
	}
	
	/**
	 * 新增订阅管理
	 * @param platSubscription
	 * @return
	 * @throws Exception
	 */
	public Integer insSubscription(PlatSubscription platSubscription) throws Exception{
		PlatSubscription p=platSubscriptionDao.findPlatSubscriptionById(platSubscription);
		if(p!=null)
			return null;
		return platSubscriptionDao.insSubscription(platSubscription);
	}
	
	/**
	 * 修改订阅管理
	 * @param platSubscription
	 * @return
	 * @throws Exception
	 */
	public Integer uptSubscription(PlatSubscription platSubscription) throws Exception{
		return platSubscriptionDao.uptSubscription(platSubscription);
	}


	@Override
	public Integer removeSubscription(PlatSubscription platSubscription)
			throws Exception {
		return platSubscriptionDao.removePlatSubscription(platSubscription);
	}

	
}
