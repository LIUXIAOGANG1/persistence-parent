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
		String sqlFetchRows = "SELECT * FROM (SELECT A.*,ROWNUM RN FROM (SELECT * FROM SFB_ORDER) A WHERE ROWNUM <= 100) WHERE 1=1 ";
		String sqlCountRows = "SELECT COUNT(*) FROM SFB_ORDER WHERE 1=1 ";
		
		String where = "";
		if(user == null){
			return oraclePaginationHelper.fetchPage(oracleJdbcTemplate, sqlCountRows, sqlFetchRows, null, pageable, new BeanPropertyRowMapper<User>(User.class));
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
		
//		if(StringUtils.isNotBlank(queryVo.getActionTimeStart())){
//			where += " AND ACTIONTIME > to_date('" + queryVo.getActionTimeStart() + "', 'yyyy-mm-dd HH24:mi:ss')";
//		}
//		if(StringUtils.isNotBlank(queryVo.getActionTimeEnd())){
//			where += " AND ACTIONTIME < to_date('" + queryVo.getActionTimeEnd() + "', 'yyyy-mm-dd HH24:mi:ss')";
//		}
		
//		String sqlEnd = " ORDER BY CREATETIME DESC ";
		
		return oraclePaginationHelper.fetchPage(oracleJdbcTemplate, sqlCountRows.concat(where), sqlFetchRows, null, pageable, new BeanPropertyRowMapper<User>(User.class));
	}
}
