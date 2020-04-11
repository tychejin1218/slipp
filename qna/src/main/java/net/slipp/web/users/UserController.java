package net.slipp.web.users;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.slipp.dao.users.UserDao;
import net.slipp.domain.users.Authenticate;
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
	public String crate(@Valid User user, BindingResult bindingResult) {
		log.debug("user : {}", user);
		if (bindingResult.hasErrors()) {
			log.debug("bindingResult has error!");
			List<ObjectError> errors = bindingResult.getAllErrors();
			for (ObjectError error : errors) {
				log.debug("error : {}, {}", error.getCode(), error.getDefaultMessage());
			}
			return "users/form";
		}
		userDao.create(user);
		log.debug("Database : {}", userDao.findById(user.getUserId()));
		return "redirect:/";
	}

	@RequestMapping("/login/form")
	public String loginForm(Model model) {
		model.addAttribute("authenticate", new Authenticate());
		return "users/login";
	}

	@RequestMapping("/login")
	public String login(@Valid Authenticate authenticate, BindingResult bindingResult, HttpSession session, Model model) {
		if (bindingResult.hasErrors()) {
			return "users/login";
		}

		User user = userDao.findById(authenticate.getUserId());
		if (user == null) {
			// TODO 에러 처리 - 존재하지 않는 사용자입니다.
			model.addAttribute("errorMessage", "존재하지 않는 사용자입니다.");
			return "users/login";
		}

		if (!user.getPassword().equals(authenticate.getPassword())) {
			// TODO 에러 처리 - 비밀번호가 틀립니다.
			model.addAttribute("errorMessage", "비밀번호가 틀립니다.");
			return "users/login";
		}

		// TODO 세션에 사용자 정보 저장
		session.setAttribute("userId", user.getUserId());

		return "redirect:/";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("userId");
		return "redirect:/";
	}
}
