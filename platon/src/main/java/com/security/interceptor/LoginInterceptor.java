package com.security.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.util.JsonResp;
import com.util.JsonUtils;

public class LoginInterceptor implements HandlerInterceptor{
	
	private static final Logger logger=Logger.getLogger(LoginInterceptor.class);
	
	public static final String USER_KEY="user";
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse  response, Object handler) throws Exception {
		 //获取请求的RUi:去除http:localhost:8080这部分剩下的
         String uri = request.getRequestURI();
       
         //UTL:除了login.jsp是可以公开访问的，其他的URL都进行拦截控制
         if (checkUrl(uri)) {
             return true;
         }
//         logFilter(request,response,handler);
         //获取session
         HttpSession session = request.getSession();
         Object user=session.getAttribute(USER_KEY);
         
         if(user==null || "".equals(user) ){
        	 
        	 JsonResp<String> res=new JsonResp<>();
             
//             httpResponse.setStatus(httpStatusCode);
//             response.sendRedirect("/platon_web/index.html");	
//             request.getRequestDispatcher("/index.html").forward(request, response);
//             response.setCharacterEncoding("UTF-8");
             response.setContentType("application/json; charset=utf-8");
             res.setData("您还没有登录，请先登录");
             res.setCode(9999);
             PrintWriter writer = null;
             try {
                 writer = response.getWriter();
                 writer.write(JsonUtils.toJsonString(res));
                 writer.flush();
             } catch (IOException e) {
                 e.printStackTrace();
             } finally {
                 if (writer != null) {
                     writer.close();
                 }
             }
             return false;
         }

         return true;
		
	}

	public Boolean checkUrl(String uri){
		if( uri.indexOf("/login") >= 0 ||
			uri.indexOf("Online") >= 0 ||
			uri.indexOf("readImg") >= 0 ){
			return true;
		}
		return false;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	public void logFilter(HttpServletRequest request,
			HttpServletResponse  response, Object handler) throws Exception{
		// 所有请求第一个进入的方法
				String reqURL = request.getRequestURL().toString();
			    String ip = request.getRemoteHost ();
			    
				InputStream  is = request.getInputStream ();
			    StringBuilder responseStrBuilder = new StringBuilder ();
			    BufferedReader streamReader = new BufferedReader (new InputStreamReader (is,"UTF-8"));
			    String inputStr;
			     while ((inputStr = streamReader.readLine ()) != null)
		         responseStrBuilder.append (inputStr);
//			     System.out.println("请求参数: " + responseStrBuilder.toString ());
			     String parmeter = responseStrBuilder.toString();
			     
			   long startTime = System.currentTimeMillis();
		       request.setAttribute("startTime", startTime);
		       if (handler instanceof HandlerMethod) {
		           StringBuilder sb = new StringBuilder(1000);
//		           sb.append("-----------------------").append(startTime).append("-------------------------------------\n");
		           HandlerMethod h = (HandlerMethod) handler;
				   //Controller 的包名
		           sb.append("Controller: ").append(h.getBean().getClass().getName()).append("\n");
				   //方法名称
		           sb.append("Method    : ").append(h.getMethod().getName()).append("\n");
				   //请求方式  post\put\get 等等
		           sb.append("RequestMethod    : ").append(request.getMethod()).append("\n");
				   //所有的请求参数
		           sb.append("Params    : ").append(parmeter).append("\n");
				   //部分请求链接
		           sb.append("URI       : ").append(request.getRequestURI()).append("\n");
				    //完整的请求链接
		           sb.append("AllURI    : ").append(reqURL).append("\n");
				   //请求方的 ip地址
		           sb.append("request IP: ").append(ip).append("\n");
		           logger.info(sb.toString());
		       }
	}
	
	
}
