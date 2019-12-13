package platon.com.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import platon.com.po.PlatMenu;
import platon.com.po.PlatUser;
import platon.com.service.PlatMenuService;
import platon.com.service.PlatUserService;

import com.security.interceptor.LoginInterceptor;
import com.util.JsonResp;

@Controller
@RequestMapping("common")
public class CommonController {
	
	private static final Logger logger=Logger.getLogger(CommonController.class);
	
	
	@Autowired
	private PlatMenuService platMenuService;
	@Autowired
	private PlatUserService platUserService; 
	
	
	
	
	/**
	 * 左侧
	 * 获取菜单列表
	 */
	@RequestMapping("findMenu")
	@ResponseBody
	public JsonResp<List<PlatMenu>> findMenu(){
		JsonResp<List<PlatMenu>> resp=new JsonResp<>();
		try {
			List<PlatMenu> list=platMenuService.findMenu();
			resp.setCode(0);
			resp.setData(list);
			return resp;
			
		} catch (Exception e) {
			e.printStackTrace();
			resp.setCode(-1);
			resp.setMessage("系统异常，请联系管理员");
		}
		return resp;
	}
	
	

	/**
	 * 修改密码
	 */
	@RequestMapping("uptUser")
	@ResponseBody
	public JsonResp<Boolean> uptUser(HttpSession session,@RequestBody PlatUser user){
		JsonResp<Boolean> resp=new JsonResp<>();
		try {
			PlatUser u=(PlatUser) session.getAttribute(LoginInterceptor.USER_KEY);
			logger.info("uptUser===》"+u.getLoginName());
			user.setLoginName(u.getLoginName());
			Boolean bool=platUserService.uptPlatUser(user);
			if(bool){
				resp.setCode(0);
				resp.setData(bool);
			}else{
				resp.setCode(1);
				resp.setData(bool);
				resp.setMessage("原密码错误");
			}
			return resp;
			
		} catch (Exception e) {
			e.printStackTrace();
			resp.setCode(-1);
			resp.setMessage("系统异常，请联系管理员");
		}
		return resp;
	}
	
}
