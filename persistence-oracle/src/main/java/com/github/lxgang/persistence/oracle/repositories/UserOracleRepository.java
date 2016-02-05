package com.github.lxgang.persistence.oracle.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.lxgang.persistence.beans.User;

public interface UserOracleRepository{
	public User insert(User order);
	
	public User findOneById(Integer id);

	public Page<User> findAllByPage(Pageable pageable,User user);
}
