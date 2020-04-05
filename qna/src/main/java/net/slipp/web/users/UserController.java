package net.slipp.web.users;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.slipp.dao.users.UserDao;
import net.slipp.domain.users.User;

@Controller
@RequestMapping("/users")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	/*Autowired를 사용해서 UserDao와 UserController 간의 의존관계를 설정 
	- 소스 구현 시에는 의존관계 설정을 위해서는 setMethod가 있어야지 UserDao 인스턴스 주입이 가능했는데, spring에서 자바 리플렉션을 통해서 UserDao를 인스턴스를주입할 수 있도록 제공
	- setMethod를 만들어서 @Autowired 의존관계를 설정하여도 상관없음*/
	@Autowired
	private UserDao userDao;

	/*@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}*/

	/*@RequestMapping("/form")
	public String form() {
		return "users/form";
	}*/

	@RequestMapping("/form")
	public String form(Model model) {
		model.addAttribute("user", new User());
		return "users/form";
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String crate(User user) {
		log.debug("user : {}", user);
		userDao.create(user);
		log.debug("Database : {}", userDao.findById(user.getUserId()));
		return "users/form";
	}
}
