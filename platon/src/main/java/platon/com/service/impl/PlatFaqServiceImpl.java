package platon.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdbcTemplateTool.page.Page;
import com.jdbcTemplateTool.page.Pagination;

import platon.com.dao.PlatFaqDao;
import platon.com.po.PlatFaq;
import platon.com.po.PlatNewsCn;
import platon.com.service.PlatFaqService;

@Service
public class PlatFaqServiceImpl  implements PlatFaqService{
	
	@Autowired
	private PlatFaqDao platFaqDao;

	@Override
	public Integer insFaq(PlatFaq faq) throws Exception {
		faq.setSortId(platFaqDao.findMaxId()+1);
		return platFaqDao.insFaq(faq);
	}

	@Override
	public Pagination<PlatFaq> findPlatFaq(PlatFaq faq) {
		Pagination<PlatFaq> pageRes=platFaqDao.findPlatFaq(faq,new Page(faq.getPageNo(),faq.getPageSize()));
		pageRes.setPageSize(faq.getPageSize());
		if(pageRes.getTotalCount()>0){
			List<PlatFaq> list=pageRes.getResult();
			int no=0;
			if((faq.getPageNo()-1)*faq.getPageSize()<=0){
				no=1;
			}else{
				no=(faq.getPageNo()-1)*faq.getPageSize()+1;
			}
			for (PlatFaq f : list) {
				if(f.getProblemCn()!=null && !"".equals(f.getProblemCn())){
					f.setProblemCn(f.getProblemCn().replace("\\'","'"));
				}
				if(f.getProblemEn()!=null && !"".equals(f.getProblemEn())){
					f.setProblemEn(f.getProblemEn().replace("\\'","'"));
				}
				if(f.getReplyCn()!=null && !"".equals(f.getReplyCn())){
					f.setReplyCn(f.getReplyCn().replace("\\'","'"));
				}
				if(f.getReplyEn()!=null && !"".equals(f.getReplyEn())){
					f.setReplyEn(f.getReplyEn().replace("\\'","'"));
				}
				
				f.setDescNo(no++);
			}
		}
		return pageRes;
	}

	@Override
	public Pagination<PlatFaq> findPlatFaqOnline(PlatFaq faq) {
		Pagination<PlatFaq> pageRes=platFaqDao.findPlatFaqOnline(faq,new Page(faq.getPageNo(),faq.getPageSize()));
		pageRes.setPageSize(faq.getPageSize());
		if(pageRes.getTotalCount()>0){
			List<PlatFaq> list=pageRes.getResult();
			int no=0;
			if((faq.getPageNo()-1)*faq.getPageSize()<=0){
				no=1;
			}else{
				no=(faq.getPageNo()-1)*faq.getPageSize()+1;
			}
			for (PlatFaq f : list) {
				if(f.getProblemCn()!=null && !"".equals(f.getProblemCn())){
					f.setProblemCn(f.getProblemCn().replace("\\'","'"));
				}
				if(f.getProblemEn()!=null && !"".equals(f.getProblemEn())){
					f.setProblemEn(f.getProblemEn().replace("\\'","'"));
				}
				if(f.getReplyCn()!=null && !"".equals(f.getReplyCn())){
					f.setReplyCn(f.getReplyCn().replace("\\'","'"));
				}
				if(f.getReplyEn()!=null && !"".equals(f.getReplyEn())){
					f.setReplyEn(f.getReplyEn().replace("\\'","'"));
				}
				f.setDescNo(no++);
			}
		}
		return pageRes;
	}

	@Override
	public PlatFaq findPlatFaqById(PlatFaq faq) {
		return platFaqDao.findPlatFaqById(faq);
	}

	@Override
	public Integer uptFaq(PlatFaq faq) throws Exception {
		return platFaqDao.uptFaq(faq);
	}

	@Override
	public int uptFaqStatus(PlatFaq faq) throws Exception {
		return platFaqDao.uptFaqStatus(faq);
	}

	@Override
	public Integer sortFaq(PlatFaq faq) throws Exception {
		PlatFaq next=platFaqDao.findFaqByStatus(faq);
		if(next!=null && next.getId()>0){
			int temp=next.getSortId();
			next.setSortId(faq.getSortId());
			faq.setSortId(temp);
			platFaqDao.sortFaq(faq);
			platFaqDao.sortFaq(next);
		}
		return 1;
	}
	
}
