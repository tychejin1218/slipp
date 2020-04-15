package net.slipp.dao.users;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.slipp.domain.users.User;

@Repository("userDao")
public class MyBatisUserDao implements UserDao {

	private static final Logger log = LoggerFactory.getLogger(jdbcUserDao.class);

	//@Autowired
	@Resource(name = "sqlSession")
	private SqlSession sqlSession;

	//private DataSource dataSource; 

	/*@PostConstruct
	public void initialize() {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource("slipp.sql"));
		DatabasePopulatorUtils.execute(resourceDatabasePopulator, dataSource);
		log.info("database initialized success!");
	}*/

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/*public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}*/

	@Override
	public User findById(String userId) {
		return sqlSession.selectOne("UserMapper.findById", userId);
	}

	@Override
	public void create(User user) {
		sqlSession.insert("UserMapper.create", user);
	}

	@Override
	public void update(User user) {
		sqlSession.insert("UserMapper.update", user);
	}
}