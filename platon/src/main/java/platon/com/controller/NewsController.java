package platon.com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import platon.com.po.PlatNews;
import platon.com.po.PlatNewsCn;
import platon.com.po.PlatNewsCnEn;
import platon.com.po.PlatNewsEn;
import platon.com.po.PlatNewsHistoryOpertionCn;
import platon.com.po.PlatNewsHistoryOpertionEn;
import platon.com.po.PlatUser;
import platon.com.service.PlatNewsCnEnService;
import platon.com.service.PlatNewsHistoryOpertionService;
import platon.com.service.PlatNewsService;
import platon.com.util.Const;
import sun.misc.BASE64Encoder;

import com.jdbcTemplateTool.page.Pagination;
import com.security.interceptor.LoginInterceptor;
import com.util.JsonResp;

@Controller
@RequestMapping("news")
public class NewsController {

	private static final Logger logger=Logger.getLogger(NewsController.class);
	
	
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

	@Autowired
	private PlatNewsService platNewsService;

	@Autowired
	private PlatNewsCnEnService platNewsCnEnService;


	@Autowired
	private PlatNewsHistoryOpertionService platNewsHistoryOpertionService;
	
	
	
	/**
	 * 新增新闻 中文
	 * 
	 * @return
	 */	
	@RequestMapping(value="saveNewsCnInfo", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> saveNewsCnInfo(HttpSession session,@RequestBody PlatNewsCn platNews) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(u==null)
				u=new PlatUser();
			platNews.setCreator(u.getName());
			platNews.setCreateTime(new Date());
			//正则替换
			if(platNews.getNewsText()!=null && !"".equals(platNews.getNewsText())){
				String text=platNews.getNewsText().replaceAll("http.+?imgUrl=", "imagesUrl=");
				platNews.setNewsText(text);
			}
			if(platNews.getTitle()!=null && !"".equals(platNews.getTitle())){
				platNews.setTitle(platNews.getTitle().replace("'","\\'"));
			}
			if(platNews.getNewsText()!=null && !"".equals(platNews.getNewsText())){
				platNews.setNewsText(platNews.getNewsText().replace("'","\\'"));
			}
			if(platNews.getAuthor()!=null && !"".equals(platNews.getAuthor())){
				platNews.setAuthor(platNews.getAuthor().replace("'","\\'"));
			}
			platNewsService.insNewsCn(platNews,u);
			
			if(platNews.getId()>0){
				json.setCode(0);
				json.setMessage("新增成功");
				json.setData(false);
			}else{
				json.setCode(1);
				json.setMessage("新增失败");
				json.setData(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}

		return json;
	}

	/**
	 * 新增新闻 英文
	 * 
	 * @return
	 */
	@RequestMapping(value="saveNewsEnInfo",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> saveNewsEnInfo(HttpSession session,HttpServletRequest request,
			HttpServletResponse response, @RequestBody PlatNewsEn platNews) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(u==null)
				u=new PlatUser();
			
			platNews.setCreator(u.getName());
			platNews.setCreateTime(new Date());
			//正则替换
			if(platNews.getNewsText()!=null && !"".equals(platNews.getNewsText())){
				String text=platNews.getNewsText().replaceAll("http.+?imgUrl=", "imagesUrl=");
				platNews.setNewsText(text);
			}
			if(platNews.getTitle()!=null && !"".equals(platNews.getTitle())){
				platNews.setTitle(platNews.getTitle().replace("'","\\'"));
			}
			if(platNews.getNewsText()!=null && !"".equals(platNews.getNewsText())){
				platNews.setNewsText(platNews.getNewsText().replace("'","\\'"));
			}
			if(platNews.getAuthor()!=null && !"".equals(platNews.getAuthor())){
				platNews.setAuthor(platNews.getAuthor().replace("'","\\'"));
			}
			platNewsService.insNewsEn(platNews,u);
			
			if(platNews.getId()>0){
				json.setCode(0);
				json.setMessage("新增成功");
				json.setData(false);
			}else{
				json.setCode(1);
				json.setMessage("新增失败");
				json.setData(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}

		return json;
	}

	/**
	 * 获取新闻列表 中文
	 * 
	 * @return
	 */
	@RequestMapping(value="findNewsCnInfo",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsCn>> findNewsCnInfo(
			@RequestBody PlatNewsCn platNews) {
		JsonResp<Pagination<PlatNewsCn>> json = new JsonResp<Pagination<PlatNewsCn>>();
		try {
			Pagination<PlatNewsCn> list = platNewsService
					.findPlatNewsCn(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}

	/**
	 * 获取新闻列表 英文
	 * 
	 * @return
	 */
	@RequestMapping(value="findNewsEnInfo",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsEn>> findNewsEnInfo(
			@RequestBody PlatNewsEn platNews) {
		JsonResp<Pagination<PlatNewsEn>> json = new JsonResp<Pagination<PlatNewsEn>>();
		try {
			Pagination<PlatNewsEn> list = platNewsService
					.findPlatNewsEn(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * 获取官网新闻列表 中文
	 * web端
	 * @return
	 */
	@RequestMapping(value="findNewsCnInfoOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsCn>> findNewsCnInfoOnline(
			@RequestBody PlatNewsCn platNews) {
		JsonResp<Pagination<PlatNewsCn>> json = new JsonResp<Pagination<PlatNewsCn>>();
		try {
			Pagination<PlatNewsCn> list = platNewsService
					.findPlatNewsCnOnline(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	/**
	 * 获取官网新闻列表 中文
	 * 移动端
	 * @return
	 */
	@RequestMapping(value="findNewsCnInfoMobileOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsCn>> findNewsCnInfoMobileOnline(
			@RequestBody PlatNewsCn platNews) {
		JsonResp<Pagination<PlatNewsCn>> json = new JsonResp<Pagination<PlatNewsCn>>();
		try {
			Pagination<PlatNewsCn> list = platNewsService
					.findPlatNewsCnMobileOnline(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * 获取官网新闻列表 英文
	 * web
	 * @return
	 */
	@RequestMapping(value="findNewsEnInfoOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsEn>> findNewsEnInfoOnline(
			@RequestBody PlatNewsEn platNews) {
		JsonResp<Pagination<PlatNewsEn>> json = new JsonResp<Pagination<PlatNewsEn>>();
		try {
			Pagination<PlatNewsEn> list = platNewsService
					.findPlatNewsEnOnline(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	
	/**
	 * 获取官网新闻列表 英文
	 * 移动端
	 * @return
	 */
	@RequestMapping(value="findNewsEnInfoMobileOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsEn>> findNewsEnInfoMobileOnline(
			@RequestBody PlatNewsEn platNews) {
		JsonResp<Pagination<PlatNewsEn>> json = new JsonResp<Pagination<PlatNewsEn>>();
		try {
			Pagination<PlatNewsEn> list = platNewsService
					.findPlatNewsEnMobileOnline(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * 获取新闻列表未绑定列表
	 * 中文
	 * @return
	 */
	@RequestMapping(value="findNotBindPlatNewsCn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsCn>> findNotBindPlatNewsCn(
			@RequestBody PlatNewsCn platNews) {
		JsonResp<Pagination<PlatNewsCn>> json = new JsonResp<Pagination<PlatNewsCn>>();
		try {
			Pagination<PlatNewsCn> list = platNewsService
					.findNotBindPlatNewsCn(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * 获取新闻列表未绑定列表
	 * 英文
	 * @return
	 */
	@RequestMapping(value="findNotBindPlatNewsEn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsEn>> findNotBindPlatNewsEn(
			@RequestBody PlatNewsEn platNews) {
		JsonResp<Pagination<PlatNewsEn>> json = new JsonResp<Pagination<PlatNewsEn>>();
		try {
			Pagination<PlatNewsEn> list = platNewsService
					.findNotBindPlatNewsEn(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	

	/**
	 * 获取单个新闻
	 * 中文
	 * @return
	 */
	@RequestMapping(value="findNewsCnInfoById",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatNewsCn> findNewsCnInfoById(
			@RequestBody PlatNewsCn news) {
		JsonResp<PlatNewsCn> json = new JsonResp<>();
		try {
			PlatNewsCn cn=platNewsService.findPlatNewsCnById(news);
			if(cn==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("获取失败");
				return json;
			}
			if(cn.getTitle()!=null && !"".equals(cn.getTitle())){
				cn.setTitle(cn.getTitle().replace("\\'","'"));
			}
			if(cn.getNewsText()!=null && !"".equals(cn.getNewsText())){
				cn.setNewsText(cn.getNewsText().replace("\\'","'"));
			}
			if(cn.getAuthor()!=null && !"".equals(cn.getAuthor())){
				cn.setAuthor(cn.getAuthor().replace("\\'","'"));
			}
			
			json.setData(cn);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	/**
	 * 获取单个新闻
	 * 英文
	 * @return
	 */
	@RequestMapping(value="findNewsEnInfoById",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatNewsEn> findNewsEnInfoById(
			@RequestBody PlatNewsEn news) {
		JsonResp<PlatNewsEn> json = new JsonResp<>();
		try {
			PlatNewsEn en=platNewsService.findPlatNewsEnById(news);
			if(en==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("获取失败");
				return json;
			}
			if(en.getTitle()!=null && !"".equals(en.getTitle())){
				en.setTitle(en.getTitle().replace("\\'","'"));
			}
			if(en.getNewsText()!=null && !"".equals(en.getNewsText())){
				en.setNewsText(en.getNewsText().replace("\\'","'"));
			}
			if(en.getAuthor()!=null && !"".equals(en.getAuthor())){
				en.setAuthor(en.getAuthor().replace("\\'","'"));
			}
			json.setData(en);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	

	/**
	 * 获取单个新闻  online
	 * 中文
	 * @return
	 */
	@RequestMapping(value="findNewsCnInfoByIdOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatNewsCn> findNewsCnInfoByIdOnline(
			@RequestBody PlatNewsCn news) {
		JsonResp<PlatNewsCn> json = new JsonResp<>();
		try {
			news.setStatus(Const.STATUS_TRUE.getType());
			PlatNewsCn cn=platNewsService.findPlatNewsCnById(news);
			if(cn==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("获取失败");
				return json;
			}
			if(cn.getTitle()!=null && !"".equals(cn.getTitle())){
				cn.setTitle(cn.getTitle().replace("\\'","'"));
			}
			if(cn.getNewsText()!=null && !"".equals(cn.getNewsText())){
				cn.setNewsText(cn.getNewsText().replace("\\'","'"));
			}
			if(cn.getAuthor()!=null && !"".equals(cn.getAuthor())){
				cn.setAuthor(cn.getAuthor().replace("\\'","'"));
			}
			
			json.setData(cn);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	/**
	 * 获取单个新闻 online
	 * 英文
	 * @return
	 */
	@RequestMapping(value="findNewsEnInfoByIdOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatNewsEn> findNewsEnInfoByIdOnline(
			@RequestBody PlatNewsEn news) {
		JsonResp<PlatNewsEn> json = new JsonResp<>();
		try {
			news.setStatus(Const.STATUS_TRUE.getType());
			PlatNewsEn en=platNewsService.findPlatNewsEnById(news);
			if(en==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("获取失败");
				return json;
			}
			if(en.getTitle()!=null && !"".equals(en.getTitle())){
				en.setTitle(en.getTitle().replace("\\'","'"));
			}
			if(en.getNewsText()!=null && !"".equals(en.getNewsText())){
				en.setNewsText(en.getNewsText().replace("\\'","'"));
			}
			if(en.getAuthor()!=null && !"".equals(en.getAuthor())){
				en.setAuthor(en.getAuthor().replace("\\'","'"));
			}
			json.setData(en);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	
	
	
	/**
	 *  中英文详情页切换
	 *  
	 * @return
	 */
	@RequestMapping(value="findNewsDetail",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatNews> findNewsDetail(
			@RequestBody PlatNewsCnEn cnen) {
		JsonResp<PlatNews> json = new JsonResp<>();
		try {
			PlatNews news=platNewsService.findNewsDtail(cnen);
			if(news==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("未找到新闻");
				return json;
			}
			if(news.getTitle()!=null && !"".equals(news.getTitle())){
				news.setTitle(news.getTitle().replace("\\'","'"));
			}
			if(news.getNewsText()!=null && !"".equals(news.getNewsText())){
				news.setNewsText(news.getNewsText().replace("\\'","'"));
			}
			if(news.getAuthor()!=null && !"".equals(news.getAuthor())){
				news.setAuthor(news.getAuthor().replace("\\'","'"));
			}
			json.setData(news);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	

	/**
	 *  中英文详情页切换
	 *  
	 * @return
	 */
	@RequestMapping(value="findNewsDetailOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatNews> findNewsDetailOnline(
			@RequestBody PlatNewsCnEn cnen) {
		JsonResp<PlatNews> json = new JsonResp<>();
		try {
			cnen.setStatus(Const.STATUS_TRUE.getType());
			PlatNews news=platNewsService.findNewsDtail(cnen);
			if(news==null){
				json.setData(null);
				json.setCode(1);
				return json;
			}
			if(news.getTitle()!=null && !"".equals(news.getTitle())){
				news.setTitle(news.getTitle().replace("\\'","'"));
			}
			if(news.getNewsText()!=null && !"".equals(news.getNewsText())){
				news.setNewsText(news.getNewsText().replace("\\'","'"));
			}
			if(news.getAuthor()!=null && !"".equals(news.getAuthor())){
				news.setAuthor(news.getAuthor().replace("\\'","'"));
			}
			json.setData(news);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	
	

	/**
	 * 
	 * 新闻状态操作 中文
	 * 发布 、撤回、删除
	 */
	@RequestMapping(value="uptPlatNewsCnStatus",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Integer> uptPlatNewsCnStatus(HttpSession session,
			@RequestBody PlatNewsCn platNews) {
		JsonResp<Integer> json = new JsonResp<Integer>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(u==null)
				u=new PlatUser();
			
			if(platNews.getId()==null || platNews.getStatus()==null){
				json.setData(0);
				json.setCode(1);
				json.setMessage("参数不全");
				return json;
			}
			int i = platNewsService.uptNewsCnStatus(platNews,u);
			if(i>0){
			
			json.setData(i);
			json.setCode(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	

	/**
	  * 新闻状态操作  英文
	 * 发布 、撤回、删除
	 */
	@RequestMapping(value="uptPlatNewsEnStatus",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Integer> uptPlatNewsEnStatus(HttpSession session,
			@RequestBody PlatNewsEn platNews) {
		JsonResp<Integer> json = new JsonResp<Integer>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(u==null)
				u=new PlatUser();
			
			if(platNews.getId()==null || platNews.getStatus()==null){
				json.setData(0);
				json.setCode(1);
				json.setMessage("参数不全");
				return json;
			}
			int i = platNewsService.uptNewsEnStatus(platNews,u);
			if(i>0){
				json.setData(i);
				json.setCode(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * 
	 * 编辑新闻内容
	 * 中文
	 */
	@RequestMapping(value="uptNewsCn", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Integer> uptNewsCn(HttpSession session,
			@RequestBody PlatNewsCn platNews) {
		JsonResp<Integer> json = new JsonResp<Integer>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(u==null)
				u=new PlatUser();
			if(platNews.getId()==null || "".equals(platNews.getId())){
				json.setCode(1);
				json.setData(0);
				json.setMessage("参数不全");
				return json;
			}
			//正则替换
			if(platNews.getNewsText()!=null && !"".equals(platNews.getNewsText())){
				String text=platNews.getNewsText().replaceAll("http.+?imgUrl=", "imagesUrl=");
				platNews.setNewsText(text);
			}
			if(platNews.getTitle()!=null && !"".equals(platNews.getTitle())){
				platNews.setTitle(platNews.getTitle().replace("'","\\'"));
			}
			if(platNews.getNewsText()!=null && !"".equals(platNews.getNewsText())){
				platNews.setNewsText(platNews.getNewsText().replace("'","\\'"));
			}
			if(platNews.getAuthor()!=null && !"".equals(platNews.getAuthor())){
				platNews.setAuthor(platNews.getAuthor().replace("'","\\'"));
			}
			Integer res = platNewsService.uptNewsCn(platNews,u);
			if(res!=null){
				
				
				json.setData(res);
				json.setCode(0);
				json.setMessage("修改成功");
			}else{
				json.setMessage("修改失败");
				json.setData(res);
				json.setCode(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	/**
	 * 
	 * 编辑新闻内容
	 *	英文
	 */
	@RequestMapping(value="uptNewsEn", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Integer> uptNewsEn(HttpSession session,
			@RequestBody PlatNewsEn platNews) {
		JsonResp<Integer> json = new JsonResp<Integer>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(u==null)
				u=new PlatUser();
			if(platNews.getId()==null || "".equals(platNews.getId())){
				json.setCode(1);
				json.setData(0);
				json.setMessage("参数不全");
				return json;
			}
			//正则替换
			if(platNews.getNewsText()!=null && !"".equals(platNews.getNewsText())){
				String text=platNews.getNewsText().replaceAll("http.+?imgUrl=", "imagesUrl=");
				platNews.setNewsText(text);
			}	
			if(platNews.getTitle()!=null && !"".equals(platNews.getTitle())){
				platNews.setTitle(platNews.getTitle().replace("'","\\'"));
			}
			if(platNews.getNewsText()!=null && !"".equals(platNews.getNewsText())){
				platNews.setNewsText(platNews.getNewsText().replace("'","\\'"));
			}
			if(platNews.getAuthor()!=null && !"".equals(platNews.getAuthor())){
				platNews.setAuthor(platNews.getAuthor().replace("'","\\'"));
			}
			Integer res = platNewsService.uptNewsEn(platNews,u);
			if(res!=null){
				json.setData(res);
				json.setCode(0);
				json.setMessage("修改成功");
			}else{
				json.setMessage("修改失败");
				json.setData(res);
				json.setCode(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	
	
	
	


	/**
	 * 操作记录 中文
	 */
	@RequestMapping(value="findNewsCnOpertion", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatNewsHistoryOpertionCn>> findNewsCnOpertion(
			@RequestBody PlatNewsHistoryOpertionCn platNews) {
		JsonResp<List<PlatNewsHistoryOpertionCn>> json = new JsonResp<List<PlatNewsHistoryOpertionCn>>();
		try {
			if(platNews.getNewId()==null || "".equals(platNews.getNewId())){
				json.setData(null);
				json.setCode(1);
				json.setMessage("参数不全");
				return json;
			}
			List<PlatNewsHistoryOpertionCn> list = platNewsHistoryOpertionService
					.findCnOpertion(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}

	/**
	 * 操作记录 英文
	 */
	@RequestMapping(value="findNewsEnOpertion", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatNewsHistoryOpertionEn>> findNewsEnOpertion(
			@RequestBody PlatNewsHistoryOpertionEn platNews) {
		JsonResp<List<PlatNewsHistoryOpertionEn>> json = new JsonResp<List<PlatNewsHistoryOpertionEn>>();
		try {
			if(platNews.getNewId()==null || "".equals(platNews.getNewId())){
				json.setData(null);
				json.setCode(1);
				json.setMessage("参数不全");
				return json;
			}
			List<PlatNewsHistoryOpertionEn> list = platNewsHistoryOpertionService
					.findEnOpertion(platNews);
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	

	
	/**
	 * 上移，下移
	 *中文
	 */
	@RequestMapping(value="sortNewsCnUporDown", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> sortNewsCnUporDown(
			@RequestBody PlatNewsCn platNews) {
		JsonResp<Boolean> json = new JsonResp<>();
		
		try {
			if(platNews.getSortId()==null || 
					platNews.getSortType()==null || 
					platNews.getId()==null){
				json.setCode(1);
				json.setData(false);
				json.setMessage("参数不全");
				return json;
			}
			platNewsService.sortNewsCnUporDown(platNews);
			json.setCode(0);
			json.setData(true);
			json.setMessage("成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	/**
	 * 上移，下移
	 *英文
	 */
	@RequestMapping(value="sortNewsEnUporDown", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> sortNewsEnUporDown(
			@RequestBody PlatNewsEn platNews) {
		JsonResp<Boolean> json = new JsonResp<>();
		
		try {
			if(platNews.getSortId()==null || 
					platNews.getSortType()==null || 
					platNews.getId()==null){
				json.setCode(1);
				json.setData(false);
				json.setMessage("参数不全");
				return json;
			}
			
			platNewsService.sortNewsEnUporDown(platNews);
			json.setCode(0);
			json.setData(true);
			json.setMessage("成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	
	/**
	 * 新增关联
	 */
	@RequestMapping(value="bindNewsCnEn", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> bindNewsCnEns(HttpSession session,
			@RequestBody PlatNewsCnEn news) {
		JsonResp<Boolean> json = new JsonResp<>();
		PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
		try {
			if(u==null)
				u=new PlatUser();
			if(news.getCnId()==null || news.getEnId()==null){
				json.setCode(1);
				json.setData(false);
				json.setMessage("参数不全");
				return json;
			}
			news.setCreator(u.getName());
			news.setCreateTime(new Date());
			news.setStatus(Const.STATUS_TRUE.getType());
			Integer i=platNewsCnEnService.insPlatNewsCnEn(news);
			
			if(i==0){
				json.setCode(1);
				json.setData(false);
				json.setMessage("绑定失败");
			}else{
				json.setCode(0);
				json.setData(true);
				json.setMessage("绑定成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	

	/**
	 * 验证绑定关系
	 */
	@RequestMapping(value="validateNewsCnEn", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatNewsCnEn> validateNewsCnEn(
			@RequestBody PlatNewsCnEn news) {
		JsonResp<PlatNewsCnEn> json = new JsonResp<>();
		try {
			if(news.getCnId()==null && news.getEnId()==null){
				json.setCode(1);
				json.setData(null);
				json.setMessage("参数不全");
				return json;
			}
			PlatNewsCnEn res=platNewsCnEnService.findPlatNewsCnEn(news);
			if(res==null){
				json.setCode(1);
				json.setData(null);
				json.setMessage("获取失败");
				return json;
			}
			json.setCode(0);
			json.setData(res);
			json.setMessage("成功");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	

	/**
	 * 删除绑定关系
	 */
	@RequestMapping(value="removeNewsCnen", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> removeNewsCnen(
			@RequestBody PlatNewsCnEn news) {
		JsonResp<Boolean> json = new JsonResp<>();
		try {
			if(news.getCnId()==null && news.getEnId()==null){
				json.setCode(1);
				json.setData(false);
				json.setMessage("参数不全");
				return json;
			}
			Boolean res=platNewsCnEnService.removeNewsCnEn(news);
			if(res){
				json.setCode(0);
				json.setData(res);
				json.setMessage("删除成功");
				return json;
			}else{
				json.setCode(1);
				json.setData(false);
				json.setMessage("删除失败");
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	

	@RequestMapping(value = "newsUploadImg", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<String> newsUploadImg(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResp<String> json = new JsonResp<>();
		try {
			json.setCode(0);
			String path = System.getProperty("user.dir").concat(File.separator).concat("..").concat(File.separator).concat("..").
					concat(File.separator).concat("..").concat(File.separator).concat("image").concat(File.separator);
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // 将request转化为MultipartHttpServletRequest
			List<MultipartFile> multipartFiles = multipartRequest
					.getFiles("imgUrl");
			if (multipartFiles != null && multipartFiles.size() > 0) {
				for (MultipartFile multipartFile : multipartFiles) {
					String url=fileWrite(multipartFile,path);
					if(url==null){
						json.setCode(1);
						json.setData(null);
						json.setMessage("图片上传失败");
						return json;
					}
					json.setData(url);
				}
			}else{
				json.setCode(1);
				json.setData(null);
				json.setMessage("图片上传失败");
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}

	/**
	 * 读取图片信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "readImg", method = RequestMethod.GET)
	@ResponseBody
	public String IoReadImage(String imgUrl, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// ServletOutputStream out = null;
		// FileInputStream ips = null;
		// try {
		// if(imgUrl==null || "".equals(imgUrl)){
		// return;
		// }
		// // 获取图片存放路径
		// String imgPath = System.getProperty("user.dir").concat(
		// File.separator)
		// + imgUrl;
		// ips = new FileInputStream(new File(imgPath));
		// response.setContentType("multipart/form-data");
		// out = response.getOutputStream();
		// // 读取文件流
		// int len = 0;
		// byte[] buffer = new byte[1024 * 10];
		// while ((len = ips.read(buffer)) != -1) {
		// out.write(buffer, 0, len);
		// }
		// out.flush();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// out.close();
		// ips.close();
		// }

		FileInputStream ips = null;
		if (imgUrl == null || "".equals(imgUrl)) {
			return null;
		}
		// 获取图片存放路径
		String imgPath = System.getProperty("user.dir").concat(File.separator)
				+ imgUrl;
		ips = new FileInputStream(new File(imgPath));
		byte[] data = null;
		// 读取图片字节数组
		try {
			data = new byte[ips.available()];
			ips.read(data);
			ips.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ips.close();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		// 返回Base64编码过的字节数组字符串
		if(data!=null){
			return "data:image/jpeg;base64,"+encoder.encode(data);
		}
		return null;
		//
		// out.flush();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// out.close();
		// ips.close();
		// }

	}

	/**
	 * 获取日期
	 * 
	 * @return
	 */
	public String dateFormat() {
		return df.format(new Date());
	}
	
	

	/**
	 * 获取日期
	 * 
	 * @return
	 */
	public Long dateTimeFormat() {
		return new Date().getTime();
	}
	
	/**
	 * 文件写入
	 * @param multipartFile  文件
	 * @param path  根路径
	 * @return
	 */
	public String fileWrite(MultipartFile multipartFile, String path) {
		String paths = dateFormat().concat(File.separator);
		String[] fileName = multipartFile.getOriginalFilename().split("\\.");// 获取文件的格式
		FileOutputStream downloadFile = null;
		InputStream is;
		try {
			is = multipartFile.getInputStream();
			try {
				int index;
				File file = new File(path + paths);
				if (!file.exists()) {
					file.mkdirs();
				}
				// 图片文件
				paths += dateFormat()+""+dateTimeFormat()+"."+fileName[1];
				logger.info("path========="+path);
				logger.info("paths========="+paths);
				file = new File(path + paths);
				downloadFile = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				while ((index = is.read(bytes)) != -1) {
					downloadFile.write(bytes, 0, index);
					downloadFile.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				downloadFile.close();
				is.close();
			}
			return paths;
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	

	/**
	 * 相关阅读
	 * 中文
	 */
	@RequestMapping(value="findPlatNewsCnByLableId", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatNewsCn>> findPlatNewsCnByLableId(
			@RequestBody PlatNews news) {
		JsonResp<List<PlatNewsCn>> json = new JsonResp<List<PlatNewsCn>>();
		try {
			Integer length=news.getLength();
			List<Integer> ids=news.getLableIds();
			if(length==null){
				news.setLength(3);
			}
			if(length>=5){
				news.setLength(5);
			}
			if(ids==null || ids.size()<1 || news.getId()==null){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			List<PlatNewsCn> list=platNewsService.findPlatNewsCnByLableId(news);
			json.setCode(Const.PARAMS_0.getType());
			json.setData(list);
			return json;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
			return json;
		}
		
		
	}
	
	

	/**
	 * 相关阅读
	 * 英文
	 */
	@RequestMapping(value="findPlatNewsEnByLableId", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatNewsEn>> findPlatNewsEnByLableId(
			@RequestBody PlatNews news) {
		JsonResp<List<PlatNewsEn>> json = new JsonResp<List<PlatNewsEn>>();
		try {
			Integer length=news.getLength();
			List<Integer> ids=news.getLableIds();
			if(length==null){
				news.setLength(3);
			}
			if(length>=5){
				news.setLength(5);
			}
			if(ids==null || ids.size()<1 || news.getId()==null){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			
			List<PlatNewsEn> list=platNewsService.findPlatNewsEnByLableId(news);
			json.setCode(Const.PARAMS_0.getType());
			json.setData(list);
			return json;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
			return json;
		}
		
		
	}
	

	/**
	 * 相关阅读
	 * 中文
	 */
	@RequestMapping(value="findPlatNewsCnByLableIdOnline", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatNewsCn>> findPlatNewsCnByLableIdOnline(
			@RequestBody PlatNews news) {
		JsonResp<List<PlatNewsCn>> json = new JsonResp<List<PlatNewsCn>>();
		try {
			Integer length=news.getLength();
			List<Integer> ids=news.getLableIds();
			if(length==null){
				news.setLength(3);
			}
			if(length>=5){
				news.setLength(5);
			}
			if(ids==null || ids.size()<1 || news.getId()==null){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			news.setStatus(Const.STATUS_TRUE.getType());
			List<PlatNewsCn> list=platNewsService.findPlatNewsCnByLableId(news);
			json.setCode(Const.PARAMS_0.getType());
			json.setData(list);
			return json;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
			return json;
		}
		
		
	}
	
	

	/**
	 * 相关阅读
	 * 英文
	 */
	@RequestMapping(value="findPlatNewsEnByLableIdOnline", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatNewsEn>> findPlatNewsEnByLableIdOnline(
			@RequestBody PlatNews news) {
		JsonResp<List<PlatNewsEn>> json = new JsonResp<List<PlatNewsEn>>();
		try {
			Integer length=news.getLength();
			List<Integer> ids=news.getLableIds();
			if(length==null){
				news.setLength(3);
			}
			if(length>=5){
				news.setLength(5);
			}
			if(ids==null || ids.size()<1 || news.getId()==null){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			news.setStatus(Const.STATUS_TRUE.getType());
			List<PlatNewsEn> list=platNewsService.findPlatNewsEnByLableId(news);
			json.setCode(Const.PARAMS_0.getType());
			json.setData(list);
			return json;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
			return json;
		}
		
		
	}
	
	
	/**
	 * 根据标签获取新闻列表
	 * 中文
	 * @return
	 */
	@RequestMapping(value="pagePlatNewsCnByLableIdOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsCn>> pagePlatNewsCnByLableId(
			@RequestBody PlatNews news) {
		JsonResp<Pagination<PlatNewsCn>> json = new JsonResp<Pagination<PlatNewsCn>>();
		try {
			if(news.getLable()==null || "".equals(news.getLable().toString().trim())){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			
			news.setStatus(Const.STATUS_TRUE.getType());
			Pagination<PlatNewsCn> list = platNewsService
					.pagePlatNewsCnByLableId(news);
			json.setCode(Const.PARAMS_0.getType());
			json.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 *根据标签获取新闻列表
	 * 英文
	 * @return
	 */
	@RequestMapping(value="pagePlatNewsEnByLableIdOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatNewsEn>> pagePlatNewsEnByLableId(
			@RequestBody PlatNews news) {
		JsonResp<Pagination<PlatNewsEn>> json = new JsonResp<Pagination<PlatNewsEn>>();
		try {
			if(news.getLable()==null || "".equals(news.getLable().toString().trim())){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			
			news.setStatus(Const.STATUS_TRUE.getType());
			Pagination<PlatNewsEn> list = platNewsService
					.pagePlatNewsEnByLableId(news);
			json.setCode(Const.PARAMS_0.getType());
			json.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}

}
