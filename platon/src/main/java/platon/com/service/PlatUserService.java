package platon.com.service;

import platon.com.po.PlatUser;

public interface PlatUserService {
	
	
	/**
	 * 根据用户名获取用户信息
	 * @param user
	 * @return
	 */
	PlatUser findPlatUser(PlatUser user);
	
	/**
	 * 
	 * 更新对象信息
	 * @param user
	 * @return
	 */
	boolean uptPlatUser(PlatUser user) throws Exception;
	
}
