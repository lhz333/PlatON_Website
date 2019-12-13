package platon.com.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import platon.com.dao.PlatMediaDao;
import platon.com.po.PlatMedia;
import platon.com.service.PlatMediaService;
import platon.com.util.Const;

import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;
@Service
public class PlatMediaServiceImpl  implements PlatMediaService{
	
	@Autowired
	private PlatMediaDao platMediaDao;
	
	
	@Override
	public int uptMediaStatus(PlatMedia media) throws Exception {
		if(Const.STATUS_TRUE.getType().intValue()==media.getStatus().intValue()){
			media.setReleaseTime(new Date());
		}
		return platMediaDao.uptPlatMediaStatus(media);
	}

	@Override
	public List<PlatMedia> findPlatMedia() {
		return platMediaDao.findPlatMedia();
	}

	@Override
	public Integer uptMedia(PlatMedia media) throws Exception {
		PlatMedia newMedia=platMediaDao.findPlatMediaById(media);
		
		newMedia.setName(media.getName());
		newMedia.setLogoUrl(media.getLogoUrl());
		newMedia.setReTime(media.getReTime());
		newMedia.setTitle(media.getTitle());
		newMedia.setLink(media.getLink());
		newMedia.setLanguage(media.getLanguage());
		return platMediaDao.uptMedia(newMedia);
	}

	@Override
	public PlatMedia findPlatMediaById(PlatMedia media) {
		return platMediaDao.findPlatMediaById(media);
	}

	@Override
	public Pagination<PlatMedia> findPlatMediaOnline(PlatMedia media) {
		return platMediaDao.findPlatMediaOnline(media, new Page(media.getPageNo(), media.getPageSize()));
	}

	@Override
	public Pagination<PlatMedia> findPlatMedia(PlatMedia media) {
		return platMediaDao.findPlatMedia(media,new Page(media.getPageNo(), media.getPageSize()));
	}

	@Override
	public Integer insMedia(PlatMedia media) throws Exception {
		return platMediaDao.insMedia(media);
	}

}
