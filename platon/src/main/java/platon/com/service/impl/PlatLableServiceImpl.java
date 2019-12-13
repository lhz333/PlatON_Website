package platon.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import platon.com.dao.PlatLableDao;
import platon.com.po.PlatLable;
import platon.com.service.PlatLableService;

@Service
public class PlatLableServiceImpl implements PlatLableService{

	@Autowired
	private PlatLableDao platLableDao;
	
	@Override
	public Integer saveLable(PlatLable lable) throws Exception{
		
		PlatLable res=platLableDao.findLableByName(lable);
		if(res!=null && res.getId()>0)
			return res.getId();
		
		return platLableDao.saveLable(lable);
		
	}

}
