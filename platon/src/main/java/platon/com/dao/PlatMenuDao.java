package platon.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatMenu;

import com.jdbcTemplateTool.JdbcTemplateTool;

@Repository
public class PlatMenuDao {
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	
	
	/**
	 * 获取菜单
	 */
	public List<PlatMenu> findPlatMenu(){
		
		String sql="SELECT * FROM `plat_menu`";
		List<PlatMenu> list=jdbcTemplateTool.findList(sql,null, PlatMenu.class);
		return list;
	}
	
	
}
