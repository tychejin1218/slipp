package net.slipp.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import net.slipp.domain.users.User;
import net.slipp.domain.users.UserTest;

public class MybatisTest {

	private static final Logger log = LoggerFactory.getLogger(UserTest.class);

	private SqlSessionFactory sqlSessionFactory;

	@Before
	public void setup() throws Exception {
		String resource = "mybatis-config-test.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		// MyBatis 연동 전에 DBCP 연결
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource("slipp.sql"));
		DatabasePopulatorUtils.execute(resourceDatabasePopulator, getDataSource());
		log.info("database initialized success!");
	}

	private DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:~/slipp");
		dataSource.setUsername("sa");
		return dataSource;
	}

	@Test
	public final void gettingStarted() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			User user = session.selectOne("UserMapper.findById", "admin");
			log.debug("User:[{}]", user);
		}
	}

	@Test
	public void insert() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			User user = new User("admin1", "password", "관리자", "admin1@naver.com");
			session.insert("UserMapper.create", user);
			User actual = session.selectOne("UserMapper.findById", user.getUserId());
			assertThat(actual, is(user));
		}
	}
}
