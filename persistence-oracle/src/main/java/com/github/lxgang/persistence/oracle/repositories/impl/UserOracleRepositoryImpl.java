package com.github.lxgang.persistence.oracle.repositories.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.lxgang.persistence.beans.User;
import com.github.lxgang.persistence.oracle.pageable.OraclePaginationHelper;
import com.github.lxgang.persistence.oracle.repositories.UserOracleRepository;

@Repository
public class UserOracleRepositoryImpl implements UserOracleRepository {

	@Resource
	private JdbcTemplate oracleJdbcTemplate;
	
	private OraclePaginationHelper<User> oraclePaginationHelper = new OraclePaginationHelper<User>();
	
	@Override
	public User insert(User user) {
		String sql = "INSERT INTO USER(ID, NAME, AGE, SEX) "
				+ "VALUES(?, ?, ?, ?)";
		
		Object[] args = new Object[] {user.getId(), user.getName(), user.getAge(), user.getSex()};
		
		int result = oracleJdbcTemplate.update(sql, args);
		if(result == 1){
			user = this.findOneById(user.getId());
			return user;
		}
		return null;
	}

	@Override
	public User findOneById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Page<User> findAllByPage(Pageable pageable, User user) {
		String sqlCountRows = "SELECT COUNT(*) FROM USER WHERE 1=1 ";
		
		String sqlPrefix = "SELECT * FROM (SELECT A.*,ROWNUM RN FROM (";
		String sqlInner = "SELECT * FROM USER WHERE 1=1 ";
		String sqlSuffix = ") A ) WHERE 1=1 ";
		
		String where = "";
		if(user == null){
			return oraclePaginationHelper.fetchPage(oracleJdbcTemplate, sqlCountRows, sqlPrefix, sqlInner, sqlSuffix, null, pageable, new BeanPropertyRowMapper<User>(User.class));
		}
		
		if(user.getId() != null){
			where += " AND ID = " + user.getId();
		}
		if(StringUtils.isNotBlank(user.getName())){
			where += " AND NAME = '" + user.getName() + "'";
		}
		if(StringUtils.isNotBlank(user.getSex())){
			where += " AND SEX = '" + user.getSex() + "'";
		}
		if(user.getAge() != null){
			where += " AND AGE = " + user.getAge();
		}
		
//		if(StringUtils.isNotBlank(user.getActionTimeStart())){
//			where += " AND ACTIONTIME > to_date('" + user.getActionTimeStart() + "', 'yyyy-mm-dd HH24:mi:ss')";
//		}
//		if(StringUtils.isNotBlank(user.getActionTimeEnd())){
//			where += " AND ACTIONTIME < to_date('" + user.getActionTimeEnd() + "', 'yyyy-mm-dd HH24:mi:ss')";
//		}
		
		sqlInner = sqlInner.concat(where);
//		String sqlEnd = " ORDER BY CREATETIME DESC ";
		
		return oraclePaginationHelper.fetchPage(oracleJdbcTemplate, sqlCountRows.concat(where), sqlPrefix, sqlInner, sqlSuffix, null, pageable, new BeanPropertyRowMapper<User>(User.class));
	}
}
