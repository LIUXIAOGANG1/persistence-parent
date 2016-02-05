package com.github.lxgang.persistence.mysql.repositories.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.lxgang.persistence.beans.User;
import com.github.lxgang.persistence.mysql.pageable.MySQLPaginationHelper;
import com.github.lxgang.persistence.mysql.repositories.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	private MySQLPaginationHelper<User> mysqlPaginationHelper = new MySQLPaginationHelper<User>();
	
	@Override
	public User findOneById(Integer id) {
		String sql = "SELECT * FROM user WHERE id = ?";
		
        List<User> users = jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<User>(User.class));
		
        if(users == null || users.isEmpty()){
        	return null;
        }
        
		return users.get(0);
	}

	@Override
	public Page<User> findAllByPage(Pageable pageable, User user) {
		String sqlFetchRows = "SELECT * FROM user WHERE 1 = 1";
		String sqlCountRows = "SELECT COUNT(*) FROM user WHERE 1 = 1";
		
		String where = "";
		if(user == null){
			return mysqlPaginationHelper.fetchPage(jdbcTemplate, sqlCountRows, sqlFetchRows, null, pageable, new BeanPropertyRowMapper<User>(User.class));
		}
		
		if(user.getId() != null){
			where += " AND id = " + user.getId();
		}
		if(StringUtils.isNotBlank(user.getName())){
			where += " AND name = '" + user.getName() + "'";
		}
		if(StringUtils.isNotBlank(user.getSex())){
			where += " AND sex = '" + user.getSex() + "'";
		}
		if(user.getAge() != null){
			where += " AND age = " + user.getAge();
		}
		
//		String sqlEnd = " ORDER BY CREATETIME DESC ";
		
		return mysqlPaginationHelper.fetchPage(jdbcTemplate, sqlCountRows.concat(where), sqlFetchRows.concat(where), null, pageable, new BeanPropertyRowMapper<User>(User.class));
	}
}
