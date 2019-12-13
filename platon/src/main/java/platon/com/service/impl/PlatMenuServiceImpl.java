package platon.com.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import platon.com.dao.PlatMenuDao;
import platon.com.po.PlatMenu;
import platon.com.service.PlatMenuService;

@Service
public class PlatMenuServiceImpl  implements PlatMenuService{
	
	@Autowired
	private PlatMenuDao platMenuDao;

	@Override
	public List<PlatMenu> findMenu() {
		//存放结果菜单
		List<PlatMenu> res=new ArrayList<PlatMenu>();
		List<PlatMenu> list=platMenuDao.findPlatMenu();
		//只显示根菜单
		List<PlatMenu> topMenu=new ArrayList<PlatMenu>();
		if(list!=null){
			for (PlatMenu platMenu : list) {
				if(platMenu.getParentId().intValue()==0){
					topMenu.add(platMenu);
				}
			}
		}
		for (PlatMenu platMenu : topMenu) {
			PlatMenu temp=getMenu(platMenu,list);
			res.add(temp);
		}
		return res;
	}
	
	
	public PlatMenu getMenu(PlatMenu topMenu,List<PlatMenu> menus){
		for (PlatMenu platMenu : menus) {
			if(platMenu.getParentId().intValue()==topMenu.getId().intValue()){
				if(topMenu.getChildren()==null){
					List<PlatMenu> temp=new ArrayList<PlatMenu>();
					topMenu.setChildren(temp);
				}
				topMenu.getChildren().add(platMenu);
			}
		}
		return topMenu;
	}
	
	
	
	
}
