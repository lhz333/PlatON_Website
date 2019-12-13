package platon.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import platon.com.dao.PlatFaqHistoryOpertionDao;
import platon.com.dao.PlatNewsHistoryOpertionDao;
import platon.com.po.PlatFaqHistoryOpertion;
import platon.com.po.PlatNewsHistoryOpertionCn;
import platon.com.po.PlatNewsHistoryOpertionEn;
import platon.com.service.PlatFaqHistoryOpertionService;
import platon.com.service.PlatNewsHistoryOpertionService;

@Service
public class PlatFaqHistoryOpertionSereviceImpl implements PlatFaqHistoryOpertionService {
	
	@Autowired
	private PlatFaqHistoryOpertionDao platFaqHistoryOpertionDao;

	@Override
	public void insFaqOpertion(PlatFaqHistoryOpertion faq) throws Exception {
		platFaqHistoryOpertionDao.insFaqOpertion(faq);
	}

	@Override
	public List<PlatFaqHistoryOpertion> findCnOpertion(
			PlatFaqHistoryOpertion faq) {
		return platFaqHistoryOpertionDao.findFaqOpertion(faq);
	}

	
	
	
	
}
