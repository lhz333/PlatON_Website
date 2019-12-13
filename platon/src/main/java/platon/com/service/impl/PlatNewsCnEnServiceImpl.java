package platon.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import platon.com.dao.PlatNewsCnEnDao;
import platon.com.po.PlatNewsCnEn;
import platon.com.service.PlatNewsCnEnService;

@Service
public class PlatNewsCnEnServiceImpl  implements PlatNewsCnEnService{
	
	@Autowired
	private PlatNewsCnEnDao platNewsCnEnDao;


	@Override
	public Integer insPlatNewsCnEn(PlatNewsCnEn news) throws Exception {
		return platNewsCnEnDao.insPlatNewsCnEn(news);
	}



	@Override
	public PlatNewsCnEn findPlatNewsCnEn(PlatNewsCnEn news) throws Exception {
		return platNewsCnEnDao.findPlatNewsCnEn(news);
	}



	@Override
	public Boolean removeNewsCnEn(PlatNewsCnEn news) throws Exception {
		return platNewsCnEnDao.removeNewsCnEn(news);
	}
	
	
	
	
}
