package platon.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import platon.com.dao.PlatUserDao;
import platon.com.po.PlatUser;
import platon.com.service.PlatUserService;

import com.util.SHA;

@Service
public class PlatUserServiceImpl implements PlatUserService{
	
	@Autowired
	private PlatUserDao platdao;
	
	@Override
	public PlatUser findPlatUser(PlatUser user) {
		return platdao.findPlatUser(user);
		
	}
	@Transactional
	@Override
	public boolean uptPlatUser(PlatUser user) throws Exception {
		PlatUser newUser=platdao.findPlatUser(user);
		if(newUser.getPassword().equals(SHA.encryptSHA(user.getPassword()))){
			newUser.setPassword(SHA.encryptSHA(user.getRePassWord()));
			return platdao.uptPlatUser(newUser);
		}
		return false;
		
	}

}
