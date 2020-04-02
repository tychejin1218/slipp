package net.slipp.dao.users;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.slipp.domain.users.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class UserDaoTest {

	private static final Logger log = LoggerFactory.getLogger(UserDaoTest.class);

	@Autowired
	private UserDao userDao;

	@Test
	public void findById() {
		User user = userDao.findById("tychejin");
		log.debug("User : {}", user);
	}

	@Test
	public void create() {
		User user = new User("tychejin2", "password", "박진영1", "tychejin2@naver.com");
		userDao.create(user);
		User actual = userDao.findById(user.getUserId());
		
		assertThat(actual, is(user));
	}
}
