import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import platon.com.dao.PlatNewsDao;
import platon.com.po.PlatFaq;
import platon.com.po.PlatNewsCn;
import platon.com.po.PlatNewsCnEn;
import platon.com.po.PlatSubscription;
import platon.com.service.PlatFaqService;
import platon.com.service.PlatNewsHistoryOpertionService;
import platon.com.service.PlatNewsService;
import platon.com.service.PlatSubscriptionService;

import com.jdbcTemplateTool.page.Pagination;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={ "classpath*:spring-mvc.xml"})
public class test111 {
	
	@Autowired
	private PlatNewsService platNewsService;
	@Autowired
	private PlatNewsHistoryOpertionService platNewsHistoryOpertionService;
	
	@Autowired
	private PlatNewsDao platNewsDao;
	
	@Autowired
	private PlatFaqService platFaqService;
	@Autowired
	private PlatSubscriptionService platSubscriptionService;
	
	@Test
	public void test() throws Exception {
		PlatNewsCn news=new PlatNewsCn();
		news.setPageNo(1);
		news.setPageSize(3);
		Pagination<PlatNewsCn> list=platNewsService.findPlatNewsCnOnline(news);
		System.out.println(list.getResult().get(0).getTitle());
		news.setPageNo(3);
		list=platNewsService.findPlatNewsCnOnline(news);
		System.out.println(list.getResult().get(0).getTitle());
		
		
		
		
////		
//		news.setId(1);
//		news= platNewsService.findPlatNewsCnById(news);
//		System.out.println(news.getTitle());
//		news.setId(1);
//		news.setTitle("sfsfs");
//		news.setReleaseTime(new Date());
//		 platNewsService.uptNewsCn(news);
		
	}
	@Test
	public void tesssave() {
		PlatNewsCnEn news=new PlatNewsCnEn();
		news.setEnId(1);
		try {
			
			platNewsDao.findNewsDtail(news);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(news);
		
	}

	@Test
	public void xxxx() {
		PlatNewsCn news=new PlatNewsCn();
		news.setTitle("231313");
		try {

//			PlatNewsHistoryOpertionCn platNewsHistoryOpertion=new PlatNewsHistoryOpertionCn();
//			platNewsHistoryOpertion.setNewId(1);
//			platNewsHistoryOpertion.setCreator("11");
//			platNewsHistoryOpertion.setStatus(1);
//			platNewsHistoryOpertion.setCreateTime(new Date());
//			platNewsHistoryOpertionService.insCnOpertion(platNewsHistoryOpertion);
			
			PlatSubscription platSubscription=new PlatSubscription();
			platSubscription.setStrStaTime("2018-01-01");
			platSubscription.setStrEndTime("2018-10-10");
			List<PlatSubscription> list=platSubscriptionService.findListPlatSubscription(platSubscription);
			System.out.println(list.size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(news);
		
	}
	
	@Test
	public void faq() {
		PlatFaq faq=new PlatFaq();
		try {
			faq.setProblemCn("1");
			faq.setProblemEn("2");
			faq.setReplyCn("3");
			faq.setReplyEn("4");
			platFaqService.insFaq(faq);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
}
