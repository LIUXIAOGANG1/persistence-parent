package com.github.lxgang.persistence.mysql.repositories.impl;

import javax.annotation.Resource;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lxgang.persistence.beans.User;
import com.github.lxgang.persistence.mysql.repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class UserRepositoryImplTest {

    @Resource
    private UserRepository userRepository;
    
    @Test
    public void findOne() {
    	User user = userRepository.findOneById(13);
    	
    	assertNotNull(user);
    	assertEquals("Test", user.getName());
    }
    
    @Test
    public void findAllByPage() {
    	Pageable pageable = new PageRequest(0, 5);
    	User user = new User();
    	
    	Page<User> users = userRepository.findAllByPage(pageable, user);
    	assertNotNull(user);
    	
    	List<User> userList = users.getContent();
    	assertNotNull(userList);
    }
}
