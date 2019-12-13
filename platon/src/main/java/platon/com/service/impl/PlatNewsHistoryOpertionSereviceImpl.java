package platon.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import platon.com.dao.PlatNewsHistoryOpertionDao;
import platon.com.po.PlatNewsHistoryOpertionCn;
import platon.com.po.PlatNewsHistoryOpertionEn;
import platon.com.service.PlatNewsHistoryOpertionService;

@Service
public class PlatNewsHistoryOpertionSereviceImpl implements PlatNewsHistoryOpertionService {
	
	@Autowired
	private PlatNewsHistoryOpertionDao platNewsHistoryOpertionDao;

	@Override
	public void insCnOpertion(PlatNewsHistoryOpertionCn platNewsHistoryOpertion) throws Exception {
		platNewsHistoryOpertionDao.insCnOpertion(platNewsHistoryOpertion);
	}
	
	@Override
	public void insEnOpertion(PlatNewsHistoryOpertionEn platNewsHistoryOpertion) throws Exception {
		platNewsHistoryOpertionDao.insEnOpertion(platNewsHistoryOpertion);
	}

	@Override
	public List<PlatNewsHistoryOpertionCn> findCnOpertion(PlatNewsHistoryOpertionCn cn){
		return platNewsHistoryOpertionDao.findCnOpertion(cn);
	}

	@Override
	public List<PlatNewsHistoryOpertionEn> findEnOpertion(PlatNewsHistoryOpertionEn en){
		return platNewsHistoryOpertionDao.findEnOpertion(en);
	}
	
	
	
	
	
}
