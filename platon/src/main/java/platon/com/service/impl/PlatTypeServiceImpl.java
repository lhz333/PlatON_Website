package platon.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import platon.com.dao.PlatTypeDao;
import platon.com.po.PlatNewsCn;
import platon.com.po.PlatTypeCn;
import platon.com.po.PlatTypeEn;
import platon.com.service.PlatTypeService;

import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

@Service
public class PlatTypeServiceImpl implements PlatTypeService {
	
	@Autowired
	private PlatTypeDao platTypeDao;
	

	@Override
	public Integer insTypeCn(PlatTypeCn cn) throws Exception {
		return platTypeDao.insTypeCn(cn);
	}

	@Override
	public Integer insTypeEn(PlatTypeEn en) throws Exception {
		return platTypeDao.insTypeEn(en);
	}

	@Override
	public Pagination<PlatTypeCn> findPlatTypeCn(PlatTypeCn cn) {
		Integer pageNo=cn.getPageNo();
		Integer pageSize=cn.getPageSize();
		
		Pagination<PlatTypeCn> pageRes=platTypeDao.pagePlatTypeCn(cn, new Page(pageNo,pageSize));
		pageRes.setPageSize(pageSize);
		return pageRes;
		
	}

	@Override
	public Pagination<PlatTypeEn> findPlatTypeEn(PlatTypeEn en) {
		Integer pageNo=en.getPageNo();
		Integer pageSize=en.getPageSize();
		
		Pagination<PlatTypeEn> pageRes=platTypeDao.pagePlatTypeEn(en,  new Page(pageNo,pageSize));
		pageRes.setPageSize(pageSize);
		return pageRes;
	}

	@Override
	public List<PlatTypeEn> findPlatTypeEnByParams(PlatTypeEn en) {
		List<PlatTypeEn> list=platTypeDao.findPlatTypeEnByParams(en);
		return list;
	}

	@Override
	public List<PlatTypeCn> findPlatTypeCnByParams(PlatTypeCn en) {
		List<PlatTypeCn> list=platTypeDao.findPlatTypeCnByParams(en);
		return list;
	}

	@Override
	public int removePlatTypeCn(PlatTypeEn cn) throws Exception {
		return platTypeDao.removePlatTypeEn(cn);
	}

	@Override
	public int removePlatTypeCn(PlatTypeCn en) throws Exception {
		return platTypeDao.removePlatTypeCn(en);		
	}

	@Override
	public int uptPlatTypeEn(PlatTypeEn en) throws Exception {
		return platTypeDao.uptPlatTypeEn(en);
	}

	@Override
	public int uptPlatTypeCn(PlatTypeCn cn) throws Exception {
		return platTypeDao.uptPlatTypeCn(cn);
	}

	
	
}
