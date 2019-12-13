package platon.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platon.com.po.PlatUser;

import com.jdbcTemplateTool.JdbcTemplateTool;
@Repository
public class PlatUserDao {
	
	@Autowired
	private JdbcTemplateTool jdbcTemplateTool;
	
	
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public PlatUser findPlatUser(PlatUser user){
		StringBuilder sb=new StringBuilder();
		sb.append("select * from plat_user where login_name=?");
		List<PlatUser> users=jdbcTemplateTool.findList(sb.toString(),new Object[]{user.getLoginName()},PlatUser.class);
		if(users!=null && users.size()>0){
			user=users.get(0);
			return user;
		}
		return null;
		
	}
	
	
	/**
	 * 修改密码
	 * @throws Exception 
	 */
	public boolean uptPlatUser(PlatUser user) throws Exception{
		int i=jdbcTemplateTool.update(user);
		if(i>0)
			return true;
		return false;
	}
	
}
