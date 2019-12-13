package platon.com.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import platon.com.po.PlatUser;
import platon.com.service.PlatUserService;

import com.security.interceptor.LoginInterceptor;
import com.util.JsonResp;
import com.util.SHA;

@Controller
@RequestMapping("login")
public class LoginControll {
	protected static Logger logger = Logger.getLogger(LoginControll.class);
	
	@Autowired
	private PlatUserService platUserService;

    
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public JsonResp<PlatUser> login(HttpSession session,@RequestBody PlatUser user) {
    	JsonResp<PlatUser> res=new JsonResp<>();
    	try {
    		res.setCode(1);
			res.setMessage("帐号或密码错误");
			if(user.getLoginName()!=null && !"".equals(user.getLoginName())){
				PlatUser u=platUserService.findPlatUser(user);
				if(u!=null){
					String pas=SHA.encryptSHA(user.getPassword());
					if(u.getPassword().equals(pas)){
						session.setAttribute(LoginInterceptor.USER_KEY, u);
						res.setCode(0);
						res.setMessage("登录成功");
						res.setData(u);
						return res;
					}
				}
			}
			return res;
    		
		} catch (Exception e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			e.printStackTrace();
		}
        return res;
    }
    
    
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    @ResponseBody
    public JsonResp<Boolean> logoutPage (HttpServletRequest request, HttpServletResponse response) {
    	JsonResp<Boolean> json=new JsonResp<Boolean>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        json.setCode(0);
        return json;
    }
    
    
    
	
}
