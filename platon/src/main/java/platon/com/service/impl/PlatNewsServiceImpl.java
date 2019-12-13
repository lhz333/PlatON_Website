package platon.com.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import platon.com.dao.PlatLableDao;
import platon.com.dao.PlatNewsDao;
import platon.com.dao.PlatTypeDao;
import platon.com.po.PlatLable;
import platon.com.po.PlatNewLable;
import platon.com.po.PlatNews;
import platon.com.po.PlatNewsCn;
import platon.com.po.PlatNewsCnEn;
import platon.com.po.PlatNewsEn;
import platon.com.po.PlatNewsHistoryOpertionCn;
import platon.com.po.PlatNewsHistoryOpertionEn;
import platon.com.po.PlatTypeCn;
import platon.com.po.PlatTypeEn;
import platon.com.po.PlatUser;
import platon.com.service.PlatLableService;
import platon.com.service.PlatNewsHistoryOpertionService;
import platon.com.service.PlatNewsService;
import platon.com.util.Const;

import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

@Service
public class PlatNewsServiceImpl implements PlatNewsService {

	@Autowired
	private PlatNewsDao platNewsDao;

	@Autowired
	private PlatLableDao platLableDao;
	
	@Autowired
	private PlatLableService  platLableService;
	
	@Autowired
	private PlatTypeDao platTypeDao;
	@Autowired
	private PlatNewsHistoryOpertionService platNewsHistoryOpertionService;
	
	
	@Override
	public Pagination<PlatNewsCn> findPlatNewsCn(PlatNewsCn news) {

		Pagination<PlatNewsCn> pageRes = platNewsDao.findPlatNewsCn(news,
				new Page(news.getPageNo(), news.getPageSize()));
		pageRes.setPageSize(news.getPageSize());
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsCn> list = pageRes.getResult();

			int no = 0;
			if ((news.getPageNo() - 1) * news.getPageSize() <= 0) {
				no = 1;
			} else {
				no = (news.getPageNo() - 1) * news.getPageSize() + 1;
			}
			for (PlatNewsCn platNewsCn : list) {
				platNewsCn.setDescNo(no++);
				if (platNewsCn.getTitle() != null
						&& !"".equals(platNewsCn.getTitle())) {
					platNewsCn.setTitle(platNewsCn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsCn.getNewsText() != null
						&& !"".equals(platNewsCn.getNewsText())) {
					platNewsCn.setNewsText(platNewsCn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsCn.getAuthor() != null
						&& !"".equals(platNewsCn.getAuthor())) {
					platNewsCn.setAuthor(platNewsCn.getAuthor().replace("\\'",
							"'"));
				}
			}
		}

		return pageRes;
	}

	@Override
	public Pagination<PlatNewsEn> findPlatNewsEn(PlatNewsEn news) {

		Pagination<PlatNewsEn> pageRes = platNewsDao.findPlatNewsEn(news,
				new Page(news.getPageNo(), news.getPageSize()));
		pageRes.setPageSize(news.getPageSize());
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsEn> list = pageRes.getResult();
			int no = 0;
			if ((news.getPageNo() - 1) * news.getPageSize() <= 0) {
				no = 1;
			} else {
				no = (news.getPageNo() - 1) * news.getPageSize() + 1;
			}
			for (PlatNewsEn platNewsEn : list) {
				platNewsEn.setDescNo(no++);
				if (platNewsEn.getTitle() != null
						&& !"".equals(platNewsEn.getTitle())) {
					platNewsEn.setTitle(platNewsEn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsEn.getNewsText() != null
						&& !"".equals(platNewsEn.getNewsText())) {
					platNewsEn.setNewsText(platNewsEn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsEn.getAuthor() != null
						&& !"".equals(platNewsEn.getAuthor())) {
					platNewsEn.setAuthor(platNewsEn.getAuthor().replace("\\'",
							"'"));
				}
			}
		}

		return pageRes;
	}
	@Transactional
	@Override
	public Integer insNewsCn(PlatNewsCn news,PlatUser u) throws Exception {
		news.setSortId(platNewsDao.findMaxCnId() + 1);
		Integer cnId=platNewsDao.insNewsCn(news);
		/**
		 * 新增标签
		 * 和对应关系
		 */
		if(news.getLables()!=null && news.getLables().size()>0){
			List<PlatNewLable> newLableList=new ArrayList<PlatNewLable>();
			for (PlatLable lable : news.getLables()) {
				Integer lableId=platLableService.saveLable(lable);
				PlatNewLable newLable=new PlatNewLable();
				newLable.setLableId(lableId);
				newLable.setNewCnId(cnId);
				newLableList.add(newLable);
			}
			platLableDao.saveNewLable(newLableList);
		}
		
		PlatNewsHistoryOpertionCn platNewsHistoryOpertion=new PlatNewsHistoryOpertionCn();
		platNewsHistoryOpertion.setNewId(news.getId());
		platNewsHistoryOpertion.setCreator(u.getName());
		platNewsHistoryOpertion.setStatus(Const.opertion_create.getType());
		platNewsHistoryOpertion.setCreateTime(new Date());
		platNewsHistoryOpertionService.insCnOpertion(platNewsHistoryOpertion);
		
		
		
		return cnId;
	}
	@Transactional
	@Override
	public Integer insNewsEn(PlatNewsEn news,PlatUser u) throws Exception {
		news.setSortId(platNewsDao.findMaxEnId() + 1);
		Integer enId=platNewsDao.insNewsEn(news);
		/**
		 * 新增标签
		 * 和对应关系
		 */
		if(news.getLables()!=null && news.getLables().size()>0){
			List<PlatNewLable> newLableList=new ArrayList<PlatNewLable>();
			for (PlatLable lable : news.getLables()) {
				Integer lableId=platLableService.saveLable(lable);
				PlatNewLable newLable=new PlatNewLable();
				newLable.setLableId(lableId);
				newLable.setNewEnId(enId);
				newLableList.add(newLable);
			}
			platLableDao.saveNewLable(newLableList);
		}
		
		PlatNewsHistoryOpertionEn platNewsHistoryOpertion=new PlatNewsHistoryOpertionEn();
		platNewsHistoryOpertion.setNewId(news.getId());
		platNewsHistoryOpertion.setCreator(u.getName());
		platNewsHistoryOpertion.setStatus(Const.opertion_create.getType());
		platNewsHistoryOpertion.setCreateTime(new Date());
		platNewsHistoryOpertionService.insEnOpertion(platNewsHistoryOpertion);
		
		
		return enId;
		
	}
	
	@Transactional
	@Override
	public Integer uptNewsCn(PlatNewsCn news,PlatUser u) throws Exception {
		PlatNewsCn cn = platNewsDao.findPlatNewsCnById(news);
		if (cn != null) {
			cn.setImgUrl(news.getImgUrl());
			cn.setNewsText(news.getNewsText());
			cn.setTitle(news.getTitle());
			cn.setReleaseTime(news.getReleaseTime());
			cn.setTopImgUrl(news.getTopImgUrl());
			cn.setAuthor(news.getAuthor());
			cn.setComplete(news.getComplete());
			cn.setNewsDescription(news.getNewsDescription());
			cn.setTypeId(news.getTypeId());
			Integer res=platNewsDao.uptNewsCn(cn);
			
			//删除对应关系
			PlatNewLable rmLable=new PlatNewLable();
			rmLable.setNewCnId(cn.getId());
			platLableDao.removeNewLable(rmLable);
			
			//添加新对应关系
			
			/**
			 * 新增标签
			 * 和对应关系
			 */
			if(news.getLables()!=null && news.getLables().size()>0){
				List<PlatNewLable> newLableList=new ArrayList<PlatNewLable>();
				for (PlatLable lable : news.getLables()) {
					Integer lableId=platLableService.saveLable(lable);
					PlatNewLable newLable=new PlatNewLable();
					newLable.setLableId(lableId);
					newLable.setNewCnId(cn.getId());
					newLableList.add(newLable);
				}
				platLableDao.saveNewLable(newLableList);
			}
			
			
			
			//添加操作记录
			PlatNewsHistoryOpertionCn platNewsHistoryOpertion=new PlatNewsHistoryOpertionCn();
			platNewsHistoryOpertion.setNewId(news.getId());
			platNewsHistoryOpertion.setCreator(u.getName());
			platNewsHistoryOpertion.setStatus(Const.opertion_edit.getType());
			platNewsHistoryOpertion.setCreateTime(new Date());
			platNewsHistoryOpertionService.insCnOpertion(platNewsHistoryOpertion);
			
			
			return res;
		}
		return null;

	}

	@Transactional
	@Override
	public Integer uptNewsEn(PlatNewsEn news,PlatUser u) throws Exception {
		PlatNewsEn cn = platNewsDao.findPlatNewsEnById(news);
		if (cn != null) {
			cn.setImgUrl(news.getImgUrl());
			cn.setNewsText(news.getNewsText());
			cn.setTitle(news.getTitle());
			cn.setReleaseTime(news.getReleaseTime());
			cn.setTopImgUrl(news.getTopImgUrl());
			cn.setAuthor(news.getAuthor());
			cn.setComplete(news.getComplete());
			cn.setNewsDescription(news.getNewsDescription());
			cn.setTypeId(news.getTypeId());
			Integer res=platNewsDao.uptNewsEn(cn);
			
			
			//删除对应关系
			PlatNewLable rmLable=new PlatNewLable();
			rmLable.setNewEnId(cn.getId());
			platLableDao.removeNewLable(rmLable);
			
			//添加新对应关系
			
			/**
			 * 新增标签
			 * 和对应关系
			 */
			if(news.getLables()!=null && news.getLables().size()>0){
				List<PlatNewLable> newLableList=new ArrayList<PlatNewLable>();
				for (PlatLable lable : news.getLables()) {
					Integer lableId=platLableService.saveLable(lable);
					PlatNewLable newLable=new PlatNewLable();
					newLable.setLableId(lableId);
					newLable.setNewEnId(cn.getId());
					newLableList.add(newLable);
				}
				platLableDao.saveNewLable(newLableList);
			}
			
			
			//添加操作记录
			PlatNewsHistoryOpertionEn platNewsHistoryOpertion=new PlatNewsHistoryOpertionEn();
			platNewsHistoryOpertion.setNewId(news.getId());
			platNewsHistoryOpertion.setCreator(u.getName());
			platNewsHistoryOpertion.setStatus(Const.opertion_edit.getType());
			platNewsHistoryOpertion.setCreateTime(new Date());
			platNewsHistoryOpertionService.insEnOpertion(platNewsHistoryOpertion);
			
			
			return res;
			
		}
		return null;
	}

	@Override
	public PlatNewsCn findPlatNewsCnById(PlatNewsCn news) throws Exception {
		/*
		 * 获取单个新闻，需要拿到上一篇和下一篇id
		 */
		PlatNewsCn cn = platNewsDao.findPlatNewsCnById(news);
//		if (cn != null && cn.getSortId() != null) {
//			PlatNewsCn sort = platNewsDao.findPlatNewsCnUpOrNext(cn);
//			if (sort != null) {
//				cn.setNextSortId(sort.getNextSortId());
//				cn.setUpSortId(sort.getUpSortId());
//			}
//			
//		}
		if(cn==null)
			return null;
		PlatNewLable lable=new PlatNewLable();
		lable.setNewCnId(news.getId());
		List<PlatLable> lables=platLableDao.finfLablesByNewsId(lable);
		if(lable!=null)
			cn.setLables(lables);

		return cn;
	}

	@Override
	public PlatNewsEn findPlatNewsEnById(PlatNewsEn news) throws Exception {
		/*
		 * 获取单个新闻，需要拿到上一篇和下一篇id
		 */
		PlatNewsEn en = platNewsDao.findPlatNewsEnById(news);
//		if (en != null && en.getSortId() != null) {
//			PlatNewsEn sort = platNewsDao.findPlatNewsEnUpOrNext(en);
//			if (sort != null) {
//				en.setNextSortId(sort.getNextSortId());
//				en.setUpSortId(sort.getUpSortId());
//			}
//		}
		if(en==null)
			return null;
		PlatNewLable lable=new PlatNewLable();
		lable.setNewEnId(news.getId());
		List<PlatLable> lables=platLableDao.finfLablesByNewsId(lable);
		if(lable!=null)
			en.setLables(lables);
		return en;
	}
	
	@Transactional
	@Override
	public Integer uptNewsCnStatus(PlatNewsCn news,PlatUser u) throws Exception {
		Integer res= platNewsDao.uptNewsCnStatus(news);
		
		PlatNewsHistoryOpertionCn platNewsHistoryOpertion=new PlatNewsHistoryOpertionCn();
		platNewsHistoryOpertion.setNewId(news.getId());
		platNewsHistoryOpertion.setCreator(u.getName());
		if(news.getStatus()==0){
			platNewsHistoryOpertion.setStatus(Const.opertion_revoke.getType());
		}else if(news.getStatus()==2){
			platNewsHistoryOpertion.setStatus(Const.opertion_del.getType());
		}else{
			platNewsHistoryOpertion.setStatus(news.getStatus());
		}
		platNewsHistoryOpertion.setCreateTime(new Date());
		platNewsHistoryOpertionService.insCnOpertion(platNewsHistoryOpertion);
		
		
		return res;
	}

	@Transactional
	@Override
	public Integer uptNewsEnStatus(PlatNewsEn news,PlatUser u) throws Exception {
		Integer res= platNewsDao.uptNewsEnStatus(news);
		
		PlatNewsHistoryOpertionEn platNewsHistoryOpertion=new PlatNewsHistoryOpertionEn();
		platNewsHistoryOpertion.setNewId(news.getId());
		platNewsHistoryOpertion.setCreator(u.getName());
		if(news.getStatus()==0){
			platNewsHistoryOpertion.setStatus(Const.opertion_revoke.getType());
		}else if(news.getStatus()==2){
			platNewsHistoryOpertion.setStatus(Const.opertion_del.getType());
		}else{
			platNewsHistoryOpertion.setStatus(news.getStatus());
		}
		platNewsHistoryOpertion.setCreateTime(new Date());
		platNewsHistoryOpertionService.insEnOpertion(platNewsHistoryOpertion);
		return res;
		
		
	}

	@Transactional
	@Override
	public Integer sortNewsCnUporDown(PlatNewsCn news) throws Exception {
		PlatNewsCn next = platNewsDao.findPlatNewsCnByStatus(news);
		if (next != null && next.getId() > 0) {
			int temp = next.getSortId();
			next.setSortId(news.getSortId());
			news.setSortId(temp);
			platNewsDao.sortNewsCn(news);
			platNewsDao.sortNewsCn(next);
		}
		return 1;
	}

	@Transactional
	@Override
	public Integer sortNewsEnUporDown(PlatNewsEn news) throws Exception {
		PlatNewsEn next = platNewsDao.findPlatNewsEnByStatus(news);
		if (next != null && next.getId() > 0) {
			int temp = next.getSortId();
			next.setSortId(news.getSortId());
			news.setSortId(temp);
			platNewsDao.sortNewsEn(news);
			platNewsDao.sortNewsEn(next);
		}
		return 1;
	}

	@Override
	public Pagination<PlatNewsCn> findNotBindPlatNewsCn(PlatNewsCn news) {
		Pagination<PlatNewsCn> pageRes = platNewsDao.findNotBindPlatNewsCn(
				news, new Page(news.getPageNo(), news.getPageSize()));
		pageRes.setPageSize(news.getPageSize());
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsCn> list = pageRes.getResult();
			int no = 0;
			if ((news.getPageNo() - 1) * news.getPageSize() <= 0) {
				no = 1;
			} else {
				no = (news.getPageNo() - 1) * news.getPageSize() + 1;
			}
			for (PlatNewsCn platNewsCn : list) {
				platNewsCn.setDescNo(no++);
				if (platNewsCn.getTitle() != null
						&& !"".equals(platNewsCn.getTitle())) {
					platNewsCn.setTitle(platNewsCn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsCn.getNewsText() != null
						&& !"".equals(platNewsCn.getNewsText())) {
					platNewsCn.setNewsText(platNewsCn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsCn.getAuthor() != null
						&& !"".equals(platNewsCn.getAuthor())) {
					platNewsCn.setAuthor(platNewsCn.getAuthor().replace("\\'",
							"'"));
				}
			}
		}

		return pageRes;
	}

	@Override
	public Pagination<PlatNewsEn> findNotBindPlatNewsEn(PlatNewsEn news) {
		Pagination<PlatNewsEn> pageRes = platNewsDao.findNotBindPlatNewsEn(
				news, new Page(news.getPageNo(), news.getPageSize()));
		pageRes.setPageSize(news.getPageSize());
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsEn> list = pageRes.getResult();
			int no = 0;
			if ((news.getPageNo() - 1) * news.getPageSize() <= 0) {
				no = 1;
			} else {
				no = (news.getPageNo() - 1) * news.getPageSize() + 1;
			}
			for (PlatNewsEn platNewsEn : list) {
				platNewsEn.setDescNo(no++);
				if (platNewsEn.getTitle() != null
						&& !"".equals(platNewsEn.getTitle())) {
					platNewsEn.setTitle(platNewsEn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsEn.getNewsText() != null
						&& !"".equals(platNewsEn.getNewsText())) {
					platNewsEn.setNewsText(platNewsEn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsEn.getAuthor() != null
						&& !"".equals(platNewsEn.getAuthor())) {
					platNewsEn.setAuthor(platNewsEn.getAuthor().replace("\\'",
							"'"));
				}
			}
		}

		return pageRes;
	}

	@Override
	public PlatNews findNewsDtail(PlatNewsCnEn news) {
		return platNewsDao.findNewsDtail(news);
	}

	@Override
	public Pagination<PlatNewsCn> findPlatNewsCnOnline(PlatNewsCn news) {
		Integer pageNo = news.getPageNo();
		Integer pageSize = news.getPageSize();

		PlatNewsCn cn = platNewsDao.findTopFindPlatNewsCn();

		Pagination<PlatNewsCn> pageRes = platNewsDao.findPlatNewsCnOnline(news,
				new Page(pageNo, pageSize));
		pageRes.setPageSize(news.getPageSize());
		int no = (pageNo - 1) * news.getPageSize() == 0 ? 1
				: (news.getPageNo() - 1) * news.getPageSize();
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsCn> list = pageRes.getResult();
			List<PlatNewsCn> res = new ArrayList<PlatNewsCn>();
			cn.setDescNo(no++);
			res.add(cn);
			for (PlatNewsCn platNewsCn : list) {
				platNewsCn.setDescNo(no++);
				if (platNewsCn.getTitle() != null
						&& !"".equals(platNewsCn.getTitle())) {
					platNewsCn.setTitle(platNewsCn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsCn.getNewsText() != null
						&& !"".equals(platNewsCn.getNewsText())) {
					platNewsCn.setNewsText(platNewsCn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsCn.getAuthor() != null
						&& !"".equals(platNewsCn.getAuthor())) {
					platNewsCn.setAuthor(platNewsCn.getAuthor().replace("\\'",
							"'"));
				}
				res.add(platNewsCn);
			}
			pageRes.setResult(res);
		} else {
			if (cn != null) {
				pageRes = new Pagination<>(pageNo, pageSize);
				List<PlatNewsCn> res = new ArrayList<PlatNewsCn>();
				if (cn.getTitle() != null && !"".equals(cn.getTitle())) {
					cn.setTitle(cn.getTitle().replace("\\'", "'"));
				}
				if (cn.getNewsText() != null && !"".equals(cn.getNewsText())) {
					cn.setNewsText(cn.getNewsText().replace("\\'", "'"));
				}
				if (cn.getAuthor() != null && !"".equals(cn.getAuthor())) {
					cn.setAuthor(cn.getAuthor().replace("\\'", "'"));
				}
				res.add(cn);
				pageRes.setResult(res);
				pageRes.setTotalCount(1);
			}
		}

		return pageRes;

	}

	@Override
	public Pagination<PlatNewsEn> findPlatNewsEnOnline(PlatNewsEn news) {
		Integer pageNo = news.getPageNo();
		Integer pageSize = news.getPageSize();

		PlatNewsEn cn = platNewsDao.findTopFindPlatNewsEn();

		Pagination<PlatNewsEn> pageRes = platNewsDao.findPlatNewsEnOnline(news,
				new Page(pageNo, pageSize));
		pageRes.setPageSize(news.getPageSize());
		int no = 0;
		if ((news.getPageNo() - 1) * news.getPageSize() <= 0) {
			no = 1;
		} else {
			no = (news.getPageNo() - 1) * news.getPageSize() + 1;
		}
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsEn> list = pageRes.getResult();
			List<PlatNewsEn> res = new ArrayList<PlatNewsEn>();
			cn.setDescNo(no++);
			res.add(cn);
			for (PlatNewsEn platNewsEn : list) {
				platNewsEn.setDescNo(no++);
				if (platNewsEn.getTitle() != null
						&& !"".equals(platNewsEn.getTitle())) {
					platNewsEn.setTitle(platNewsEn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsEn.getNewsText() != null
						&& !"".equals(platNewsEn.getNewsText())) {
					platNewsEn.setNewsText(platNewsEn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsEn.getAuthor() != null
						&& !"".equals(platNewsEn.getAuthor())) {
					platNewsEn.setAuthor(platNewsEn.getAuthor().replace("\\'",
							"'"));
				}
				res.add(platNewsEn);
			}
			pageRes.setResult(res);
		} else {
			if (cn != null) {
				pageRes = new Pagination<>(pageNo, pageSize);
				List<PlatNewsEn> res = new ArrayList<PlatNewsEn>();
				if (cn.getTitle() != null && !"".equals(cn.getTitle())) {
					cn.setTitle(cn.getTitle().replace("\\'", "'"));
				}
				if (cn.getNewsText() != null && !"".equals(cn.getNewsText())) {
					cn.setNewsText(cn.getNewsText().replace("\\'", "'"));
				}
				if (cn.getAuthor() != null && !"".equals(cn.getAuthor())) {
					cn.setAuthor(cn.getAuthor().replace("\\'", "'"));
				}
				res.add(cn);
				pageRes.setResult(res);
				pageRes.setTotalCount(1);
			}
		}

		return pageRes;

	}

	@Override
	public Pagination<PlatNewsCn> findPlatNewsCnMobileOnline(PlatNewsCn news) {
		/**
		 * 如果是根据分类名称查询，需要根据分类名称获取分类id
		 * 如果是观点名称，则过滤新闻和活动，获取其他数据
		 */
		if(news!=null && news.getTypeName()!=null && !"".equals(news.getTypeName())){
			/**
			 * 观点包括不确定的新闻，所以首先获取到
			 * 新闻and 活动
			 */
			if(!"新闻".equals(news.getTypeName()) && !"活动".equals(news.getTypeName()) ){
				List<PlatTypeCn> types=new ArrayList<PlatTypeCn>(); 
				PlatTypeCn cn=new PlatTypeCn();
				cn.setName("新闻");
				List<PlatTypeCn> ens=null;
				ens=platTypeDao.findPlatTypeCnByParams(cn);
				if(ens!=null && ens.size()>0){
					types.add(ens.get(0));
				}
				cn.setName("活动");
				ens=platTypeDao.findPlatTypeCnByParams(cn);
				if(ens!=null && ens.size()>0){
					types.add(ens.get(0));
				}
				news.setTypsList(types);
			}else{
				PlatTypeCn cn=new PlatTypeCn();
				cn.setName(news.getTypeName());
				List<PlatTypeCn> ens=platTypeDao.findPlatTypeCnByParams(cn);
				if(ens!=null && ens.size()>0){
					news.setTypeId(ens.get(0).getId());
				}else{
					news.setTypeId(-1);
				}
			}
			
		}
		
		
		Pagination<PlatNewsCn> pageRes = platNewsDao
				.findPlatNewsCnMobileOnline(news, new Page(news.getPageNo(),
						news.getPageSize()));
		pageRes.setPageSize(news.getPageSize());
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsCn> list = pageRes.getResult();

			int no = 0;
			if ((news.getPageNo() - 1) * news.getPageSize() <= 0) {
				no = 1;
			} else {
				no = (news.getPageNo() - 1) * news.getPageSize() + 1;
			}
			for (PlatNewsCn platNewsCn : list) {
				platNewsCn.setDescNo(no++);
				if (platNewsCn.getTitle() != null
						&& !"".equals(platNewsCn.getTitle())) {
					platNewsCn.setTitle(platNewsCn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsCn.getNewsText() != null
						&& !"".equals(platNewsCn.getNewsText())) {
					platNewsCn.setNewsText(platNewsCn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsCn.getAuthor() != null
						&& !"".equals(platNewsCn.getAuthor())) {
					platNewsCn.setAuthor(platNewsCn.getAuthor().replace("\\'",
							"'"));
				}
			}
		}

		return pageRes;
	}

	@Override
	public Pagination<PlatNewsEn> findPlatNewsEnMobileOnline(PlatNewsEn news) {
		
		/**
		 * 如果是根据分类名称查询，需要根据分类名称获取分类id
		 * 如果是观点名称，则过滤新闻和活动，获取其他数据
		 */
		if(news!=null && news.getTypeName()!=null && !"".equals(news.getTypeName())){
			/**
			 * 观点包括不确定的新闻，所以首先获取到
			 * 新闻and 活动
			 */
			if(!"News".equals(news.getTypeName()) && !"Events".equals(news.getTypeName())){
				List<PlatTypeEn> types=new ArrayList<PlatTypeEn>(); 
				PlatTypeEn cn=new PlatTypeEn();
				cn.setName("News");
				List<PlatTypeEn> ens=null;
				ens=platTypeDao.findPlatTypeEnByParams(cn);
				if(ens!=null && ens.size()>0){
					types.add(ens.get(0));
				}
				cn.setName("Events");
				ens=platTypeDao.findPlatTypeEnByParams(cn);
				if(ens!=null && ens.size()>0){
					types.add(ens.get(0));
				}
				news.setTypsList(types);
			}else{
				PlatTypeEn cn=new PlatTypeEn();
				cn.setName(news.getTypeName());
				List<PlatTypeEn> ens=platTypeDao.findPlatTypeEnByParams(cn);
				if(ens!=null && ens.size()>0){
					news.setTypeId(ens.get(0).getId());	
				}else{
					news.setTypeId(-1);
				}
			}
			
		}
		
		
		Pagination<PlatNewsEn> pageRes = platNewsDao
				.findPlatNewsEnMobileOnline(news, new Page(news.getPageNo(),
						news.getPageSize()));
		pageRes.setPageSize(news.getPageSize());
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsEn> list = pageRes.getResult();
			int no = 0;
			if ((news.getPageNo() - 1) * news.getPageSize() <= 0) {
				no = 1;
			} else {
				no = (news.getPageNo() - 1) * news.getPageSize() + 1;
			}
			for (PlatNewsEn platNewsEn : list) {
				platNewsEn.setDescNo(no++);
				if (platNewsEn.getTitle() != null
						&& !"".equals(platNewsEn.getTitle())) {
					platNewsEn.setTitle(platNewsEn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsEn.getNewsText() != null
						&& !"".equals(platNewsEn.getNewsText())) {
					platNewsEn.setNewsText(platNewsEn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsEn.getAuthor() != null
						&& !"".equals(platNewsEn.getAuthor())) {
					platNewsEn.setAuthor(platNewsEn.getAuthor().replace("\\'",
							"'"));
				}
			}
		}

		return pageRes;
	}

	@Override
	public List<PlatNewsCn> findPlatCnByTypeId(Integer id) {
		return platNewsDao.findPlatCnByTypeId(id);
	}

	@Override
	public List<PlatNewsEn> findPlatEnByTypeId(Integer id) {
		return platNewsDao.findPlatEnByTypeId(id);
	}

	@Override
	public List<PlatNewsCn> findPlatNewsCnByLableId(PlatNews news) {
		List<Integer> ids=news.getLableIds();
		Integer length=news.getLength();
		
		Map<Integer, PlatNewsCn> map = new HashMap<Integer, PlatNewsCn>();
		List<PlatNewsCn> sortCn=new ArrayList<>();
		List<PlatNewsCn> result=new ArrayList<>();
		
		//相关阅读，首先根据标签获取新闻
		for (Integer integer : ids) {
			List<PlatNewsCn> list = platNewsDao
					.findPlatNewsCnByLableId(integer,news.getStatus());
			if (list != null && list.size() > 0) {
				for (PlatNewsCn platNewsCn : list) {
					if(news.getId().intValue()!=platNewsCn.getId().intValue()){
						if (!map.containsKey(platNewsCn.getId())) {
							map.put(platNewsCn.getId(), platNewsCn);
						}
					}
				}
			}
		}
		//如果标签没有相关信息，则通过分类获取、
		if(map.keySet().size()==0 || map.keySet().size()<length){
			List<PlatNewsCn> list =platNewsDao.findPlatCnByTypeIdOnline(news.getTypeId());
			for (PlatNewsCn platNewsCn : list) {
				if(news.getId().intValue()!=platNewsCn.getId().intValue()){
					if (!map.containsKey(platNewsCn.getId())) {
						map.put(platNewsCn.getId(), platNewsCn);
					}
				}
			}
		}
		
		
		//如果分类也没有先关信息，则获取全部的分类，取最近的新闻。
		if(map.keySet().size()==0 || map.keySet().size()<length){
			List<PlatNewsCn> list =platNewsDao.findTopNewsCn(length);
			for (PlatNewsCn platNewsCn : list) {
				if(news.getId().intValue()!=platNewsCn.getId().intValue()){
					if (!map.containsKey(platNewsCn.getId())) {
						map.put(platNewsCn.getId(), platNewsCn);
					}
				}
			}
		}
		
		
		
		for (Integer i : map.keySet()) {
			sortCn.add(map.get(i));
		}
		if(sortCn!=null && sortCn.size()>0){
			// sory 排序
			if(news.getStatus()!=null)
				dateSortC(sortCn);
			Integer l=length>sortCn.size()?sortCn.size():length;
			for (int i = 0; i < l; i++) {
				result.add(sortCn.get(i));
			}
		}
		return result;
	}

	@Override
	public List<PlatNewsEn> findPlatNewsEnByLableId(PlatNews news) {
		List<Integer> ids=news.getLableIds();
		Integer length=news.getLength();
		Map<Integer, PlatNewsEn> map = new HashMap<Integer, PlatNewsEn>();
		List<PlatNewsEn> sortEn=new ArrayList<>();
		List<PlatNewsEn> result=new ArrayList<>();
		for (Integer integer : ids) {
			List<PlatNewsEn> list = platNewsDao
					.findPlatNewsEnByLableId(integer,news.getStatus());
			if (list != null && list.size() > 0) {
				for (PlatNewsEn platNewsEn : list) {
					if(news.getId().intValue()!=platNewsEn.getId().intValue()){
						if (!map.containsKey(platNewsEn.getId())) {
							map.put(platNewsEn.getId(), platNewsEn);
						}
					}
				}
			}
		}
		
		//如果标签没有相关信息，则通过分类获取、
		if(map.keySet().size()==0 || map.keySet().size()<length){
			List<PlatNewsEn> list =platNewsDao.findPlatEnByTypeIdOnline(news.getTypeId());
			for (PlatNewsEn platNewsEn : list) {
				if(news.getId().intValue()!=platNewsEn.getId().intValue()){
					if (!map.containsKey(platNewsEn.getId())) {
						map.put(platNewsEn.getId(), platNewsEn);
					}
				}
			}
		}
		
		
		//如果分类也没有先关信息，则获取全部的分类，取最近的新闻。
		if(map.keySet().size()==0 || map.keySet().size()<length){
			List<PlatNewsEn> list =platNewsDao.findTopNewsEn(length);
			for (PlatNewsEn platNewsEn : list) {
				if(news.getId().intValue()!=platNewsEn.getId().intValue()){
					if (!map.containsKey(platNewsEn.getId())) {
						map.put(platNewsEn.getId(), platNewsEn);
					}
				}
			}
		}
		
	
		for (Integer i : map.keySet()) {
			sortEn.add(map.get(i));
		}
		if(sortEn!=null && sortEn.size()>0){
			// sory 排序
			if(news.getStatus()!=null)
				dateSortE(sortEn);
			Integer l=length>sortEn.size()?sortEn.size():length;
			for (int i = 0; i <l ; i++) {
				result.add(sortEn.get(i));
			}
		}
		return result;
	}

	/**
	 * 冒泡算法 根据对象时间排序
	 * @param a
	 * @return
	 */
	public static List<PlatNewsCn> dateSortC(List<PlatNewsCn> a) {
		int len = a.size();
		for (int i = len - 1; i >= 1; i--) {
			for (int j = 0; j <= i - 1; j++) {
				PlatNewsCn p1 = a.get(i);
				PlatNewsCn p2 = a.get(j);
				if (p1.getReleaseTime().before(p2.getReleaseTime())) {
					a.set(j, p1);
					a.set(i, p2);
				}
			}
		}
		return a;

	}
	/**
	 * 冒泡算法 根据对象时间排序
	 * @param a
	 * @return
	 */
	public static List<PlatNewsEn> dateSortE(List<PlatNewsEn> a) {
		int len = a.size();
		for (int i = len - 1; i >= 1; i--) {
			for (int j = 0; j <= i - 1; j++) {
				PlatNewsEn p1 = a.get(i);
				PlatNewsEn p2 = a.get(j);
				if (p1.getReleaseTime().before(p2.getReleaseTime())) {
					a.set(j, p1);
					a.set(i, p2);
				}
			}
		}
		return a;

	}

	@Override
	public Pagination<PlatNewsCn> pagePlatNewsCnByLableId(PlatNews news) {
		Pagination<PlatNewsCn> pageRes = platNewsDao
				.pagePlatNewsCnByLableId(news, new Page(news.getPageNo(),
						news.getPageSize()));
		pageRes.setPageSize(news.getPageSize());
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsCn> list = pageRes.getResult();

			int no = 0;
			if ((news.getPageNo() - 1) * news.getPageSize() <= 0) {
				no = 1;
			} else {
				no = (news.getPageNo() - 1) * news.getPageSize() + 1;
			}
			for (PlatNewsCn platNewsCn : list) {
				platNewsCn.setDescNo(no++);
				if (platNewsCn.getTitle() != null
						&& !"".equals(platNewsCn.getTitle())) {
					platNewsCn.setTitle(platNewsCn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsCn.getNewsText() != null
						&& !"".equals(platNewsCn.getNewsText())) {
					platNewsCn.setNewsText(platNewsCn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsCn.getAuthor() != null
						&& !"".equals(platNewsCn.getAuthor())) {
					platNewsCn.setAuthor(platNewsCn.getAuthor().replace("\\'",
							"'"));
				}
			}
		}

		return pageRes;
	}

	@Override
	public Pagination<PlatNewsEn> pagePlatNewsEnByLableId(PlatNews news) {
		Pagination<PlatNewsEn> pageRes = platNewsDao
				.pagePlatNewsEnByLableId(news, new Page(news.getPageNo(),
						news.getPageSize()));
		pageRes.setPageSize(news.getPageSize());
		if (pageRes.getTotalCount() > 0) {
			List<PlatNewsEn> list = pageRes.getResult();

			int no = 0;
			if ((news.getPageNo() - 1) * news.getPageSize() <= 0) {
				no = 1;
			} else {
				no = (news.getPageNo() - 1) * news.getPageSize() + 1;
			}
			for (PlatNewsEn platNewsCn : list) {
				platNewsCn.setDescNo(no++);
				if (platNewsCn.getTitle() != null
						&& !"".equals(platNewsCn.getTitle())) {
					platNewsCn.setTitle(platNewsCn.getTitle().replace("\\'",
							"'"));
				}
				if (platNewsCn.getNewsText() != null
						&& !"".equals(platNewsCn.getNewsText())) {
					platNewsCn.setNewsText(platNewsCn.getNewsText().replace(
							"\\'", "'"));
				}
				if (platNewsCn.getAuthor() != null
						&& !"".equals(platNewsCn.getAuthor())) {
					platNewsCn.setAuthor(platNewsCn.getAuthor().replace("\\'",
							"'"));
				}
			}
		}

		return pageRes;
	}

}
