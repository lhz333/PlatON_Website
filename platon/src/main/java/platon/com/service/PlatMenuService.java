package platon.com.service;

import java.util.List;

import platon.com.po.PlatMenu;

public interface PlatMenuService {
	
	
	/**
	 * 获取左侧菜单
	 */
	
	public List<PlatMenu> findMenu();
	
	
}
