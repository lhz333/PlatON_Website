package com.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.model.Users;


@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	    @Override  
	    public void onAuthenticationSuccess(HttpServletRequest request,  
	            HttpServletResponse response, Authentication authentication)  
	            throws IOException, ServletException {  
	        // 认证成功后，获取用户信息并添加到session中  
	    	Users userDetails = (Users) authentication.getPrincipal();  
//	        MangoUser user = userService.getUserByName(userDetails.getUsername());  
	        request.getSession().setAttribute("user", userDetails);  
	        super.onAuthenticationSuccess(request, response, authentication);  
	      
	    }  
	
}
