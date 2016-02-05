package com.github.lxgang.persistence.mysql.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.lxgang.persistence.beans.User;

public interface UserRepository {
	public User findOneById(Integer id);
	
	public Page<User> findAllByPage(Pageable pageable,User user);
}
