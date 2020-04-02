package net.slipp.dao.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import net.slipp.domain.users.User;

public class UserDao extends JdbcDaoSupport {

	@PostConstruct
	public void initialize() {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource("slipp.sql"));
		DatabasePopulatorUtils.execute(resourceDatabasePopulator, getDataSource());
	}

	public User findById(String userId) {

		String sql = "select * from USERS where userId = ?";

		RowMapper<User> rowMapper = new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
			}
		};

		return getJdbcTemplate().queryForObject(sql, rowMapper, userId);
	}

	public void create(User user) {

		String sql = "insert into USERS values(?, ?, ?, ?)";

		getJdbcTemplate().update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
	}
}
