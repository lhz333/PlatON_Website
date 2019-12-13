package platon.com.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import platon.com.po.PlatFaq;
import platon.com.po.PlatFaqHistoryOpertion;
import platon.com.po.PlatUser;
import platon.com.service.PlatFaqHistoryOpertionService;
import platon.com.service.PlatFaqService;
import platon.com.util.Const;

import com.jdbcTemplateTool.page.Pagination;
import com.security.interceptor.LoginInterceptor;
import com.util.JsonResp;

@Controller
@RequestMapping("faq")
public class FaqController {
	
	
	@Autowired
	private PlatFaqService platFaqService;

	@Autowired
	private PlatFaqHistoryOpertionService platFaqHistoryOpertionService;
	
	/**
	 * 插入faq问答 
	 * @return
	 */
	@RequestMapping(value="insFaq",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> insFaq(HttpSession session,
			@RequestBody PlatFaq faq) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			faq.setCreateTime(new Date());
			faq.setCreator(u.getName());
			if(faq.getProblemCn()!=null && !"".equals(faq.getProblemCn())){
				faq.setProblemCn(faq.getProblemCn().replace("'","\\'"));
			}
			if(faq.getProblemEn()!=null && !"".equals(faq.getProblemEn())){
				faq.setProblemEn(faq.getProblemEn().replace("'","\\'"));
			}
			if(faq.getReplyCn()!=null && !"".equals(faq.getReplyCn())){
				faq.setReplyCn(faq.getReplyCn().replace("'","\\'"));
			}
			if(faq.getReplyEn()!=null && !"".equals(faq.getReplyEn())){
				faq.setReplyEn(faq.getReplyEn().replace("'","\\'"));
			}
			
			
			Integer i=platFaqService.insFaq(faq);
			if(i==null || i==0){
				json.setData(false);
				json.setCode(1);
				json.setMessage("插入失败");
				return json;
			}
			PlatFaqHistoryOpertion opertion=new PlatFaqHistoryOpertion();
			opertion.setFaqId(i);
			opertion.setCreator(u.getName());
			opertion.setCreateTime(new Date());
			opertion.setStatus(Const.opertion_create.getType());
			platFaqHistoryOpertionService.insFaqOpertion(opertion);
			
			json.setData(true);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	/**
	 * * 获取faq列表
	 * @return
	 */
	@RequestMapping(value="findPlatFaq",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatFaq>> findPlatFaq(
			@RequestBody PlatFaq faq) {
		JsonResp<Pagination<PlatFaq>> json = new JsonResp<>();
		try {
			Pagination<PlatFaq> res=platFaqService.findPlatFaq(faq);
			if(res==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("获取失败");
				return json;
			}
			
			json.setData(res);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	/**
	 *官网获取faq 
	 * @return
	 */
	@RequestMapping(value="findPlatFaqOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatFaq>> findPlatFaqOnline(
			@RequestBody PlatFaq faq) {
		JsonResp<Pagination<PlatFaq>> json = new JsonResp<>();
		try {
			Pagination<PlatFaq> res=platFaqService.findPlatFaqOnline(faq);
			if(res==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("获取失败");
				return json;
			}
			
			
			json.setData(res);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	
	/**
	 * 获取单个faq 
	 * @return
	 */
	@RequestMapping(value="findPlatFaqById",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatFaq> findPlatFaqById(
			@RequestBody PlatFaq faq) {
		JsonResp<PlatFaq> json = new JsonResp<>();
		try {
			PlatFaq res=platFaqService.findPlatFaqById(faq);
			if(res==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("获取失败");
				return json;
			}
			if(res.getProblemCn()!=null && !"".equals(res.getProblemCn())){
				res.setProblemCn(res.getProblemCn().replace("\\'","'"));
			}
			if(res.getProblemEn()!=null && !"".equals(res.getProblemEn())){
				res.setProblemEn(res.getProblemEn().replace("\\'","'"));
			}
			if(res.getReplyCn()!=null && !"".equals(res.getReplyCn())){
				res.setReplyCn(res.getReplyCn().replace("\\'","'"));
			}
			if(res.getReplyEn()!=null && !"".equals(res.getReplyEn())){
				res.setReplyEn(res.getReplyEn().replace("\\'","'"));
			}
			
			json.setData(res);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * 官网获取单个faq 
	 * @return
	 */
	@RequestMapping(value="findPlatFaqByIdOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatFaq> findPlatFaqByIdOnline(
			@RequestBody PlatFaq faq) {
		JsonResp<PlatFaq> json = new JsonResp<>();
		try {
			faq.setStatus(Const.STATUS_TRUE.getType());
			PlatFaq res=platFaqService.findPlatFaqById(faq);
			if(res==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("获取失败");
				return json;
			}
			if(res.getProblemCn()!=null && !"".equals(res.getProblemCn())){
				res.setProblemCn(res.getProblemCn().replace("\\'","'"));
			}
			if(res.getProblemEn()!=null && !"".equals(res.getProblemEn())){
				res.setProblemEn(res.getProblemEn().replace("\\'","'"));
			}
			if(res.getReplyCn()!=null && !"".equals(res.getReplyCn())){
				res.setReplyCn(res.getReplyCn().replace("\\'","'"));
			}
			if(res.getReplyEn()!=null && !"".equals(res.getReplyEn())){
				res.setReplyEn(res.getReplyEn().replace("\\'","'"));
			}
			
			json.setData(res);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	
	
	

	/**
	 * 编辑faq
	 * @return
	 */
	@RequestMapping(value="uptFaq",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> uptFaq(HttpSession session,
			@RequestBody PlatFaq faq) {
		JsonResp<Boolean> json = new JsonResp<>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(faq.getProblemCn()==null && faq.getReplyCn()==null
					&& faq.getProblemEn()==null && faq.getReplyEn()==null){
				json.setData(false);
				json.setCode(1);
				json.setMessage("参数不全");
				return json;
			}
			
			if(faq.getProblemCn()!=null && !"".equals(faq.getProblemCn())){
				faq.setProblemCn(faq.getProblemCn().replace("'","\\'"));
			}
			if(faq.getProblemEn()!=null && !"".equals(faq.getProblemEn())){
				faq.setProblemEn(faq.getProblemEn().replace("'","\\'"));
			}
			if(faq.getReplyCn()!=null && !"".equals(faq.getReplyCn())){
				faq.setReplyCn(faq.getReplyCn().replace("'","\\'"));
			}
			if(faq.getReplyEn()!=null && !"".equals(faq.getReplyEn())){
				faq.setReplyEn(faq.getReplyEn().replace("'","\\'"));
			}
			Integer res=platFaqService.uptFaq(faq);
			if(res==null){
				json.setData(false);
				json.setCode(1);
				json.setMessage("修改失败");
				return json;
			}
			
			PlatFaqHistoryOpertion opertion=new PlatFaqHistoryOpertion();
			opertion.setFaqId(faq.getId());
			opertion.setCreator(u.getName());
			opertion.setCreateTime(new Date());
			opertion.setStatus(Const.opertion_edit.getType());
			platFaqHistoryOpertionService.insFaqOpertion(opertion);
			
			
			json.setData(true);
			json.setCode(0);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * faq发布及撤回 删除 
	 * @return
	 */
	@RequestMapping(value="uptFaqStatus",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> uptFaqStatus(HttpSession session,
			@RequestBody PlatFaq faq) {
		JsonResp<Boolean> json = new JsonResp<>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(faq.getId()==null || faq.getStatus()==null){
				json.setData(false);
				json.setCode(1);
				json.setMessage("参数不全");
				return json;
			}
			Integer res=platFaqService.uptFaqStatus(faq);
			if(res==null){
				json.setData(false);
				json.setCode(1);
				json.setMessage("修改失败");
				return json;
			}
			
			PlatFaqHistoryOpertion opertion=new PlatFaqHistoryOpertion();
			opertion.setFaqId(faq.getId());
			opertion.setCreator(u.getName());
			opertion.setCreateTime(new Date());
			if(faq.getStatus()==0)
				opertion.setStatus(Const.opertion_revoke.getType());
			else if(faq.getStatus()==2)
				opertion.setStatus(Const.opertion_del.getType());
			else
				opertion.setStatus(faq.getStatus());
			platFaqHistoryOpertionService.insFaqOpertion(opertion);
			
			
			json.setData(true);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * faq 上移，下移
	 * @return
	 */
	@RequestMapping(value="sortFaqUporDown",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> sortFaqUporDown(HttpSession session,
			@RequestBody PlatFaq faq) {
		JsonResp<Boolean> json = new JsonResp<>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(faq.getSortType()==null || 
					faq.getId()==null ||
					faq.getSortId()==null){
				json.setCode(1);
				json.setData(false);
				json.setMessage("参数不全");
				return json;
			}
			
			platFaqService.sortFaq(faq);
			
			PlatFaqHistoryOpertion opertion=new PlatFaqHistoryOpertion();
			opertion.setFaqId(faq.getId());
			opertion.setCreator(u.getName());
			opertion.setCreateTime(new Date());
			opertion.setStatus(Const.opertion_sort.getType());
			platFaqHistoryOpertionService.insFaqOpertion(opertion);
			
			json.setData(true);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	

	/**
	 * 操作记录 
	 */
	@RequestMapping(value="findFaqCnOpertion", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatFaqHistoryOpertion>> findFaqCnOpertion(
			@RequestBody PlatFaqHistoryOpertion faq) {
		JsonResp<List<PlatFaqHistoryOpertion>> json = new JsonResp<List<PlatFaqHistoryOpertion>>();
		try {
			if(faq.getFaqId()==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("参数不全");
				return json;
			}
			List<PlatFaqHistoryOpertion> list = platFaqHistoryOpertionService.findCnOpertion(faq);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}

}
